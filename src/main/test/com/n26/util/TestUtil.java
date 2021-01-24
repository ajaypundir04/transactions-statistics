package com.n26.util;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

import java.time.LocalDateTime;

public class TestUtil {

    public static Transaction transaction(LocalDateTime time) {

        Transaction transaction = new Transaction();
        transaction.setAmount(4);
        transaction.setTimestamp(time);
        return transaction;
    }

    public static Statistics statistics() {
        return new Statistics(20, 4, 5, 4, 4);
    }

    public static Statistics[] store() {
        Statistics[] store= new Statistics[60];
        store[0] = new Statistics(20, 4, 5, 4, 4);
        store[1] = new Statistics(10, 2, 2.5, 2, 4);
        return store;
    }
}
