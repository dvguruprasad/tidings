package com.tidings.backend;

import com.tidings.backend.pipelines.classification.ClassificationPipeline;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ClassificationPipelineTest {

    @Test
    public void shouldExecuteAllStagesOfThePipeline() {
        new ClassificationPipeline(feeds()).start();
    }

    private List<String> feeds() {
        String[] feeds = {
                "http://www.thehindu.com/sport/other-sports/?service=rss",
                "http://www.thehindu.com/sport/motorsport/?service=rss",
                "http://www.thehindu.com/sport/tennis/?service=rss",
                "http://www.thehindu.com/sport/cricket/?service=rss",
                "http://www.thehindu.com/sport/athletics/?service=rss",
                "http://www.thehindu.com/sport/football/?service=rss",
                "http://www.thehindu.com/arts/cinema/?service=rss",
                "http://www.thehindu.com/arts/dance/?service=rss",
                "http://www.thehindu.com/life-and-style/kids/?service=rss",
                "http://www.thehindu.com/sci-tech/technology/?service=rss",
                "http://us.lrd.yahoo.com/_ylt=AlmyQsAJU_5RRWApxa9dki3zscB_;_ylu=X3oDMTFnZDRsOWw5BG1pdANSU1MgRXRlcnRhaW5tZW50BHBvcwM1BHNlYwNNZWRpYVJTU0VkaXRvcmlhbA--;_ylg=X3oDMTFyZGdqa3FwBGludGwDaW4EbGFuZwNlbi1pbgRwc3RhaWQDBHBzdGNhdANlbnRlcnRhaW5tZW50BHB0A3NlY3Rpb25z;_ylv=0/SIG=11qm111fu/EXP=1347362371/**http%3A//in.news.yahoo.com/rss/bollywood",
                "http://us.lrd.yahoo.com/_ylt=ArOt0UYdvUYcKgtCWXFl0jbzscB_;_ylu=X3oDMTFnNW91azJhBG1pdANSU1MgRXRlcnRhaW5tZW50BHBvcwM3BHNlYwNNZWRpYVJTU0VkaXRvcmlhbA--;_ylg=X3oDMTFyZGdqa3FwBGludGwDaW4EbGFuZwNlbi1pbgRwc3RhaWQDBHBzdGNhdANlbnRlcnRhaW5tZW50BHB0A3NlY3Rpb25z;_ylv=0/SIG=11qr2iksv/EXP=1347362371/**http%3A//in.news.yahoo.com/rss/hollywood"
        };
        return Arrays.asList(feeds);
    }
}