package com.tidings.backend;

import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;
import org.junit.Test;

public class TrainingPipelineTest {

    @Test
    public void shouldExecuteTheTrainingPipeline() {
        Pipeline pipeline = new Pipeline();

        MemoryChannel<Message> trainingLoadInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> crawlInbox = new MemoryChannel<Message>();
        ThreadFiber crawlWorker = new ThreadFiber();
        TrainingRepository trainingRepository = new TrainingRepository();
        TrainingDataExtractionStage extractionStage = new TrainingDataExtractionStage(trainingLoadInbox, crawlInbox, crawlWorker, trainingRepository);
        pipeline.addStage(extractionStage);
        pipeline.start();
        trainingLoadInbox.publish(new Message("something"));
    }
}
