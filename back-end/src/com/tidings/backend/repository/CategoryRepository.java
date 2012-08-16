package com.tidings.backend.repository;

import com.tidings.backend.domain.Category;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository extends Repository {
    protected MongoCollection collection() {
        return jongo.getCollection("categories");
    }

    public List<Category> all() {
        ArrayList<Category> result = new ArrayList<Category>();
        Iterable<Category> categories = collection().find().as(Category.class);
        for (Category category : categories) {
            result.add(category);
        }
        return result;
    }

    public void deleteAll() {
        collection().drop();
    }

    public void save(Category category) {
        collection().save(category);
    }
}
