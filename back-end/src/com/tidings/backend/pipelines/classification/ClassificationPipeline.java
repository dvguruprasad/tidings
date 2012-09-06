package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.DocumentProbability;
import com.tidings.backend.domain.Link;
import com.tidings.backend.domain.NewsTransformer;
import com.tidings.backend.repository.*;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.ArrayList;
import java.util.List;

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


        crawlInbox.publish(new Message(newsFeeds()));
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

    private List<String> newsFeeds() {
        List<Link> all = new NewsFeedsRepository().all();
        ArrayList<String> links = new ArrayList<String>();
        for (Link link : all) {
            links.add(link.value());
        }
        return links;
    }

    public static void main(String[] arguments) {
        new ClassificationPipeline().run();
    }
}
