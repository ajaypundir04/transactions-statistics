package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Ajay Singh Pundir
 * It will be used to calculate the stats within last 60 seconds.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TransactionProcessor transactionProcessor;

    @Autowired
    public StatisticsServiceImpl(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    /**
     * Transactions will be in-memory persist.
     *
     * @param transaction @{@link Transaction} get from the consumer.
     */
    public void register(Transaction transaction) {
        logger.info("Adding transaction to in-memory store");
        addTransactionToStore(transaction);
    }

    /**
     * Statistics will be calculated within last 60 seconds.
     *
     * @return @{@link Statistics} required calculations.
     */
    public Statistics getStatistics() {

        Statistics[] store = transactionProcessor.getStore();

        double max = 0;
        double min = 0;
        double sum = 0.0;
        long count = 0;
        double avg = 0;

        boolean started = false;

        for (int i = 0; i < 60; i++) {
            if (store[i] != null) {

                count += store[i].getCount();

                sum += store[i].getSum();

                if (started) {
                    if (max < store[i].getMax()) {
                        max = store[i].getMax();
                    }

                    if (min > store[i].getMin()) {
                        min = store[i].getMin();
                    }
                } else {
                    started = true;
                    max = store[i].getMax();
                    min = store[i].getMin();
                }
            }
        }
        if (count > 0) {
            avg = sum / count;
        }
        Statistics statistics = new Statistics(sum, avg, max, min, count);
        logger.info("Statistics {} Now {}", statistics.toString(), Instant.now());
        return statistics;
    }

    /**
     *  Empty statistics if all the transactions are deleted
     */
    public void clearStatistics() {
        transactionProcessor.clear();
    }

    private void addTransactionToStore(Transaction t) {
        transactionProcessor.addTransaction(t);
    }
}
