package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.Document;
import com.tidings.backend.domain.NewsItem;
import com.tidings.backend.domain.TextSanitizer;
import com.tidings.backend.domain.WordBag;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class TextSanitizationStage extends Stage {
    private final TextSanitizer textSanitizer;

    public TextSanitizationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker) {
        super(inbox, outbox, worker);
        textSanitizer = TextSanitizer.create();
    }

    public void onMessage(Message message) {
        List<NewsItem> items = (List<NewsItem>) message.payload();
        System.out.println("Sanitizing text for " + items.size() + " items");
        for (NewsItem item : items) {
            List<String> sanitized = textSanitizer.sanitize(item.fullText());
            Document document = new Document(WordBag.create(sanitized), item.category());
            publish(new Message(document));
        }
    }
}