package com.tidings.backend;

import com.tidings.backend.pipelines.training.FrequencyComputationStage;
import com.tidings.backend.pipelines.training.ProbabilityCompuationStage;
import com.tidings.backend.pipelines.training.TextSanitizationStage;
import com.tidings.backend.pipelines.training.TrainingDataExtractionStage;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;
import org.junit.Test;

public class TrainingPipelineTest {
    @Test
    public void shouldExecuteTheTrainingPipeline() {
        TrainingRepository trainingRepository = new TrainingRepository();

        Pipeline pipeline = new Pipeline();

        MemoryChannel<Message> trainingLoadInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> transformationInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> frequencyInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> probabilityInbox = new MemoryChannel<Message>();

        ThreadFiber dataExtractionWorker = new ThreadFiber();
        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber frequencyWorker = new ThreadFiber();
        ThreadFiber probablityWorker = new ThreadFiber();

        TrainingDataExtractionStage extractionStage = new TrainingDataExtractionStage(trainingLoadInbox, transformationInbox, dataExtractionWorker, trainingRepository);
        TextSanitizationStage transformationStage = new TextSanitizationStage(transformationInbox, frequencyInbox, crawlWorker);
        FrequencyComputationStage frequencyCompuationStage = new FrequencyComputationStage(frequencyInbox, probabilityInbox, frequencyWorker, new CategoryRepository(), new TrainingRepository());
        ProbabilityCompuationStage probabilityCompuationStage = new ProbabilityCompuationStage(probabilityInbox, null, probablityWorker, new CategoryDistributionRepository(), new CategoryRepository());

        pipeline.addStage(extractionStage);
        pipeline.addStage(transformationStage);
        pipeline.addStage(frequencyCompuationStage);
        pipeline.addStage(probabilityCompuationStage);

        pipeline.start();
        trainingLoadInbox.publish(new Message("something"));

        try {
            dataExtractionWorker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}