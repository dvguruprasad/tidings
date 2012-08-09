package com.tidings.backend.stages;

import com.tidings.backend.domain.NewsFeed;
import com.tidings.backend.domain.NewsItem;
import com.tidings.backend.domain.Link;
import com.tidings.backend.repository.NewsItemsRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class DeduplicationStage extends Stage {
    private List<Link> links;

    public DeduplicationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber,
                              NewsItemsRepository newsItemsRepository) {
        super(inbox, outbox, threadFiber);
        links = newsItemsRepository.uniqueLinks();
    }

    public void onMessage(Message message) {
        List<NewsItem> items = ((NewsFeed) message.payload()).newsItems();
        for (NewsItem item : items) {
            Link newLink = item.link();
            if (!links.contains(newLink)) {
                links.add(newLink);
                publish(new Message(item));
            }
        }
    }
}
