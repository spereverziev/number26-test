package com.number26.test.repository;

import com.number26.test.model.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
    }

    @After
    public void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    public void testSave() {
        //given
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(10.0);
        transaction.setType("type");

        assertThat(transactionRepository.findAll()).isEmpty();

        //when
        transactionRepository.save(transaction);

        //then
        assertThat(transactionRepository.findAll()).hasSize(1);
        assertThat(transactionRepository.findOne(1L)).isEqualToComparingFieldByField(transaction);
    }

    @Test(expected = NullPointerException.class)
    public void testSave_shouldThrowNPE_whenTransactionNull() {
        //given
        Transaction transaction = null;

        assertThat(transactionRepository.findAll()).isEmpty();

        //when
        transactionRepository.save(transaction);

        //then
        assertThat(transactionRepository.findAll()).hasSize(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSave_shouldThrowIllegalArgumentException_whenTransactionIdIsNull() {
        //given
        Transaction transaction = new Transaction();
        transaction.setAmount(10.0);
        transaction.setType("type");

        assertThat(transactionRepository.findAll()).isEmpty();

        //when
        transactionRepository.save(transaction);

        //then
        assertThat(transactionRepository.findAll()).hasSize(0);
    }

    @Test
    public void testFindOne() {
        //given
        assertThat(transactionRepository.findAll()).isEmpty();

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(10.0);
        transaction.setType("type");

        transactionRepository.save(transaction);

        //when
        Transaction result = transactionRepository.findOne(1L);

        //then
        assertThat(result).isEqualToComparingFieldByField(transaction);
    }

    @Test
    public void testFindAllByType() {
        //given
        String type1 = "type1";
        String type2 = "type2";

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(10.0);
        transaction1.setType(type1);

        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(10.0);
        transaction2.setType(type1);

        transactionRepository.save(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setId(3L);
        transaction3.setAmount(10.0);
        transaction3.setType(type2);

        transactionRepository.save(transaction3);


        //when
        Set<Long> allByType1 = transactionRepository.findAllByType(type1);
        Set<Long> allByType2 = transactionRepository.findAllByType(type2);

        //then
        assertThat(allByType1).hasSize(2);
        assertThat(allByType1).containsExactly(1L, 2L);

        assertThat(allByType2).hasSize(1);
        assertThat(allByType2).containsExactly(3L);
    }

}