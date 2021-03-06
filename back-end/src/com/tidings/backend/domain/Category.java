package com.tidings.backend.domain;

public class Category {
    private String name;
    private long wordFrequency;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public long wordFrequency() {
        return wordFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (name != null ? !name.equals(category.name) : category.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
