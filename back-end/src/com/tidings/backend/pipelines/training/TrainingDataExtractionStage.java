package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.Document;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class TrainingDataExtractionStage extends Stage {
    private TrainingRepository trainingRepository;

    public TrainingDataExtractionStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, TrainingRepository trainingRepository) {
        super(inbox, outbox, threadFiber);
        this.trainingRepository = trainingRepository;
    }

    public void onMessage(Message message) {
        long total = trainingRepository.getCategorizedRecordsCount();
        int pageSize = 10000;
        int pageNumber = ((int) Math.ceil((double) total / pageSize));

        List<Document> records = trainingRepository.getCategorizedRecords(pageSize, 0, 0);
        publish(new Message(records));
        long lastRetrievedId = records.get(records.size() - 1).id();
        for (int currentPage = 1; currentPage < pageNumber; currentPage++) {
            int offset = currentPage * pageSize;
            records = trainingRepository.getCategorizedRecords(pageSize, offset, lastRetrievedId);
            lastRetrievedId = records.get(records.size() - 1).id();
            publish(new Message(records));
        }
    }
}