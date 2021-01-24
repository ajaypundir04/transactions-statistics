package com.n26.service;

import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author Ajay Singh Pundir
 * Handles the trasactions operation.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final StatisticsService statisticsService;
    private final long expireLimitInSecond;

    @Autowired
    public TransactionServiceImpl(StatisticsService statisticsService,
                                  @Value("${transactions.expire-limit-in-seconds:60}")
                                          long expireLimitInSecond) {
        this.statisticsService = statisticsService;
        this.expireLimitInSecond = expireLimitInSecond;
    }

    /**
     *  Valid transactions will be considered for the statistics calculations.
     * @param transaction
     * @throws OlderTransactionException if it's an older transaction
     * @throws UnParsableTransactionException if it's not valid transaction
     */
    public void create(Transaction transaction) throws OlderTransactionException, UnParsableTransactionException {
        Assert.notNull(transaction, "Transaction can not be null.");
        createTransactionAtInstant(transaction, Instant.now());
    }

    /**
     * Clear all the transaction.
     */
    public void delete() {
        statisticsService.clearStatistics();
    }

    private void createTransactionAtInstant(Transaction transaction, Instant instant)
            throws OlderTransactionException, UnParsableTransactionException {

        LocalDateTime currentTime = LocalDateTime.now();
        if (transaction.getTimestamp().isAfter(currentTime))
            throw new UnParsableTransactionException();

        if (transaction.getTimestamp().isBefore(currentTime.minusSeconds(60)))
            throw new OlderTransactionException();

        statisticsService.register(transaction);
    }

}
