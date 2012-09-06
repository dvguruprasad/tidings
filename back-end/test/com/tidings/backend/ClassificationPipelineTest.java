package com.tidings.backend;

import com.tidings.backend.pipelines.classification.ClassificationPipeline;
import org.junit.Test;

public class ClassificationPipelineTest {
    @Test
    public void shouldExecuteAllStagesOfThePipeline() {
        new ClassificationPipeline().run();
    }
}