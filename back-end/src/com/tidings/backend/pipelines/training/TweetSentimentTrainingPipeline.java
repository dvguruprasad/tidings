package com.tidings.backend.pipelines.training;

import com.tidings.backend.domain.StopWords;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.Date;

public class TweetSentimentTrainingPipeline {
    public void start() {
        Pipeline pipeline = new Pipeline();

        MemoryChannel<Message> trainingLoadInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> transformationInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> frequencyInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> probabilityInbox = new MemoryChannel<Message>();

        ThreadFiber dataExtractionWorker = new ThreadFiber();
        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber frequencyWorker = new ThreadFiber();
        ThreadFiber probablityWorker = new ThreadFiber();

        TrainingRepository trainingRepository = TrainingRepository.forSentimentAnalysis();
        CategoryRepository categoryRepository = CategoryRepository.forSentimentAnalysis();
        CategoryDistributionRepository categoryDistributionRepository = CategoryDistributionRepository.forSentimentAnalysis();

        TrainingDataExtractionStage extractionStage = new TrainingDataExtractionStage(trainingLoadInbox, transformationInbox, dataExtractionWorker, trainingRepository);
        TextSanitizationStage transformationStage = new TextSanitizationStage(transformationInbox, frequencyInbox, crawlWorker, new StopWords("data/stopwords_sentiment.txt"));
        FrequencyComputationStage frequencyCompuationStage = new FrequencyComputationStage(frequencyInbox, probabilityInbox, frequencyWorker, categoryRepository, trainingRepository, categoryDistributionRepository);
        ProbabilityCompuationStage probabilityCompuationStage = new ProbabilityCompuationStage(probabilityInbox, null, probablityWorker, categoryDistributionRepository, categoryRepository);

        pipeline.addStage(extractionStage);
        pipeline.addStage(transformationStage);
        pipeline.addStage(frequencyCompuationStage);
        pipeline.addStage(probabilityCompuationStage);
        System.out.println("Started training at " + new Date());
        pipeline.start();

        trainingLoadInbox.publish(new Message("something"));

        try {
            dataExtractionWorker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new TweetSentimentTrainingPipeline().start();
    }
}
