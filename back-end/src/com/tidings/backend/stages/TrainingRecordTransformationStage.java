package com.tidings.backend.stages;

import com.tidings.backend.domain.*;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class TrainingRecordTransformationStage extends Stage {

    private final TextSanitizer textSanitizer;
    private int count;

    public TrainingRecordTransformationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker) {
        super(inbox, outbox, worker);
        textSanitizer = new TextSanitizer(new StopWords("data/stopwords.txt"));
        count = 0;
    }

    public void onMessage(Message message) {
        List<TrainingRecord> trainingRecords = (List<TrainingRecord>) message.payload();

        for (TrainingRecord trainingRecord : trainingRecords) {
            List<String> sanitized = textSanitizer.sanitize(trainingRecord.transformedText());
            Document document = new Document(WordBag.create(sanitized), trainingRecord.category());
            count++;
                publish(new Message(document));
        }
    }
}