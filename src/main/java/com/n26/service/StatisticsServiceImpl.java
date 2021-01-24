package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ajay Singh Pundir
 * It will be used to calculate the stats within last 60 seconds.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TransactionManager transactionManager;

    @Autowired
    public StatisticsServiceImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Transactions will be in-memory persist.
     *
     * @param transaction @{@link Transaction} get from the consumer.
     */
    public void register(Transaction transaction) {
        addTransactionToStore(transaction);
    }

    /**
     * Statistics will be calculated within last 60 seconds.
     *
     * @return @{@link Statistics} required calculations.
     */
    public Statistics getStatistics() {

        Statistics[] store = transactionManager.getStore();

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
        return new Statistics(sum, avg, max, min, count);
    }

    public void clearStatistics() {
        transactionManager.clear();
    }

    private void addTransactionToStore(Transaction t) {
        transactionManager.addTransaction(t);
    }
}
