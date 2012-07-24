package com.tidings.backend.stages;

import com.tidings.backend.domain.Document;
import com.tidings.backend.domain.TrainingRecord;
import com.tidings.backend.domain.WordBag;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.util.List;

public class TrainingRecordTransformationStage extends Stage {

    public TrainingRecordTransformationStage(Channel<Message> inbox, Channel<Message> outbox, Fiber worker) {
        super(inbox, outbox, worker);
    }

    public void onMessage(Message message) {
        List<TrainingRecord> trainingRecords = (List<TrainingRecord>) message.payload();
        for (TrainingRecord trainingRecord : trainingRecords) {
            Document document = new Document(WordBag.create(trainingRecord.transformedText()), trainingRecord.category());
            publish(new Message(document));
        }
    }
}
