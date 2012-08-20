package com.tidings.backend.pipelines.bulkseeding;


import com.tidings.backend.domain.NewsFeed;
import com.tidings.backend.domain.NewsFeedBuilder;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.Map;

public class TrainingDataCrawlStage extends Stage {

    public TrainingDataCrawlStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
    }

    public void onMessage(Message message) {
        Map<String, String[]> feedList = (Map<String, String[]>) message.payload();
        for (String category : feedList.keySet()) {
            String[] feeds = feedList.get(category);
            for (String url : feeds) {
                System.out.println("Pulling feeds from: " + url);
                NewsFeed feed = new NewsFeedBuilder(new TrainingRepository()).pullNewContents(url).categorize(category).extractFullText().instance();
                if (feed != null)
                    publish(new Message(feed));
            }
        }
    }

}