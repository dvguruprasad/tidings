package com.tidings.backend.repository;

import com.mongodb.Bytes;
import com.tidings.backend.domain.CategoryDistribution;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CategoryDistributionRepository extends Repository {
    protected MongoCollection collection() {
        MongoCollection collection = jongo.getCollection("category_distributions");
        collection.getDBCollection().addOption(Bytes.QUERYOPTION_NOTIMEOUT);
        return collection;

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

    public long count(String categoryName) {
        return collection().count("{\"categoryScores." + categoryName + "\" : { $exists : true }}");
    }

    public Iterable<CategoryDistribution> all() {
        return collection().find().as(CategoryDistribution.class);
    }

    public Iterable<CategoryDistribution> all(int offset) {
        return collection().find().skip(offset).as(CategoryDistribution.class);
    }

    private String byWord(String word) {
        return "{\"word\" :\"" + word + "\"}";
    }
}
