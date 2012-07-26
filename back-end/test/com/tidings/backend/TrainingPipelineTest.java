package com.tidings.backend;

import com.tidings.backend.repository.TrainingRepository;
import com.tidings.backend.stages.FrequencyComputationStage;
import com.tidings.backend.stages.TrainingDataExtractionStage;
import com.tidings.backend.stages.TrainingRecordTransformationStage;
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
//        MemoryChannel<Message> probabilityInbox = new MemoryChannel<Message>();

        ThreadFiber dataExtractionWorker = new ThreadFiber();
        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber frequencyWorker = new ThreadFiber();
//        ThreadFiber probablityWorker = new ThreadFiber();

        TrainingDataExtractionStage extractionStage = new TrainingDataExtractionStage(trainingLoadInbox, transformationInbox, dataExtractionWorker, trainingRepository);
        TrainingRecordTransformationStage transformationStage = new TrainingRecordTransformationStage(transformationInbox, frequencyInbox, crawlWorker);
        FrequencyComputationStage frequencyCompuationStage = new FrequencyComputationStage(frequencyInbox, null, frequencyWorker);
//        ProbabilityCompuationStage probabilityCompuationStage = new ProbabilityCompuationStage(probabilityInbox, null, probablityWorker);

        pipeline.addStage(extractionStage);
        pipeline.addStage(transformationStage);
        pipeline.addStage(frequencyCompuationStage);
//        pipeline.addStage(probabilityCompuationStage);

        pipeline.start();
        trainingLoadInbox.publish(new Message("something"));
        try {
            dataExtractionWorker.join();
            crawlWorker.join();
            frequencyWorker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
