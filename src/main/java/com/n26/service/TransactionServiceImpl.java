package com.n26.service;

import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final StatisticsService statisticsService;
    @Value("${transactions.expire-limit-in-seconds:60}")
    private long expireLimitInSecond;

    @Autowired
    public TransactionServiceImpl(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public void removeTransactions() {
        statisticsService.clearStatistics();
    }

    protected void createTransactionAtInstant(Transaction tx, Instant instant)
            throws OlderTransactionException, UnParsableTransactionException {

        LocalDateTime currentTime = LocalDateTime.now();
        if (tx.getTimestamp().isAfter(currentTime))
            throw new UnParsableTransactionException();

        if (tx.getTimestamp().isBefore(currentTime.minusSeconds(60)))
            throw new OlderTransactionException();

        statisticsService.register(tx);
    }

    public void createTransaction(Transaction transaction) throws OlderTransactionException, UnParsableTransactionException {
        createTransactionAtInstant(transaction, Instant.now());
    }

    public long getExpireLimitInSecond() {
        return expireLimitInSecond;
    }

}
