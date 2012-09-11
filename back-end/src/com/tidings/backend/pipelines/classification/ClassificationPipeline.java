package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.DocumentProbability;
import com.tidings.backend.domain.Link;
import com.tidings.backend.domain.NewsTransformer;
import com.tidings.backend.repository.*;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClassificationPipeline {

    public void run() {
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

//        crawlInbox.publish(new Message(newsFeeds()));
        new ClassificationScheduler(crawlInbox, new NewsFeedsRepository()).schedule();

        try {
            crawlWorker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    class ClassificationScheduler {
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        private MemoryChannel<Message> entryPointInbox;
        private NewsFeedsRepository newsFeedsRepository;

        public ClassificationScheduler(MemoryChannel<Message> entryPointInbox, NewsFeedsRepository newsFeedsRepository) {
            this.entryPointInbox = entryPointInbox;
            this.newsFeedsRepository = newsFeedsRepository;
        }

        public void schedule() {
            long size = newsFeedsRepository.count();
            int numberOfPages = 10;
            int feedsPerPage = (int) Math.ceil(size / (double) numberOfPages);
            int initialDelayInSeconds = 0;
            for (int currentPage = 0; currentPage < numberOfPages; currentPage++, initialDelayInSeconds += (3 * 60)) {
                final List<Link> feeds = newsFeedsRepository.all(currentPage, feedsPerPage);
                final Runnable publishMessage = new Runnable() {
                    public void run() {
                        entryPointInbox.publish(new Message(feeds));
                    }
                };
                scheduler.scheduleWithFixedDelay(publishMessage, initialDelayInSeconds, 5 * 60, TimeUnit.SECONDS);
            }
        }
    }

    public static void main(String[] arguments) {
        new ClassificationPipeline().run();
    }
}
