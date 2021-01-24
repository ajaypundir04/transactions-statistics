package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class StatisticsServiceImplTest {

    private final TransactionProcessor transactionProcessor = Mockito.mock(TransactionProcessor.class);
    private final StatisticsServiceImpl statisticsService = new StatisticsServiceImpl(transactionProcessor);

    @Before
    public void setUp() {
        Mockito.reset(transactionProcessor);
    }


    @Test
    public void registerTest() {
        ArgumentCaptor<Transaction> valueCapture = ArgumentCaptor.forClass(Transaction.class);
        Mockito.doNothing().when(transactionProcessor).addTransaction(valueCapture.capture());
        Transaction transaction = TestUtil.transaction(LocalDateTime.now());
        statisticsService.register(transaction);
        Mockito.verify(transactionProcessor, Mockito.times(1)).addTransaction(Mockito.any(Transaction.class));
        Assert.assertEquals(transaction, valueCapture.getValue());
    }

    @Test
    public void getStatisticsTest() {
        Statistics [] store = TestUtil.store();
        Mockito.when(transactionProcessor.getStore()).thenReturn(store);
        Statistics statistics = statisticsService.getStatistics();
        Mockito.verify(transactionProcessor, Mockito.times(1)).getStore();
        Statistics expected = new Statistics(30, 3.75, 5, 2, 8);
        Assert.assertEquals(expected, statistics);
    }

}
