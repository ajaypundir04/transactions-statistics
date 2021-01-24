package com.n26.service;

import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.n26.utils.TimeUtils.toSeconds;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Value( "${transactionService.stale-after-seconds:60}" )
    private long staleAfterSeconds;

    private final StatisticsService statisticsService;

    public TransactionServiceImpl( StatisticsService statisticsService ) {
        this.statisticsService = statisticsService;
    }

    public void removeTransactions() {
        // delete all tx
        statisticsService.clearStatistics();
    }

    protected void createTransactionAtInstant( Transaction tx, Instant instant )
            throws OlderTransactionException, UnParsableTransactionException {

        LocalDateTime currentTime = LocalDateTime.now();
        if ( tx.getTimestamp().isAfter( currentTime ) )
            throw new UnParsableTransactionException();

        if ( tx.getTimestamp().isBefore( currentTime.minusSeconds( 60 ) ) )
            throw new OlderTransactionException();

        // persist tx
        statisticsService.register( tx );
    }

    public void createTransaction( Transaction tx ) throws OlderTransactionException, UnParsableTransactionException {
        createTransactionAtInstant( tx, Instant.now() );
    }

    public long getStaleAfterSeconds() {
        return staleAfterSeconds;
    }

}
