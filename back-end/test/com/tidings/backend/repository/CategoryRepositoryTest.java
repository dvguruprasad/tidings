package com.tidings.backend.repository;

import com.tidings.backend.domain.Category;
import com.tidings.backend.domain.NewsItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class CategoryRepositoryTest {

    private CategoryRepository repository;

    @Before
    public void setUp() {
        repository = new CategoryRepository();
        repository.deleteAll();
    }

    @Test
    public void shouldGetAllCategories() {
        repository.save(new Category("infrastructure"));
        repository.save(new Category("analysis"));

        List<Category> categories = repository.all();
        Assert.assertEquals(2, categories.size());
        Assert.assertTrue(categories.contains(new Category("infrastructure")));
        Assert.assertTrue(categories.contains(new Category("analysis")));
    }

    @Test
    public void foo(){
        new TrainingRepository().save(new NewsItem("title", "foo", "ds", new Date()));
    }
}
