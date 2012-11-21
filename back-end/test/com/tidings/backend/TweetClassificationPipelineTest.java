package com.tidings.backend;

import com.tidings.backend.pipelines.classification.TweetClassificationPipeline;
import org.junit.Test;

public class TweetClassificationPipelineTest {
    @Test
    public void shouldExecuteAllStagesOfThePipeline() {
        new TweetClassificationPipeline().run();
    }
}