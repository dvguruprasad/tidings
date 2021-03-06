package com.tidings.backend.pipelines.bulkseeding;

import com.tidings.backend.pipelines.training.TrainingDataLoadingStage;
import com.tidings.backend.repository.NewsTrainingRepository;
import com.tidings.backend.repository.TrainingRepository;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;

import java.util.HashMap;
import java.util.Map;

public class BulkSeedingPipeline {
    private Map<String, String[]> feeds;

    public BulkSeedingPipeline(Map<String, String[]> feeds) {
        this.feeds = feeds;
    }

    public void run() {
        Pipeline pipeline = new Pipeline();
        final MemoryChannel<Message> crawlInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> dedupInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> loaderInbox = new MemoryChannel<Message>();

        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber transformWorker = new ThreadFiber();
        ThreadFiber dedupWorker = new ThreadFiber();
        ThreadFiber loadingWorker = new ThreadFiber();

        NewsTrainingRepository trainingRepository = TrainingRepository.forNewsClassification();

        TrainingDataCrawlStage crawlStage = new TrainingDataCrawlStage(crawlInbox, dedupInbox, crawlWorker, trainingRepository);
        TrainingDataDeduplicationStage deduplicationStage = new TrainingDataDeduplicationStage(dedupInbox, loaderInbox, dedupWorker, trainingRepository);
        TrainingDataLoadingStage trainingDataLoadingStage = new TrainingDataLoadingStage(loaderInbox, null, loadingWorker, trainingRepository);

        pipeline.addStage(crawlStage);
        pipeline.addStage(deduplicationStage);
        pipeline.addStage(trainingDataLoadingStage);
        pipeline.start();
        crawlInbox.publish(new Message(feeds));
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

    public static void main(String[] args) {
        new BulkSeedingPipeline(feeds()).run();
    }

    private static Map<String, String[]> feeds() {
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
                "http://www.npr.org/rss/rss.php?id=1042",
                " http://www.nytimes.com/services/xml/rss/nyt/Music.xml",
                "http://www.usmagazine.com/this_minute/feed",
                "http://www.nytimes.com/services/xml/rss/nyt/Television.xml",
                "http://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=6&cad=rja&ved=0CGwQFjAF&url=http%3A%2F%2Fnewsrss.bbc.co.uk%2Frss%2Fnewsonline_uk_edition%2Fentertainment%2Frss.xml&ei=eRUzUNXUBMqrrAfU5ICYDw&usg=AFQjCNECDux4AhqFIPAA67g6gN29AlCgNg",
                "http://www.glamsham.com/rss/glamRss.xml",
                "http://www.glamsham.com/rss/glamrss_scoops.xml",
                "http://www.glamsham.com/rss/glamrss_review.xml",
                "http://feedproxy.google.com/oneindia-all-news",
                "http://feedproxy.google.com/oneindia-entertainment-bollywood",
                "http://feedproxy.google.com/oneindia-entertainment-hollywood",
                "http://feedproxy.google.com/oneindia-entertainment-music",
                "http://feedproxy.google.com/oneindia-thatskannada-news",
                "http://www.bharatstudent.com/cafebharat/hindi_rss.php",
                "http://entertainment.in.msn.com/rss/bollywood.aspx",
                "http://www.mtv.com/rss/news/news_full.jhtml",
                "http://www.music-news.com/rss/news.asp",
                "http://feeds.feedburner.com/thr/news",
                "http://news.yahoo.com/rss/entertainment;_ylt=AiLpBB22wyO7q9X_kY_u4HswFxkF;_ylu=X3oDMTFoZGJxbzBpBG1pdANSU1MgRW50ZXJ0YWlubWVudARwb3MDNQRzZWMDTWVkaWFSU1NFZGl0b3JpYWw-;_ylg=X3oDMTIyaW90OGk5BGludGwDdXMEbGFuZwNlbi11cwRwc3RhaWQDBHBzdGNhdANlbnRlcnRhaW5tZW50fG1vdmllcwRwdANzZWN0aW9ucw--;_ylv=3",
                "http://news.yahoo.com/rss/fashion;_ylt=AscqSiI.y3oQ_aDPD_Xtk3EwFxkF;_ylu=X3oDMTFpYzhiNWhyBG1pdANSU1MgRW50ZXJ0YWlubWVudARwb3MDMTcEc2VjA01lZGlhUlNTRWRpdG9yaWFs;_ylg=X3oDMTIyaW90OGk5BGludGwDdXMEbGFuZwNlbi11cwRwc3RhaWQDBHBzdGNhdANlbnRlcnRhaW5tZW50fG1vdmllcwRwdANzZWN0aW9ucw--;_ylv=3",
                "http://www.blockbuster.com/rss/top100",
        };

        String[] geekFeeds = new String[]{
                "http://news.ycombinator.com/rss",
                "http://www.reddit.com/r/programming.rss",
                "http://www.systemswemake.com/feed/",
                "http://www.lisazhang.ca/feeds/posts/default",
                "http://tm.durusau.net/?feed=rss2",
                "http://bitworking.org/news/feed/",
                "http://feeds.feedburner.com/maxindelicato",
                "http://pl.atyp.us/wordpress/?feed=rss2",
                "http://clustercenter.org/rss2/all/",
                "http://databeta.wordpress.com/feed/",
                "http://fishofthebay.com/feed",
                "http://www.dist-systems.bbn.com/papers/rss.xml",
                "http://glinden.blogspot.com/feeds/posts/default",
                "http://feeds.feedburner.com/catonmat",
                "http://feeds.feedburner.com/HackingNetflix",
                "http://www.stevesouders.com/blog/feed/",
                "http://highscalability.com/rss.xml",
                "http://www.informaniac.net/feeds/posts/default",
                "http://www.kitchensoap.com/feed/",
                "http://feeds.feedburner.com/MainlyData",
                "http://muratbuffalo.blogspot.com/feeds/posts/default",
                "http://mint.typepad.com/blog/rss.xml",
                "http://blogs.msdn.com/pathelland/rss.xml",
                "http://perspectives.mvdirona.com/SyndicationService.asmx/GetRss",
                "http://horicky.blogspot.com/feeds/posts/default",
                "http://feeds.feedburner.com/Shell-fu",
                "http://feeds.feedburner.com/thechangelog",
                "http://roy.gbiv.com/untangled/feed",
                "http://www.allthingsdistributed.com/rss.xml",
                "http://www.webperformancematters.com/journal/rss.xml",
                "http://bartoszmilewski.wordpress.com/feed/",
                "http://www.dzone.com/links/feed/frontpage/rss.xml",
                "http://weblogs.asp.net/scottgu/rss.aspx",
                "http://feeds.feedburner.com/ScottHanselman",
                "http://feeds.feedburner.com/37signals/beMH",
                "http://www.codeplex.com/site/feeds/rss",
                "http://feeds.feedburner.com/pushingpixels",
                "http://successfulsoftware.wordpress.com/feed",
                "http://feeds2.feedburner.com/ProgrammableWeb",
                "http://feeds.feedburner.com/hashrocket-blog",
                "http://feeds.feedburner.com/allaboutagile",
                "http://byatool.com/feed/",
                "http://feeds2.feedburner.com/9lesson",
                "http://jwcooney.com/feed/",
                "http://blog.softwaredevelopersindia.com/?feed=rss2",
                "http://feeds.feedburner.com/RuminationsOfAProgrammer",
        };
        categorizedFeeds.put("sports", sportsFeeds);
        categorizedFeeds.put("science", scienceFeeds);
        categorizedFeeds.put("entertainment", entertainmentFeeds);
        categorizedFeeds.put("softwaredevelopment", geekFeeds);
        return categorizedFeeds;
    }
}