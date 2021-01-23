package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedList;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    LinkedList[] store = new LinkedList[60];

    @PostConstruct
    private void postConstruct() {
        initialize();
    }

    private void initialize() {
        store = new LinkedList[60];
    }
    // Stats will be added to store for considering calculation purpose
    public void register(Transaction tx) {
        addTransactionToStore(tx);
    }

    // Add Algorithm here  for stats calcultaion
    protected Statistics getStatistics(long now) {
        return null;
    }

    public Statistics getStatistics() {
        return getStatistics(Instant.now().toEpochMilli());
    }

    public synchronized void clearStatistics() {
        initialize();
    }

    private void addTransactionToStore(Transaction t) {
        long lastElementEpoc = -1;

        LocalDateTime currentTime = LocalDateTime.now();
        long currentTimeEpoc = toEpocTime(currentTime);
        long startIndexEpoc = toEpocTime(currentTime.minusSeconds(60));

        if (startIndexEpoc > lastElementEpoc && lastElementEpoc > -1) {
            Arrays.fill(store, null);
        } else {
            int shift = getIndexFromEpoc(lastElementEpoc) - getIndexFromEpoc(startIndexEpoc);
            arrayLeftShift(store, shift, getIndexFromEpoc(lastElementEpoc));
        }

        lastElementEpoc = t.getTimestamp();
        int index = getIndexFromEpoc(lastElementEpoc);
        LinkedList<Transaction> trans = store[index];
        if (trans == null) {
            trans = new LinkedList<>();
        }

        trans.add(t);
        store[index] = trans;
    }

    private void arrayLeftShift(LinkedList[] store, int count, int total) {
        for (int i = count; i <= total; i++) {
            store[i - count] = store[i];
        }
    }

    private long toEpocTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    private int getIndexFromEpoc(long time) {
        return (int) (time % 60);
    }
}
