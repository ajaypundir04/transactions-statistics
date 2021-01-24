package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import static com.n26.utils.TimeUtils.toSeconds;

@Service
public class TransactionManager {

    private static final int SIZE = 60;
    private final Object MUTEX = new Object();
    private final Statistics[] store = new Statistics[SIZE];
    private final AtomicLong firstIndexSeconds = new AtomicLong(-1);
    private final AtomicLong lastIndexSeconds = new AtomicLong(-1);


    public void addTransaction(Transaction transaction) {
        long transactionSeconds = toSeconds(transaction.getTimestamp());
        synchronized (MUTEX) {
            shuffleArray();
            int index = (int) (transactionSeconds - firstIndexSeconds.get());
            if (index == 60) {
                index = index - 1;
            }
            Statistics trans = store[index];
            double amt = transaction.getAmount();
            if (trans == null) {
                trans = new Statistics(amt, 0, amt, amt, 1);
            } else {
                add(amt, trans);
            }
            store[index] = trans;
        }
    }

    public Statistics[] getStore() {
        DateTime dt = new DateTime();
        if (dt.isWindowExpired(lastIndexSeconds.get())) {
            clear();
            return store;
        } else {
            synchronized (MUTEX) {
                Statistics[] tmpArray = new Statistics[SIZE];
                System.arraycopy(
                        store, calculateIndex(dt.getStartTime()) + 1,
                        tmpArray, 0,
                        (int) ((lastIndexSeconds.get() - dt.getStartTime()) - 1)
                );
                return tmpArray;
            }
        }
    }

    private int calculateIndex(long starTime) {
        return (int) (starTime - firstIndexSeconds.get());
    }

    private void add(double amount, Statistics stats) {
        stats.setCount(stats.getCount() + 1);
        stats.setSum(stats.getSum() + amount);
        stats.setMax(Double.max(stats.getMax(), amount));
        stats.setMin(Double.min(stats.getMin(), amount));
    }

    public void clear() {
        synchronized (MUTEX) {
            Arrays.fill(store, null);
        }
    }

    private void shuffleArray() {
        DateTime dt = new DateTime();
        if (firstIndexSeconds.get() > -1 && lastIndexSeconds.get() > -1) {
            if (dt.isWindowExpired(lastIndexSeconds.get())) {
                clear();
            } else if (dt.getStartTime() > firstIndexSeconds.get()) {
                int shuffleCount = (int) (dt.getStartTime() - firstIndexSeconds.get());
                arrayLeftShift(shuffleCount);
            }
        }
        firstIndexSeconds.getAndSet(dt.startTime);
        lastIndexSeconds.getAndSet(dt.getEndTime());
    }

    private void arrayLeftShift(int shuffleCount) {
        int i = shuffleCount;
        while (i < SIZE) {
            store[i - shuffleCount] = store[i];
            i++;
        }

        for (int j = (i - shuffleCount); j < SIZE; j++) {
            store[j] = null;
        }
    }

    static class DateTime {
        private final long startTime;
        private final long endTime;

        DateTime() {
            LocalDateTime dt = LocalDateTime.now();
            endTime = toSeconds(dt);
            startTime = toSeconds(dt.minusSeconds(SIZE));
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public boolean isWindowExpired(long storedEndTime) {
            return getStartTime() >= storedEndTime;
        }
    }
}
