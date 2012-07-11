package messagepassing.pipeline;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    private List<Stage> stages = new ArrayList<Stage>();

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    public void start() {
        for (Stage stage : stages) {
            stage.start();
        }
    }
}