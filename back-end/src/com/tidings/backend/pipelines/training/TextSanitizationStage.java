package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.Document;
import com.tidings.backend.domain.StopWords;
import com.tidings.backend.domain.TextSanitizer;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class TextSanitizationStage extends Stage {
    private final TextSanitizer textSanitizer;

    public TextSanitizationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker, StopWords stopWords) {
        super(inbox, outbox, worker);
        textSanitizer = TextSanitizer.create(stopWords);
    }

    public void onMessage(Message message) {
        List<Document> items = (List<Document>) message.payload();
        for (Document document : items) {
            document.sanitize(textSanitizer);
            publish(new Message(document));
        }
    }
}