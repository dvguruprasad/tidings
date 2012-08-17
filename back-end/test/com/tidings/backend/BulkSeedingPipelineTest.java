package com.tidings.backend;

import com.tidings.backend.pipelines.bulkseeding.TrainingDataCrawlStage;
import com.tidings.backend.pipelines.bulkseeding.TrainingDataDeduplicationStage;
import com.tidings.backend.pipelines.training.TrainingDataLoadingStage;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BulkSeedingPipelineTest {

    @Test
    public void shouldExecuteAllStagesOfThePipeline() {
        Pipeline pipeline = new Pipeline();
        final MemoryChannel<Message> crawlInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> dedupInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> loaderInbox = new MemoryChannel<Message>();

        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber transformWorker = new ThreadFiber();
        ThreadFiber dedupWorker = new ThreadFiber();
        ThreadFiber loadingWorker = new ThreadFiber();

        TrainingDataCrawlStage crawlStage = new TrainingDataCrawlStage(crawlInbox, dedupInbox, crawlWorker);
        TrainingDataDeduplicationStage deduplicationStage = new TrainingDataDeduplicationStage(dedupInbox, loaderInbox, dedupWorker, new TrainingRepository());
        TrainingDataLoadingStage trainingDataLoadingStage = new TrainingDataLoadingStage(loaderInbox, null, loadingWorker, new TrainingRepository());

        pipeline.addStage(crawlStage);
        pipeline.addStage(deduplicationStage);
        pipeline.addStage(trainingDataLoadingStage);
        pipeline.start();
        crawlInbox.publish(new Message(feeds()));
        try {
            crawlWorker.join();
            transformWorker.join();
            loadingWorker.join();

            crawlWorker.dispose();
            transformWorker.dispose();
            loadingWorker.dispose();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String[]> feeds() {
        Map<String, String[]> categorizedFeeds = new HashMap<String, String[]>();
        String[] sportsFeeds = new String[]{
                "http://sports.yahoo.com/mlb/rss.xml",
                "http://sports.yahoo.com/sc/rss.xml",
                "http://sports.yahoo.com/oly/rss.xml",
                "http://sports.yahoo.com/ten/rss.xml",
                "http://sports.yahoo.com/nhl/rss.xml",
                "http://www.skysports.com/rss/0,20514,11881,00.xml",
                "http://www.skysports.com/rss/0,20514,12098,00.xml",
        };
        String[] scienceFeeds = new String[]{
                "http://www.sciencenews.org/view/feed/name/all.rss",
                "http://www.sciencenews.org/view/feed/collection_id/11/name/Deleted_Scenes.rss",
                "http://www.sciencenews.org/view/feed/label_id/2356/name/Atom_%2B_Cosmos.rss",
                "http://www.sciencenews.org/view/feed/label_id/2362/name/Earth.rss",
                "http://www.sciencenews.org/view/feed/label_id/2337/name/Environment.rss",
                "http://www.sciencenews.org/view/feed/label_id/2363/name/Genes_%2B_Cells.rss",
                "http://www.sciencenews.org/view/feed/label_id/2347/name/Science_%2B_Society.rss",
                "http://rss.sciam.com/ScientificAmerican-Global",
                "http://www.sciencenewsdaily.org/feed.xml",
        };

        String[] entertainmentFeeds = new String[]{
                "http://news.google.com/news?pz=1&cf=all&ned=in&hl=en&topic=e&output=rss",
                "http://timesofindia.indiatimes.com/rssfeeds/1081479906.cms",
                "http://www.eonline.com/syndication/feeds/rssfeeds/topstories.xml",
                "http://www.eonline.com/syndication/feeds/rssfeeds/celebritynews.xml",
                "http://www.thehindu.com/arts/music/?service=rss",
                "http://www.thehindu.com/arts/theatre/?service=rss",
                "http://www.thehindu.com/arts/radio-and-tv/?service=rss",
                "http://www.bollywoodhungama.com/rss/news.xml",
                "http://www.bollywoodhungama.com/rss/movie_reviews.xml",
                "http://www.bollywoodhungama.com/rss/music_reviews.xml"
        };
        categorizedFeeds.put("Sports", sportsFeeds);
        categorizedFeeds.put("Science", scienceFeeds);
        categorizedFeeds.put("Entertainment", entertainmentFeeds);
        return categorizedFeeds;
    }


    public static void main(String[] args) {
        new BulkSeedingPipelineTest().shouldExecuteAllStagesOfThePipeline();
    }
}