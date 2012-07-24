package com.tidings.backend;

import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.MessagePredicate;
import messagepassing.pipeline.Pipeline;
import messagepassing.pipeline.PredicatedChannel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;
import org.junit.Test;

public class TrainingPipelineTest {

    @Test
    public void shouldExecuteTheTrainingPipeline() {
        TrainingRepository trainingRepository = new TrainingRepository();

        Pipeline pipeline = new Pipeline();

        MemoryChannel<Message> trainingLoadInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> trainingLoadOutbox = new MemoryChannel<Message>();

        ThreadFiber dataExtractionWorker = new ThreadFiber();
        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber frequencyWorker = new ThreadFiber();
        ThreadFiber probablityWorker = new ThreadFiber();

////        TrainingDataExtractionStage extractionStage =
////                new TrainingDataExtractionStage(trainingLoadInbox, transformationInbox, dataExtractionWorker, trainingRepository);
////
////        TrainingRecordTransformationStage transformationStage = new TrainingRecordTransformationStage(transformationInbox, frequencyInbox, crawlWorker);
////
////        FrequencyCompuationStage frequencyCompuationStage = new FrequencyCompuationStage(frequencyInbox, probabilityInbox, frequencyWorker);
////
////        ProbabilityCompuationStage probabilityCompuationStage = new ProbabilityCompuationStage(probabilityInbox, null, probablityWorker);
//
//        pipeline.addStage(extractionStage);
//        pipeline.addStage(transformationStage);
//        pipeline.addStage(frequencyCompuationStage);
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
