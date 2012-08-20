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
                "http://newsrss.bbc.co.uk/rss/sportonline_uk_edition/cricket/rss.xml",
                "http://news.bbc.co.uk/rss/sportonline_world_edition/cricket/rss.xml",
                "http://www.cricketdamaka.com/feed/",
                "http://www.cricinfo.com/homepage/index.rdf",
                "http://www.telegraph.co.uk/newsfeed/rss/sport_cricket.xml",
                "http://www.star-cricket.com/index.php/feed",
                "http://www.star-cricket.com/feed",
                "http://www.cricket-blog.com/feed.xml",
                "http://feeds.feedburner.com/crickety",
                "http://www.fanblogs.com/sec/archives/arkansas.xml",
                "http://news.bbc.co.uk/rss/sportonline_world_edition/football/teams/i/inverness_ct/rss.xml",
                "http://big3scouting.com/feed/rss/",
                "http://feeds.feedburner.com/buffs",
                "http://www.fanblogs.com/acc/archives/florida_state.xml",
                "http://georgiatech.rivals.com/rss2feed.asp?SID=1032",
                "http://www.patriotsplanet.com/BB/external.php?type=rss2&forumids=2",
                "http://z.about.com/6/g/worldsoccer/b/index.xml",
                "http://feeds.feedburner.com/Eplmatchescom",
                "http://www.eplmatches.com/index.php/feed/",
                "http://www.aboutaball.co.uk/footballnews/NewsFeed.php",
                "http://feeds.feedburner.com/DragBoatAlleyCalendarFull",
                "http://www.treknraft.com/treknraft-feed.xml",
                "http://z.about.com/6/g/swimming/b/index.xml",
                "http://newsrss.bbc.co.uk/rss/sportonline_uk_edition/other_sports/swimming/rss.xml",
                "http://www.swimmingworldmagazine.com/common/published/rss/homepage_news.xml",
                "http://interglacial.com/rss/adult_swim.rss",
                "http://feeds.bignewsnetwork.com/?rss=0ef2595a306055ff",
                "http://www.tennis-x.com/xblog/feed",
                "http://news.bbc.co.uk/rss/sportonline_world_edition/tennis/rss.xml",
                "http://sports.espn.go.com/espn/rss/tennis/news?null",
                "http://feeds.feedburner.com/reuters/UKTennisNews",
                "http://sports.yahoo.com/ten/rss.xml",
                "http://sports.espn.go.com/espn/rss/columnist?name=simmons_bill",
                "http://hitbyapitch.wordpress.com/feed",
                "http://news.bbc.co.uk/rss/sportonline_world_edition/other_sports/cycling/rss.xml",
                "http://www.fit-facts.com/blog/feed/rss/",
        };

        String[] scienceFeeds = new String[]{
                "http://www.sciencenews.org/view/feed/collection_id/11/name/Deleted_Scenes.rss",
                "http://www.sciencenews.org/view/feed/label_id/2356/name/Atom_%2B_Cosmos.rss",
                "http://www.sciencenews.org/view/feed/label_id/2362/name/Earth.rss",
                "http://www.sciencenews.org/view/feed/label_id/2337/name/Environment.rss",
                "http://www.sciencenews.org/view/feed/label_id/2363/name/Genes_%2B_Cells.rss",
                "http://www.sciencenews.org/view/feed/label_id/2347/name/Science_%2B_Society.rss",
                "http://rss.sciam.com/ScientificAmerican-Global",
                "http://www.sciencenewsdaily.org/feed.xml",
                "http://www.sciencedaily.com/rss/top_news.xml",
                "http://www.sciencedaily.com/rss/top_news/top_science.xml",
                "http://www.sciencedaily.com/rss/top_news/top_health.xml",
                "http://www.sciencedaily.com/rss/living_well.xml",
                "http://www.sciencedaily.com/rss/strange_science.xml",
                "http://www.sciencedaily.com/rss/most_popular.xml",
                "http://popsci.com/rss.xml",
                "http://feeds.howstuffworks.com/DailyStuff",
                "http://www.nasa.gov/rss/breaking_news.rss",
                "http://www.sciencemag.org/rss/twis.xml",
                "http://www.nytimes.com/services/xml/rss/nyt/Science.xml",
                "http://web.mit.edu/newsoffice/topic/mithealth-rss.xml",
                "http://web.mit.edu/newsoffice/topic/mitphysics-rss.xml",
                "http://web.mit.edu/newsoffice/topic/mitchemistry-rss.xml",
                "http://web.mit.edu/newsoffice/topic/mitbiology-rss.xml",
                "http://www.nytimes.com/services/xml/rss/nyt/Space.xml",
                "http://www.washingtonpost.com/wp-dyn/rss/nation/science/index.xml",
                "http://www.npr.org/rss/rss.php?id=1007",
                "http://www.nasa.gov/rss/science_at_nasa.rss",
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
                "http://www.bollywoodhungama.com/rss/music_reviews.xml",
                "http://villagevoice.com/rss/music.rss.xml",
                "http://newsrss.bbc.co.uk/rss/newsonline_world_edition/entertainment/rss.xml",
                "http://www.npr.org/rss/rss.php?id=1041",
                "http://www.npr.org/rss/rss.php?id=1042",

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