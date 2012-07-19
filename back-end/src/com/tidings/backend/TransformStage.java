package com.tidings.backend;

import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class TransformStage extends Stage {
    private NewsTransformer transformer;

    public TransformStage(Channel<Message> inbox, Channel<Message> outbox, Fiber fiber, NewsTransformer newsTransformer) {
        super(inbox, outbox, fiber);
        this.transformer = newsTransformer;
    }

    public void onMessage(Message message) {
        NewsFeed newsFeed = (NewsFeed) message.payload();
        System.out.println("Transforming " + newsFeed.entryCount() + " newsItems from " + newsFeed.title());
        NewsFeed transformed = transformer.transform(newsFeed);
        Message outbound = new Message(transformed);
        publish(outbound);
    }
}