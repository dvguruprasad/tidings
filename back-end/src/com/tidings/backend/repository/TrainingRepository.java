package com.tidings.backend.repository;

import com.tidings.backend.domain.TrainingRecord;
import org.jongo.MongoCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TrainingRepository extends Repository {
    
    private MongoCollection collection(){
        return jongo.getCollection("training_data");
    }

    public List<TrainingRecord> getCategorizedRecords(int numberOfRecords) {
        ArrayList<TrainingRecord> result = new ArrayList<TrainingRecord>();
        Iterable<TrainingRecord> trainingRecordsIterable = collection().find(whereCategoryIsNotNull()).limit(numberOfRecords).as(TrainingRecord.class);
        for (TrainingRecord trainingRecord : trainingRecordsIterable) {
            result.add(trainingRecord);
        }
        return result;
    }

    public long getCategorizedRecordsCount() {
        return collection().count(whereCategoryIsNotNull());
    }

    private String whereCategoryIsNotNull() {
        return "{'category' : {$ne : null}}";
    }
}
