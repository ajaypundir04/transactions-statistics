package com.n26.util;

import com.n26.model.Transaction;

import java.time.LocalDateTime;

public class TestUtil {

    public static Transaction transaction(LocalDateTime time) {

        Transaction transaction = new Transaction();
        transaction.setAmount(4);
        transaction.setTimestamp(time);
        return transaction;
    }
}
