package com.n26.service;

import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import com.n26.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class TransactionServiceImplTest {

    private final StatisticsServiceImpl statisticsService = Mockito.mock(StatisticsServiceImpl.class);
    private final long expirationTime = 60;
    private final TransactionServiceImpl transactionService = new TransactionServiceImpl(statisticsService, expirationTime);

    @Before
    public void setUp() {
        Mockito.reset(statisticsService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullRequest() throws UnParsableTransactionException, OlderTransactionException {
        transactionService.create(null);
    }

    @Test
    public void createTest() throws UnParsableTransactionException, OlderTransactionException {
        Mockito.doNothing().when(statisticsService).register(Mockito.any(Transaction.class));
        transactionService.create(TestUtil.transaction(LocalDateTime.now()));
        Mockito.verify(statisticsService, Mockito.times(1)).register(Mockito.any(Transaction.class));
    }

    @Test(expected = UnParsableTransactionException.class)
    public void createThrowsUnParsableTransactionExceptionTest() throws UnParsableTransactionException, OlderTransactionException {
        Mockito.doNothing().when(statisticsService).register(Mockito.any(Transaction.class));
        transactionService.create(TestUtil.transaction(LocalDateTime.now().plusMinutes(2)));
        Mockito.verify(statisticsService, Mockito.times(0)).register(Mockito.any(Transaction.class));
    }

    @Test(expected = OlderTransactionException.class)
    public void createThrowsOlderTransactionExceptionExceptionTest() throws UnParsableTransactionException, OlderTransactionException {
        Mockito.doNothing().when(statisticsService).register(Mockito.any(Transaction.class));
        transactionService.create(TestUtil.transaction(LocalDateTime.now().minusMinutes(2)));
        Mockito.verify(statisticsService, Mockito.times(0)).register(Mockito.any(Transaction.class));
    }

    @Test
    public void deleteTest() throws UnParsableTransactionException, OlderTransactionException {
        Mockito.doNothing().when(statisticsService).clearStatistics();
        transactionService.delete();
        Mockito.verify(statisticsService, Mockito.times(1)).clearStatistics();
    }

}
