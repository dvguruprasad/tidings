package com.tidings.backend.pipelines.bulkseeding;

import com.tidings.backend.pipelines.training.TrainingDataLoadingStage;
import com.tidings.backend.repository.NewsTrainingRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.Map;

public class BulkSeedingPipeline {
    private Map<String, String[]> feeds;

    public BulkSeedingPipeline(Map<String, String[]> feeds) {
        this.feeds = feeds;
    }

    public void run() {
        Pipeline pipeline = new Pipeline();
        final MemoryChannel<Message> crawlInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> dedupInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> loaderInbox = new MemoryChannel<Message>();

        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber transformWorker = new ThreadFiber();
        ThreadFiber dedupWorker = new ThreadFiber();
        ThreadFiber loadingWorker = new ThreadFiber();

        NewsTrainingRepository trainingRepository = TrainingRepository.forNewsClassification();

        TrainingDataCrawlStage crawlStage = new TrainingDataCrawlStage(crawlInbox, dedupInbox, crawlWorker, trainingRepository);
        TrainingDataDeduplicationStage deduplicationStage = new TrainingDataDeduplicationStage(dedupInbox, loaderInbox, dedupWorker, trainingRepository);
        TrainingDataLoadingStage trainingDataLoadingStage = new TrainingDataLoadingStage(loaderInbox, null, loadingWorker, trainingRepository);

        pipeline.addStage(crawlStage);
        pipeline.addStage(deduplicationStage);
        pipeline.addStage(trainingDataLoadingStage);
        pipeline.start();
        crawlInbox.publish(new Message(feeds));
        try {
            crawlWorker.join();
            transformWorker.join();
            loadingWorker.join();

            crawlWorker.dispose();
            transformWorker.dispose();
            loadingWorker.dispose();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
