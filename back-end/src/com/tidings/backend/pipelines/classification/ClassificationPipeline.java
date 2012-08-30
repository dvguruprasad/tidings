package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.DocumentProbability;
import com.tidings.backend.domain.NewsTransformer;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.NewsItemsRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.List;

public class ClassificationPipeline {
    private List<String> feeds;

    public ClassificationPipeline(List<String> feeds) {
        this.feeds = feeds;
    }

    public void start() {
        Pipeline pipeline = new Pipeline();
        final MemoryChannel<Message> crawlInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> transformInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> dedupInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> loaderInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> classificationInbox = new MemoryChannel<Message>();
        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber transformWorker = new ThreadFiber();
        ThreadFiber dedupWorker = new ThreadFiber();
        ThreadFiber classificationWorker = new ThreadFiber();
        ThreadFiber loadingWorker = new ThreadFiber();

        DocumentProbability probability = new DocumentProbability(new CategoryDistributionRepository(), new TrainingRepository());

        FeedCrawlStage crawlStage = new FeedCrawlStage(crawlInbox, transformInbox, crawlWorker);
        TransformStage trasformStage = new TransformStage(transformInbox, dedupInbox, transformWorker, new NewsTransformer());
        DeduplicationStage deduplicationStage = new DeduplicationStage(dedupInbox, classificationInbox, dedupWorker, new NewsItemsRepository());
        ClassificationStage classificationStage = new ClassificationStage(classificationInbox, loaderInbox, classificationWorker, new CategoryRepository(), probability);
        NewsItemsLoadingStage newsItemsLoadingStage = new NewsItemsLoadingStage(loaderInbox, null, loadingWorker, new NewsItemsRepository());

        pipeline.addStage(crawlStage);
        pipeline.addStage(trasformStage);
        pipeline.addStage(deduplicationStage);
        pipeline.addStage(classificationStage);
        pipeline.addStage(newsItemsLoadingStage);
        pipeline.start();

        crawlInbox.publish(new Message(feeds));
        try {
            crawlWorker.join();
            transformWorker.join();
            classificationWorker.join();
            loadingWorker.join();

            crawlWorker.dispose();
            transformWorker.dispose();
            classificationWorker.dispose();
            loadingWorker.dispose();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
