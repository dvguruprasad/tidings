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

        FeedCrawlStage crawlStage = new FeedCrawlStage(crawlInbox, transformInbox, crawlWorker);
        TransformStage trasformStage = transformationStage(transformInbox, dedupInbox, transformWorker);
        DeduplicationStage deduplicationStage = deduplicationStage(dedupInbox, classificationInbox, dedupWorker);
        ClassificationStage classificationStage = classificationStage(loaderInbox, classificationInbox, classificationWorker);
        LoadingStage loadingStage = loadingStage(loaderInbox, loadingWorker);

        pipeline.addStage(crawlStage);
        pipeline.addStage(trasformStage);
        pipeline.addStage(deduplicationStage);
        pipeline.addStage(classificationStage);
        pipeline.addStage(loadingStage);
        pipeline.start();
        crawlInbox.publish(new Message(bigDataFeeds()));
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
            loadingWorker.join();

            crawlWorker.dispose();
            transformWorker.dispose();
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

    private DeduplicationStage deduplicationStage(MemoryChannel<Message> dedupInbox, MemoryChannel<Message> classificationInbox, ThreadFiber dedupWorker) {
        return new DeduplicationStage(dedupInbox, classificationInbox, dedupWorker, new NewsItemsRepository());
    }

    private ClassificationStage classificationStage(MemoryChannel<Message> loaderInbox, MemoryChannel<Message> classificationInbox, ThreadFiber classificationWorker) {
        return new ClassificationStage(classificationInbox, loaderInbox, classificationWorker, new CategoryRepository(), new Probability(new TrainingRepository(), new CategoryDistributionRepository()));
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
        };
        return Arrays.asList(feeds);
    }

    public static void main(String[] args) {
        new PipelineTest().shouldExecuteAllStagesOfThePipeline();
    }
}