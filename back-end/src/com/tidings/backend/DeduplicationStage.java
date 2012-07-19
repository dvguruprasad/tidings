package com.tidings.backend;

import com.tidings.backend.repository.Link;
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
        initialize(newsItemsRepository);
    }

    private void initialize(NewsItemsRepository newsItemsRepository) {
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
