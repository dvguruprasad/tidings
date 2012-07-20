package com.tidings.backend.stages;


import com.tidings.backend.domain.NewsFeed;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class FeedCrawlStage extends Stage {

    public FeedCrawlStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        super(inbox, outbox, threadFiber);
    }

    public void onMessage(Message message) {
        List<String> feedList = (List<String>) message.payload();
        for (String url : feedList) {
            System.out.println("pulling feed from :" + url);
            NewsFeed feed = NewsFeed.pull(url);
            if (feed != null)
                publish(new Message(feed));
        }
        System.out.println("Completed one iteration of pulling all feeds");
    }

}