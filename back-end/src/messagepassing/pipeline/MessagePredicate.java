package messagepassing.pipeline;

import com.tidings.backend.domain.TrainingRecord;

import java.util.ArrayList;
import java.util.List;

public class MessagePredicate {
    private int totalCount;
    private List<TrainingRecord> extractedRecords = new ArrayList<TrainingRecord>();

    public boolean isTrue() {
        return extractedRecords.size() == 100;
    }

    public Message getMessage() {
        return new Message("consolidated");
    }

    public void process(Message m) {
        List<TrainingRecord> newOnes = (List<TrainingRecord>) m.payload();
        extractedRecords.addAll(newOnes);
    }

    public void add(Message s) {

    }
}
