package com.tidings.backend;

import com.tidings.backend.pipelines.bulkseeding.BulkSeedingTweetsPipeline;
import com.tidings.backend.repository.TrainingRepository;
import org.junit.Test;

public class BulkSeedingTweetsPipelineTest {
    @Test
    public void shouldExecuteAllStagesOfThePipeline() {
        BulkSeedingTweetsPipeline pipeline = new BulkSeedingTweetsPipeline(TrainingRepository.forSentimentAnalysis(), "/Users/Guru/code/tidings_sentiment_data/2000_sentiment_analysis_data.csv");
        pipeline.run();
    }
}