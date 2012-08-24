package com.tidings.backend.domain;

public class CategoryScore {
    public static final CategoryScore EMPTY = new CategoryScore();

    private int frequency;
    private double probability;

    public CategoryScore() {
        frequency = 0;
    }

    public CategoryScore(int frequency) {
        this.frequency = frequency;
    }

    public int frequency() {
        return frequency;
    }

    public double probability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public void incrementFrequency(int count) {
        frequency += count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryScore that = (CategoryScore) o;

        if (frequency != that.frequency) return false;
        if (Double.compare(that.probability, probability) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = frequency;
        temp = probability != +0.0d ? Double.doubleToLongBits(probability) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
