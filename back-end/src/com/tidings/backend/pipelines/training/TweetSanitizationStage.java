package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.Emoticons;
import com.tidings.backend.domain.StopWords;
import com.tidings.backend.domain.Tweet;
import com.tidings.backend.domain.TweetSanitizer;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class TweetSanitizationStage extends Stage {

    private final TweetSanitizer tweetSanitizer;

    public TweetSanitizationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker) {
        super(inbox, outbox, worker);
        tweetSanitizer = TweetSanitizer.create(new StopWords("data/stopwords_sentiment.txt"), Emoticons.create("data/emoticons.csv"));
    }

    public void onMessage(Message message) {
        Tweet tweet = (Tweet) message.payload();
        tweet.sanitize(tweetSanitizer);
        publish(new Message(tweet));
    }
}