package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.NewsItem;
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
        int pageSize = 10;
        int pageNumber = ((int) Math.ceil((double) total / pageSize));
        for (int currentPage = 0; currentPage < pageNumber; currentPage++) {
            int offset = currentPage * pageSize;
            List<NewsItem> records = trainingRepository.getCategorizedRecords(pageSize, offset);
            publish(new Message(records));
        }
    }

}