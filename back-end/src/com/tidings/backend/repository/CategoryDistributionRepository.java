package com.tidings.backend.repository;

import com.mongodb.Bytes;
import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.CategoryScore;
import org.jongo.MongoCollection;

import java.util.Collection;

public class CategoryDistributionRepository extends Repository {
    protected MongoCollection collection() {
        MongoCollection collection = jongo.getCollection("category_distributions");
        collection.getDBCollection().addOption(Bytes.QUERYOPTION_NOTIMEOUT);
        return collection;

    }

    public void saveOrUpdate(Collection<CategoryDistribution> distributions) {
        for (CategoryDistribution distribution : distributions) {
            saveOrUpdate(distribution);
        }
    }

    public void saveOrUpdate(Collection<CategoryDistribution> distributions, String category) {
        for (CategoryDistribution distribution : distributions) {
            saveOrUpdate(distribution, category);
        }
    }

    public void saveOrUpdate(CategoryDistribution distribution) {
        collection().update(byWord(distribution.word())).upsert().with(" # ", distribution);
    }

    public void saveOrUpdate(CategoryDistribution distribution, String category) {
        for (CategoryScore categoryScore : distribution.categoryScores().values()) {
            collection().update(byWord(distribution.word())).with("{$inc : {\"categoryScores." + category + ".frequency\" : " + categoryScore.frequency() + "}}");
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

    public Iterable<CategoryDistribution> all(int pageNumber, int pageSize) {
        int offset = pageNumber * pageSize;
        return collection().find().skip(offset).limit(pageSize).as(CategoryDistribution.class);
    }

    public void save(Collection<CategoryDistribution> categoryDistributions) {
        for (CategoryDistribution categoryDistribution : categoryDistributions) {
            collection().save(categoryDistribution);
        }
    }

    private String byWord(String word) {
        return "{\"word\" :\"" + word + "\"}";
    }
}
