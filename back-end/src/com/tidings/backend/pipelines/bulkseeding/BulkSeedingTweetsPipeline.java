package com.tidings.backend.pipelines.bulkseeding;

import com.tidings.backend.domain.Document;
import com.tidings.backend.repository.TrainingRepository;
import com.tidings.backend.utils.FileUtils;
import com.tidings.backend.utils.IActionOnLine;

public class BulkSeedingTweetsPipeline {

    private final TrainingRepository repository;
    private final String trainingFile;

    public BulkSeedingTweetsPipeline(TrainingRepository repository, String trainingFile) {
        this.repository = repository;
        this.trainingFile = trainingFile;
    }

    public void run() {
        FileUtils.foreachLineInFile(trainingFile, new CreateNewsItem());
    }

    private class CreateNewsItem implements IActionOnLine {
        long count = 1;

        public void act(String line) {
            try {
                String[] split = line.split(",");
                String sentiment = "0".equals(split[1]) ? "negative" : "positive";
                String tweet = split[3];
                repository.save(new Document(count++, tweet, sentiment));
            } catch (Exception e) {
                System.out.println("ERROR: Failed creating tweet data: \"" + line + "\": " + e);
            }
        }
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("ERROR: Specify the data file as an argument.");
            System.exit(0);
        }
        new BulkSeedingTweetsPipeline(TrainingRepository.forSentimentAnalysis(), args[0]).run();
    }
}