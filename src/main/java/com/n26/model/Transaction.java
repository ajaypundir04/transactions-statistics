package com.n26.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    private double amount;
    @JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" )
    private LocalDateTime timestamp;

    public double getAmount() {
        return amount;
    }

    public void setAmount( double amount ) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp( LocalDateTime timestamp ) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Transaction that = ( Transaction ) o;
        return Double.compare( that.amount, amount ) == 0 && timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash( amount, timestamp );
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
