package com.tidings.backend.repository;

import com.tidings.backend.domain.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CategoryRepositoryTest {

    public static final String INFRASTRUCTURE = "infrastructure";
    public static final String ANALYSIS = "analysis";

    private CategoryRepository repository;

    @Before
    public void setUp() {
        repository = CategoryRepository.forNewsClassification();
        repository.deleteAll();
    }

    @Test
    public void shouldGetAllCategories() {
        repository.save(new Category(INFRASTRUCTURE));
        repository.save(new Category(ANALYSIS));

        List<Category> categories = repository.all();
        Assert.assertEquals(2, categories.size());
        Assert.assertTrue(categories.contains(new Category(INFRASTRUCTURE)));
        Assert.assertTrue(categories.contains(new Category(ANALYSIS)));
    }

    @Test
    public void shouldIncrementWordFrequencyForACategory(){
        repository.save(new Category(INFRASTRUCTURE));
        repository.addToWordCount(INFRASTRUCTURE, 30);
        Assert.assertEquals(30, repository.find(INFRASTRUCTURE).wordFrequency());

        repository.addToWordCount(INFRASTRUCTURE, 20);
        Assert.assertEquals(50, repository.find(INFRASTRUCTURE).wordFrequency());
    }
}
