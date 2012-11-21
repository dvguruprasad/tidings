package com.tidings.backend.pipelines.classification;

import com.tidings.backend.domain.DocumentProbability;
import com.tidings.backend.pipelines.training.TweetSanitizationStage;
import com.tidings.backend.repository.*;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

public class TweetClassificationPipeline {

    public void run() {
        Pipeline pipeline = new Pipeline();
        final MemoryChannel<Message> tweetReadInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> transformInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> loaderInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> classificationInbox = new MemoryChannel<Message>();
        ThreadFiber tweetReadWorker = new ThreadFiber();
        ThreadFiber transformWorker = new ThreadFiber();
        ThreadFiber classificationWorker = new ThreadFiber();
        ThreadFiber loadingWorker = new ThreadFiber();

        CategoryRepository categoryRepository = CategoryRepository.forSentimentAnalysis();
        DocumentProbability probability = new DocumentProbability(CategoryDistributionRepository.forSentimentAnalysis(), TrainingRepository.forSentimentAnalysis(), categoryRepository);

        TweetReadStage readStage = new TweetReadStage(tweetReadInbox, transformInbox, tweetReadWorker, TweetClassificationDataRepository.forTesting());
        TweetSanitizationStage trasformStage = new TweetSanitizationStage(transformInbox, classificationInbox, transformWorker);
        ClassificationStage classificationStage = new ClassificationStage(classificationInbox, loaderInbox, classificationWorker, categoryRepository, probability);
        TweetsLoadingStage newsItemsLoadingStage = new TweetsLoadingStage(loaderInbox, null, loadingWorker, new TweetsRepository());

        pipeline.addStage(readStage);
        pipeline.addStage(trasformStage);
        pipeline.addStage(classificationStage);
        pipeline.addStage(newsItemsLoadingStage);
        pipeline.start();

        tweetReadInbox.publish(new Message("trigger"));

        try {
            tweetReadWorker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] arguments) {
        new TweetClassificationPipeline().run();
    }
}
