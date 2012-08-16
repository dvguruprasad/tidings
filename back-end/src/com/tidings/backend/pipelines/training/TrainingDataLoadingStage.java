package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.NewsItem;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class TrainingDataLoadingStage extends Stage {
    private TrainingRepository repository;

    public TrainingDataLoadingStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker, TrainingRepository repository) {
        super(inbox, outbox, worker);
        this.repository = repository;
    }

    public void onMessage(Message message) {
        NewsItem newsItem = (NewsItem) message.payload();
        repository.save(newsItem);
    }
}
