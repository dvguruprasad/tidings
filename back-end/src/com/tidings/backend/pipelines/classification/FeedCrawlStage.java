package com.tidings.backend.pipelines.classification;


import com.tidings.backend.domain.Link;
import com.tidings.backend.domain.NewsFeed;
import com.tidings.backend.domain.NewsFeedBuilder;
import com.tidings.backend.repository.NewsTrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedCrawlStage extends Stage {

    private final NewsTrainingRepository repository;

    public FeedCrawlStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, NewsTrainingRepository repository) {
        super(inbox, outbox, threadFiber);
        this.repository = repository;
    }

    public void onMessage(Message message) {
        List<Link> feedList = (List<Link>) message.payload();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (final Link link : feedList) {
            executorService.execute(pullFeedAndPublish(link.value()));
        }
    }

    private Runnable pullFeedAndPublish(final String url) {
        return new Runnable() {
            public void run() {
                System.out.println("Pulling feed from: " + url);
                NewsFeed feed = new NewsFeedBuilder(repository).pullNewContents(url).extractFullText().instance();
                if (feed != null)
                    publish(new Message(feed));
            }
        };
    }

}