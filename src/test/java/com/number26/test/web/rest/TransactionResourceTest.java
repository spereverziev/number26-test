package com.number26.test.web.rest;

import com.number26.test.Number26TestApplication;
import com.number26.test.model.Transaction;
import com.number26.test.repository.TransactionRepository;
import com.number26.test.service.TransactionService;
import com.number26.test.web.rest.dto.TransactionDto;
import com.number26.test.web.rest.errors.ExceptionTranslator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Number26TestApplication.class)
@WebAppConfiguration
@IntegrationTest
public class TransactionResourceTest {

    public static final double DEFAULT_AMOUNT = 10.0;
    public static final String DEFAULT_TYPE = "type";
    @Inject
    private TransactionService transactionService;

    @Inject
    private TransactionRepository transactionRepository;

    private MockMvc restMvc;

    @Before
    public void setUp() {
        TransactionResource transactionResource = new TransactionResource();
        ReflectionTestUtils.setField(transactionResource, "transactionService", transactionService);

        this.restMvc = MockMvcBuilders
                .standaloneSetup(transactionResource)
                .setControllerAdvice(new ExceptionTranslator())
                .build();

    }

    @After
    public void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    public void testCreateAnswerWithoutParentId() throws Exception {
        //given
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(DEFAULT_AMOUNT);
        transactionDto.setType(DEFAULT_TYPE);

        assertThat(transactionRepository.findAll()).isEmpty();

        //when
        restMvc.perform(
                put("/transactionservice/transaction/1")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(transactionDto)))
                .andExpect(status().isOk());


        //then
        assertThat(transactionRepository.findAll()).hasSize(1);
        Transaction actual = transactionRepository.findOne(1L);

        assertThat(actual.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(actual.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    public void testCreateAnswerWithParentId() throws Exception {
        //given
        Transaction transactionParent = new Transaction();
        transactionParent.setId(1L);
        transactionParent.setAmount(DEFAULT_AMOUNT);
        transactionParent.setType(DEFAULT_TYPE);
        transactionRepository.save(transactionParent);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(DEFAULT_AMOUNT);
        transactionDto.setType(DEFAULT_TYPE);
        transactionDto.setParentId(1L);

        assertThat(transactionRepository.findAll()).hasSize(1);

        //when
        restMvc.perform(
                put("/transactionservice/transaction/2")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(transactionDto)))
                .andExpect(status().isOk());


        //then
        assertThat(transactionRepository.findAll()).hasSize(2);
        Transaction actual = transactionRepository.findOne(2L);

        assertThat(actual.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(actual.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(transactionParent.getChildTransaction()).isEqualToComparingFieldByField(actual);
    }

    @Test
    public void testCreateAnswerWithParentId_shouldReturnBadRequest_whenCyclicReference() throws Exception {
        //given
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(DEFAULT_AMOUNT);
        transactionDto.setType(DEFAULT_TYPE);
        transactionDto.setParentId(1L);

        //when
        ResultActions resultActions = restMvc.perform(
                put("/transactionservice/transaction/1")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(transactionDto)));


        //then
        resultActions.andExpect(status().isBadRequest());

        assertThat(transactionRepository.findAll()).hasSize(0);
    }

    @Test
    public void testGetSum() throws Exception {
        //given
        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setAmount(1.0);
        transactionDto1.setType(DEFAULT_TYPE);
        transactionService.addTransaction(1L, transactionDto1);

        TransactionDto transactionDto2 = new TransactionDto();
        transactionDto2.setAmount(2.0);
        transactionDto2.setType(DEFAULT_TYPE);
        transactionDto2.setParentId(1L);
        transactionService.addTransaction(2L, transactionDto2);

        TransactionDto transactionDto3 = new TransactionDto();
        transactionDto3.setAmount(3.0);
        transactionDto3.setType(DEFAULT_TYPE);
        transactionDto3.setParentId(2L);
        transactionService.addTransaction(3L, transactionDto3);

        //when
        ResultActions resultForTransaction1 = restMvc.perform(
                get("/transactionservice/sum/1")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8));

        ResultActions resultForTransaction3 = restMvc.perform(
                get("/transactionservice/sum/3")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8));


        //then
        resultForTransaction1.andExpect(status().isOk());
        resultForTransaction1.andExpect(jsonPath("$.sum").value(6.0));

        resultForTransaction3.andExpect(status().isOk());
        resultForTransaction3.andExpect(jsonPath("$.sum").value(3.0));
    }
}