package com.tidings.backend.stages;

import com.tidings.backend.domain.TrainingRecord;
import com.tidings.backend.repository.TrainingRepository;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import messagepassing.pipeline.Message;
import messagepassing.pipeline.Stage;
import org.jetlang.channels.Channel;
import org.jetlang.fibers.Fiber;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TrainingDataExtractionStage extends Stage {
    private TrainingRepository trainingRepository;

    public TrainingDataExtractionStage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber, TrainingRepository trainingRepository) {
        super(inbox, outbox, threadFiber);
        this.trainingRepository = trainingRepository;
    }

    public void onMessage(Message message) {
        long total = trainingRepository.getCategorizedRecordsCount();
        int pageSize = 50;
        int numberOfPages = ((int) Math.ceil((double) total / pageSize));
        while (numberOfPages-- > 0) {
            List<TrainingRecord> records = trainingRepository.getCategorizedRecords(pageSize);
            for (TrainingRecord record : records) {
                record.setTransformedText(extractText(record));
            }
            publish(new Message(records));
        }
    }

    private String extractText(TrainingRecord record) {
        String extractedText = null;
        try {
            extractedText = ArticleExtractor.INSTANCE.getText(new URL(record.link()));
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return extractedText;
    }
}