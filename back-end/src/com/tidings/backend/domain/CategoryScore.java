package com.tidings.backend.domain;

public class CategoryScore {
    public static final CategoryScore EMPTY = new CategoryScore();

    private int frequency;
    private float probability;

    public CategoryScore() {
        frequency = 0;
    }

    public CategoryScore(int frequency) {
        this.frequency = frequency;
    }

    public int frequency() {
        return frequency;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public void updateFrequency(int count) {
        frequency += count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryScore that = (CategoryScore) o;

        if (frequency != that.frequency) return false;
        if (Float.compare(that.probability, probability) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = frequency;
        result = 31 * result + (probability != +0.0f ? Float.floatToIntBits(probability) : 0);
        return result;
    }
}
