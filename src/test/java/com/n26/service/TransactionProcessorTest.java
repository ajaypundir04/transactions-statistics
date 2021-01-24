package com.n26.service;

import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionProcessorTest {

    TransactionProcessor transactionProcessor = new TransactionProcessor();

    @Test
    public void testAddTransaction() {
        Transaction transaction = TestUtil.transaction(LocalDateTime.now().minusSeconds(1));
        transactionProcessor.addTransaction(transaction);
        Statistics[] store = transactionProcessor.getStore();
        Statistics actual = Arrays.stream(store).filter(Objects::nonNull).findFirst().get();
        Statistics expected = new Statistics(4, 4, 4, 4, 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddMultipleTransactionAtSameInstant() {
        Transaction transaction = TestUtil.transaction(LocalDateTime.now());
        transactionProcessor.addTransaction(transaction);
        transactionProcessor.addTransaction(transaction);
        Statistics[] store = transactionProcessor.getStore();
        Statistics actual = Arrays.stream(store).filter(Objects::nonNull).findFirst().get();
        Statistics expected = new Statistics(8, 4, 4, 4, 2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddMultipleTransactionAtDifferentInstant() {
        Transaction transaction = TestUtil.transaction(LocalDateTime.now());
        transactionProcessor.addTransaction(transaction);
        transaction = TestUtil.transaction(LocalDateTime.now().minusSeconds(10));
        transactionProcessor.addTransaction(transaction);
        Statistics[] store = transactionProcessor.getStore();
        List<Statistics> list = Arrays.stream(store).filter(Objects::nonNull).collect(Collectors.toList());
        Statistics expected = new Statistics(4, 4, 4, 4, 1);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(expected, list.get(0));
        Assert.assertEquals(expected, list.get(1));
    }

    @Test
    public void testAddOlderTransactionsTest() {
        Transaction transaction = TestUtil.transaction(LocalDateTime.now());
        transactionProcessor.addTransaction(transaction);
        transaction = TestUtil.transaction(LocalDateTime.now().minusSeconds(60));
        transactionProcessor.addTransaction(transaction);
        Statistics[] store = transactionProcessor.getStore();
        List<Statistics> list = Arrays.stream(store).filter(Objects::nonNull).collect(Collectors.toList());
        Statistics expected = new Statistics(4, 4, 4, 4, 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(expected, list.get(0));
    }

    @Test(expected = UnParsableTransactionException.class)
    public void testAddFutureTransactions() {
        Transaction transaction = TestUtil.transaction(LocalDateTime.now());
        transactionProcessor.addTransaction(transaction);
        transaction = TestUtil.transaction(LocalDateTime.now().plusSeconds(1));
        transactionProcessor.addTransaction(transaction);
    }

    @Test
    public void testClearTransactions() {
        Transaction transaction = TestUtil.transaction(LocalDateTime.now());
        transactionProcessor.addTransaction(transaction);
        transactionProcessor.clear();
        List<Statistics> list = Arrays.stream(transactionProcessor.getStore()).filter(Objects::nonNull).collect(Collectors.toList());
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testGetStore() throws InterruptedException {
        Transaction transaction = TestUtil.transaction(LocalDateTime.now().minusSeconds(60));
        transactionProcessor.addTransaction(transaction);
        transaction = TestUtil.transaction(LocalDateTime.now());
        transactionProcessor.addTransaction(transaction);
        List<Statistics> list = Arrays.stream(transactionProcessor.getStore()).filter(Objects::nonNull).collect(Collectors.toList());
        Statistics expected = new Statistics(4, 4, 4, 4, 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(expected, list.get(0));
    }

}
