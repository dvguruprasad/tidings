package com.tidings.backend;

import com.tidings.backend.pipelines.training.TweetSentimentTrainingPipeline;
import org.junit.Test;

public class TweetSentimentTrainingPipelineTest {
    @Test
    public void shouldExecuteTheTrainingPipeline() {
        new TweetSentimentTrainingPipeline().start();
    }
}