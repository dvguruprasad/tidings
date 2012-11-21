package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.Tweet;
import com.tidings.backend.repository.TweetsRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

public class TweetsLoadingStage extends Stage {
    private TweetsRepository repository;

    public TweetsLoadingStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker, TweetsRepository repository) {
        super(inbox, outbox, worker);
        this.repository = repository;
    }

    public void onMessage(Message message) {
        Tweet tweet = (Tweet) message.payload();
        repository.save(tweet);
    }
}
