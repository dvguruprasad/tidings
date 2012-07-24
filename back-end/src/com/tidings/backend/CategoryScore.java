package com.tidings.backend;

public class CategoryScore {
    private String category;
    private int count;

    public CategoryScore() {
    }

    public CategoryScore(String category, int count) {
        this.category = category;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryScore that = (CategoryScore) o;

        if (category != null ? !category.equals(that.category) : that.category != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return category != null ? category.hashCode() : 0;
    }
}
