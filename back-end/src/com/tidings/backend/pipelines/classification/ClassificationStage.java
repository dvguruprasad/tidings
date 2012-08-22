package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.Category;
import com.tidings.backend.domain.NewsItem;
import com.tidings.backend.domain.Probability;
import com.tidings.backend.domain.WordBag;
import com.tidings.backend.repository.CategoryRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.List;

public class ClassificationStage extends Stage {

    private CategoryRepository categoryRepository;
    private Probability probability;

    public ClassificationStage(MemoryChannel<Message> inbox, MemoryChannel<Message> outbox, ThreadFiber worker, CategoryRepository categoryRepository, Probability probability) {
        super(inbox, outbox, worker);
        this.categoryRepository = categoryRepository;
        this.probability = probability;
    }

    public void onMessage(Message message) {
        NewsItem newsItem = (NewsItem) message.payload();
        WordBag wordBag = newsItem.wordBag();
        List<Category> categories = categoryRepository.all();
        Category finalCategory = null;
        float highestProbability = (float) 0.0;

        for (Category category : categories) {
            float temp = probability.ofDocumentBelongingToCategoryGivenWords(category, wordBag);
            if (temp > highestProbability) {
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
