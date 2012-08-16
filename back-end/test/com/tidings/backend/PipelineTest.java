package com.tidings.backend;

import com.tidings.backend.domain.NewsTransformer;
import com.tidings.backend.domain.Probability;
import com.tidings.backend.repository.CategoryDistributionRepository;
import com.tidings.backend.repository.CategoryRepository;
import com.tidings.backend.repository.NewsItemsRepository;
import com.tidings.backend.repository.TrainingRepository;
import com.tidings.backend.stages.*;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Pipeline;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.fibers.ThreadFiber;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PipelineTest {

    @Test
    public void shouldExecuteAllStagesOfThePipeline() {
        Pipeline pipeline = new Pipeline();
        final MemoryChannel<Message> crawlInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> transformInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> dedupInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> loaderInbox = new MemoryChannel<Message>();
        MemoryChannel<Message> classificationInbox = new MemoryChannel<Message>();
        ThreadFiber crawlWorker = new ThreadFiber();
        ThreadFiber transformWorker = new ThreadFiber();
        ThreadFiber dedupWorker = new ThreadFiber();
        ThreadFiber classificationWorker = new ThreadFiber();
        ThreadFiber loadingWorker = new ThreadFiber();

        Probability probability = new Probability(new TrainingRepository(), new CategoryDistributionRepository());

        FeedCrawlStage crawlStage = new FeedCrawlStage(crawlInbox, transformInbox, crawlWorker);
        TransformStage trasformStage = transformationStage(transformInbox, dedupInbox, transformWorker);
        DeduplicationStage deduplicationStage = new DeduplicationStage(dedupInbox, classificationInbox, dedupWorker, new NewsItemsRepository());
        ClassificationStage classificationStage = new ClassificationStage(classificationInbox, loaderInbox, classificationWorker, new CategoryRepository(), probability);
        LoadingStage loadingStage = loadingStage(loaderInbox, loadingWorker);

        pipeline.addStage(crawlStage);
        pipeline.addStage(trasformStage);
        pipeline.addStage(deduplicationStage);
        pipeline.addStage(classificationStage);
        pipeline.addStage(loadingStage);
        pipeline.start();
        crawlInbox.publish(new Message(feeds()));
//        final Message crawlTrigger = new Message(new CrawlableSources());

//        ThreadFiber scheduler = new ThreadFiber();
//        Disposable scheduleWorker = scheduler.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("******************************************");
//                System.out.println("Crawl Triggered at " + new Date());
//                System.out.println("******************************************");
//                crawlInbox.publish(crawlTrigger);
//            }
//        }, 0, 120, TimeUnit.MINUTES);
//        scheduler.start();
        try {
            crawlWorker.join();
            transformWorker.join();
            classificationWorker.join();
            loadingWorker.join();

            crawlWorker.dispose();
            transformWorker.dispose();
            classificationWorker.dispose();
            loadingWorker.dispose();
//            scheduleWorker.dispose();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private LoadingStage loadingStage(MemoryChannel<Message> loaderInbox, ThreadFiber loadingWorker) {
        return new LoadingStage(loaderInbox, null, loadingWorker, new NewsItemsRepository());
    }

    private TransformStage transformationStage(MemoryChannel<Message> transformInbox, MemoryChannel<Message> dedupInbox, ThreadFiber transformWorker) {
        return new TransformStage(transformInbox, dedupInbox, transformWorker, new NewsTransformer());
    }

    private List<String> feeds(){
        String[] feeds = {
//                "http://sports.yahoo.com/mlb/rss.xml",
//                "http://add.my.yahoo.com/rss?url=http://sports.yahoo.com/nascar/rss.xml",
//                "http://add.my.yahoo.com/rss?url=http://sports.yahoo.com/sow/rss.xml",
//                "http://sports.yahoo.com/sc/rss.xml",
//                "http://sports.yahoo.com/oly/rss.xml",
//                "http://sports.yahoo.com/ten/rss.xml",
                "http://sports.yahoo.com/nhl/rss.xml",
                "http://www.skysports.com/rss/0,20514,11881,00.xml",
                "http://www.skysports.com/rss/0,20514,12098,00.xml",
//                "http://www.sciencenews.org/view/feed/name/all.rss",
//                "http://www.sciencenews.org/view/feed/collection_id/11/name/Deleted_Scenes.rss",
//                "http://www.sciencenews.org/view/feed/label_id/2356/name/Atom_%2B_Cosmos.rss",
//                "http://www.sciencenews.org/view/feed/label_id/2362/name/Earth.rss",
//                "http://www.sciencenews.org/view/feed/label_id/2337/name/Environment.rss",
                "http://www.sciencenews.org/view/feed/label_id/2363/name/Genes_%2B_Cells.rss",
                "http://www.sciencenews.org/view/feed/label_id/2347/name/Science_%2B_Society.rss",
                "http://rss.sciam.com/ScientificAmerican-Global",
                "http://www.sciencenewsdaily.org/feed.xml",
        };
        return Arrays.asList(feeds);
    }
    
    
    private List<String> bigDataFeeds() {
        String[] feeds = {
//                "http://allthingsd.com/tag/big-data/feed/",
//                "http://feeds.feedburner.com/TdwiChannel-BigDataAnalytics",
//                "http://blogs.teradata.com/rss/",
//                "http://datastori.es/feed/",
//                "http://hortonworks.com/feed/",
//                "http://www.zdnet.com/blog/big-data/rss.xml",
//                "http://feeds.feedburner.com/QlikviewMarketingIntelligenceBlog",
//                "http://www.thebigdatainsightgroup.com/site/rss/articles/all",
//                "http://www.smartercomputingblog.com/category/big-data/feed/",
//                "http://bigdatablog.emc.com/?feed=rss2",
//                "http://blogs.cisco.com/tag/big-data/feed",
//                "http://beautifuldata.net/feed/",
//                "http://blog.ffctn.com/rss.xml",
//                "http://datawithoutborders.cc/feed/",
//                "http://www.teradata.com/rss/Articles",
//                "http://www.teradata.com/rss/News",
//                "http://cloudofdata.com/tag/big-data/feed/",
//                "http://www.sqlstream.com/blog/tag/big-data/feed/",
                "http://www.clusterseven.com/ralph-baxters-big-data-blog/rss.xml",
                "http://www.clusterseven.com/press-releases/rss.xml",
                "http://blogs.splunk.com/feed/",
                "http://feeds2.feedburner.com/WikibonBlog",
                "http://www.appdynamics.com/blog/feed/",
                "http://intelligentbusiness.biz/wordpress/?feed=rss2",
                "http://feeds.feedburner.com/hkotadia/qkGh",
                "http://www.cubrid.org/blog/rss",
                "http://feeds.feedburner.com/smartdatacollective_allposts",
                "http://feeds.feedburner.com/typepad/petewarden",
                "http://marcandrews.typepad.com/marc_andrews/atom.xml",
                "http://www.allthingsdistributed.com/index.xml",
                "http://feeds.feedburner.com/Datavisualization"
        };
        return Arrays.asList(feeds);
    }

    public static void main(String[] args) {
        new PipelineTest().shouldExecuteAllStagesOfThePipeline();
    }
}