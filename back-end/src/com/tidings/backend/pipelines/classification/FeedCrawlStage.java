package com.tidings.backend.pipelines.classification;


import com.tidings.backend.domain.NewsFeed;
import com.tidings.backend.domain.NewsFeedBuilder;
import com.tidings.backend.repository.TrainingRepository;
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
            System.out.println("Pulling feed from: " + url);
            NewsFeed feed = new NewsFeedBuilder(new TrainingRepository()).pullNewContents(url).extractFullText().instance();
            if (feed != null)
                publish(new Message(feed));
        }
        System.out.println("Completed one iteration of pulling all feeds");
    }

}