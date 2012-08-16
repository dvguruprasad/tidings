package com.tidings.backend.pipelines.bulkseeding;


import com.tidings.backend.domain.NewsFeed;
import com.tidings.backend.domain.NewsFeedBuilder;
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
                System.out.println("Pulling feed from: " + url);
                NewsFeed feed = new NewsFeedBuilder().pullContents(url).categorize(category).instance();
                if (feed != null)
                    publish(new Message(feed));
            }
        }
    }

}