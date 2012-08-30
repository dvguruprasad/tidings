package com.tidings.backend.pipelines.training;

import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.Date;

public class TrainingPipeline {
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

        CategoryRepository categoryRepository = new CategoryRepository();
        CategoryDistributionRepository categoryDistributionRepository = new CategoryDistributionRepository();
        TrainingRepository trainingRepository = new TrainingRepository();

        TrainingDataExtractionStage extractionStage = new TrainingDataExtractionStage(trainingLoadInbox, transformationInbox, dataExtractionWorker, trainingRepository);
        TextSanitizationStage transformationStage = new TextSanitizationStage(transformationInbox, frequencyInbox, crawlWorker);
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
    
    public static void main(String[] args){
        new TrainingPipeline().start();
    }
}
