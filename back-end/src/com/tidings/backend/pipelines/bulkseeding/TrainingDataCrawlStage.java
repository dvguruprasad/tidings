package com.tidings.backend.pipelines.bulkseeding;

import com.tidings.backend.domain.NewsFeed;
import com.tidings.backend.domain.NewsFeedBuilder;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainingDataCrawlStage extends Stage {

    public TrainingDataCrawlStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
    }

    public void onMessage(Message message) {
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        Map<String, String[]> feedList = (Map<String, String[]>) message.payload();
        for (String category : feedList.keySet()) {
            String[] feeds = feedList.get(category);
            for (String feedUrl : feeds) {
                executorService.execute(feedCrawlRunnable(category, feedUrl));
            }
        }
    }

    private Runnable feedCrawlRunnable(final String category, final String url) {
        return new Runnable() {
            public void run() {
                System.out.println("Pulling feeds from: " + url);
                NewsFeed feed = new NewsFeedBuilder(new TrainingRepository()).pullNewContents(url).categorize(category).extractFullText().instance();
                if (feed != null)
                    publish(new Message(feed));
            }
        };
    }
}