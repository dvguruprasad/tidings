package com.tidings.backend.pipelines.bulkseeding;

import com.tidings.backend.domain.Link;
import com.tidings.backend.domain.NewsFeed;
import com.tidings.backend.domain.NewsItem;
import com.tidings.backend.repository.NewsTrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class TrainingDataDeduplicationStage extends Stage {
    private List<Link> links;

    public TrainingDataDeduplicationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber,
                                          NewsTrainingRepository newsItemsRepository) {
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
