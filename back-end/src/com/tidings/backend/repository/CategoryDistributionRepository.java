package com.tidings.backend.repository;

import com.tidings.backend.domain.CategoryDistribution;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CategoryDistributionRepository extends Repository {
    protected MongoCollection collection() {
        return jongo.getCollection("category_distributions");
    }

    public void saveOrUpdate(CategoryDistribution distribution) {
        collection().update(byWord(distribution.word())).upsert().with(" # ", distribution);
    }

    public void saveOrUpdate(Collection<CategoryDistribution> distributions) {
        for (CategoryDistribution distribution : distributions) {
            saveOrUpdate(distribution);
        }
    }

    public CategoryDistribution findByWord(String word) {
        return collection().findOne(byWord(word)).as(CategoryDistribution.class);
    }

    public void deleteAll() {
        collection().drop();
    }

    public long count() {
        return collection().count();
    }

    public Iterable<CategoryDistribution> all() {
        return collection().find().as(CategoryDistribution.class);
    }

    public Iterable<CategoryDistribution> findAll(Set<String> words, String category) {
        ArrayList<CategoryDistribution> result = new ArrayList<CategoryDistribution>();
        for (String word : words) {
            CategoryDistribution distribution = findByWord(word);
            if (null == distribution) {
                distribution = new CategoryDistribution(word);
                distribution.addOrUpdateCategory(category);
            }
            result.add(distribution);
        }
        return result;
    }

    private String byWord(String word) {
        return "{\"word\" :\"" + word + "\"}";
    }
}
