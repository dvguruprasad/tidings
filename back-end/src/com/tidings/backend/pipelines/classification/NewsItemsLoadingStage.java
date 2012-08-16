package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.NewsItem;
import com.tidings.backend.repository.NewsItemsRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class NewsItemsLoadingStage extends Stage {
        private NewsItemsRepository repository;

        public NewsItemsLoadingStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker, NewsItemsRepository repository) {
            super(inbox, outbox, worker);
            this.repository = repository;
        }

        public void onMessage(Message message) {
            NewsItem newsItem = (NewsItem) message.payload();
            repository.save(newsItem);
        }
    }
