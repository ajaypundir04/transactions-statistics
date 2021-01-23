package com.n26.model;


import java.util.Objects;

public class Statistics {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;
    private long timestamp;

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics that = (Statistics) o;
        return Double.compare(that.sum, sum) == 0 && Double.compare(that.avg, avg) == 0 && Double.compare(that.max, max) == 0 && Double.compare(that.min, min) == 0 && count == that.count && timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, avg, max, min, count, timestamp);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "sum=" + sum +
                ", avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                ", timestamp=" + timestamp +
                '}';
    }
}
