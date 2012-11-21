package com.tidings.backend.repository;

import com.tidings.backend.domain.Category;
import com.tidings.backend.domain.Document;
import com.tidings.backend.domain.NewsItem;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;

public class TrainingRepository extends Repository {
    TrainingRepository(String collectionName) {
        this.collectionName = collectionName;
    }

    public static TrainingRepository forSentimentAnalysis() {
        return new TrainingRepository("twitter_sentiment_training_data");
    }

    public static NewsTrainingRepository  forNewsClassification() {
        return new NewsTrainingRepository("news_training_data");
    }


    public MongoCollection collection() {
        return jongo.getCollection(collectionName);
    }

    public List<Document> getCategorizedRecords(int numberOfRecords, int offset, long lastRetrievedId) {
        ArrayList<Document> result = new ArrayList<Document>();
        Iterable<Document> iterable = null;
        iterable = collection().find("{'category' : {$ne : null}, 'identifier' : {$gt : #}}", lastRetrievedId)
                .limit(numberOfRecords).sort("{identifier : 1}").as(Document.class);
        for (Document document : iterable) {
            result.add(document);
        }
        return result;
    }

    public Iterable<NewsItem> all() {
        return collection().find().as(NewsItem.class);
    }


    public long getCategorizedRecordsCount() {
        return collection().count(whereCategoryIsNotNull());
    }

    private String whereCategoryIsNotNull() {
        return "{'category' : {$ne : null}}";
    }

    public long count(String category) {
        return collection().count("{'category' : '" + category + "'}");
    }

    public long count(Category category) {
        return count(category.name());
    }

    public long count() {
        return collection().count();
    }
}
