package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.Tweet;
import com.tidings.backend.repository.TweetClassificationDataRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

public class TweetReadStage extends Stage{

    private final TweetClassificationDataRepository repository;

    public TweetReadStage(MemoryChannel<Message> inbox, MemoryChannel<Message> outbox, ThreadFiber fiber, TweetClassificationDataRepository repository) {
        super(inbox, outbox, fiber);
        this.repository = repository;
    }

    public void onMessage(Message message) {
        Iterable<Tweet> allTweets = repository.all();
        for (Tweet tweet : allTweets) {
            publish(new Message(tweet));
        }
    }
}
