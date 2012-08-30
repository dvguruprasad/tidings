package com.tidings.backend;

import com.tidings.backend.pipelines.training.TrainingPipeline;
import org.junit.Test;

public class TrainingPipelineTest {
    @Test
    public void shouldExecuteTheTrainingPipeline() {
        new TrainingPipeline().start();
    }
}