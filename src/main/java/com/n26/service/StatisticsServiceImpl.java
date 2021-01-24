package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TransactionManager transactionManager;

    @Autowired
    public StatisticsServiceImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    // Stats will be added to store for considering calculation purpose
    public void register(Transaction tx) {
        addTransactionToStore(tx);
    }

    // Add Algorithm here  for stats calculation
    protected Statistics getStatistics(long now) {
        return null;
    }

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
