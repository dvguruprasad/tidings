package com.tidings.backend.repository;

import com.mongodb.Bytes;
import com.tidings.backend.domain.CategoryDistribution;
import com.tidings.backend.domain.CategoryScore;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryDistributionRepository extends Repository {
    private CategoryDistributionRepository(String collectionName) {
        this.collectionName = collectionName;
    }

    public static CategoryDistributionRepository forSentimentAnalysis() {
        return new CategoryDistributionRepository("tweets_category_distributions");
    }


    public static CategoryDistributionRepository forNewsClassification() {
        return new CategoryDistributionRepository("news_category_distributions");
    }

    protected MongoCollection collection() {
        MongoCollection collection = jongo.getCollection(collectionName);
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
        String word = distribution.word().replace("\\", "\\\\"); // so that json parse happens on \\ and not \
        collection().update(byWord(word)).upsert().with(" # ", distribution);
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

    public List<CategoryDistribution> all(int numberOfRecords, int offset, long lastRetrievedId) {
        ArrayList<CategoryDistribution> result = new ArrayList<CategoryDistribution>();
        Iterable<CategoryDistribution> iterable = null;
        iterable = collection().find("{'id' : {$gt : #}}", lastRetrievedId).limit(numberOfRecords).sort("{id: 1}").as(CategoryDistribution.class);
        for (CategoryDistribution distribution : iterable) {
            result.add(distribution);
        }
        return result;
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
