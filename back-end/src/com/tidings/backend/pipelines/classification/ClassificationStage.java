package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.Category;
import com.tidings.backend.domain.DocumentProbability;
import com.tidings.backend.domain.NewsItem;
import com.tidings.backend.domain.WordBag;
import com.tidings.backend.repository.CategoryRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.List;

public class ClassificationStage extends Stage {

    private CategoryRepository categoryRepository;
    private DocumentProbability probability;

    public ClassificationStage(MemoryChannel<Message> inbox, MemoryChannel<Message> outbox, ThreadFiber worker, CategoryRepository categoryRepository, DocumentProbability documentProbability) {
        super(inbox, outbox, worker);
        this.categoryRepository = categoryRepository;
        this.probability = documentProbability;
    }

    public void onMessage(Message message) {
        NewsItem newsItem = (NewsItem) message.payload();
        List<Category> categories = categoryRepository.all();
        WordBag wordBag = newsItem.wordBag();

        Category finalCategory = null;
        double highestProbability = Double.MAX_VALUE;

        for (Category category : categories) {
            double temp = probability.compute(category, wordBag);
            if (temp < highestProbability) {
                highestProbability = temp;
                finalCategory = category;
            }
        }

        if (finalCategory != null) {
            newsItem.setCategory(finalCategory.name());
        }
        publish(new Message(newsItem));
    }
}
