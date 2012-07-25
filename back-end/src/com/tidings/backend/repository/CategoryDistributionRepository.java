package com.tidings.backend.repository;

import com.tidings.backend.domain.CategoryDistribution;
import org.jongo.MongoCollection;

import java.util.Collection;

public class CategoryDistributionRepository extends Repository {
    private MongoCollection wordCollection() {
        return jongo.getCollection("category_distributions");
    }

    public void saveOrUpdate(CategoryDistribution distribution) {
        wordCollection().update("{'word' : '" + distribution.word() + "'}").upsert().with(" # ", distribution);
    }

    public void saveOrUpdate(Collection<CategoryDistribution> distributions) {
        for (CategoryDistribution distribution : distributions) {
            saveOrUpdate(distribution);
        }
    }

    public CategoryDistribution findByWord(String word) {
        return wordCollection().findOne("{'word' : '" + word + "' }").as(CategoryDistribution.class);
    }

    public void deleteAll() {
        wordCollection().drop();
    }

    public long count() {
        return wordCollection().count();
    }
}