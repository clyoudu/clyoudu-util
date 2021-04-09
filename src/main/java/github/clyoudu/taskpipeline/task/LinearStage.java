package github.clyoudu.taskpipeline.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author leichen
 */
public class LinearStage extends Stage {

    private List<AbstractStage> stages;

    public LinearStage(TaskContext taskContext, AbstractStage parentStage, String name, List<AbstractStage> stages) {
        super(taskContext, parentStage, name);
        this.stages = stages;
    }

    public List<AbstractStage> getStages() {
        return stages;
    }

    @Override
    public String getProgress() {
        return new BigDecimal(100 * getFinishedCount().get())
            .divide(new BigDecimal(stages.size()), 2, RoundingMode.HALF_UP).toPlainString() + "%";
    }

    @Override
    protected boolean executeInternal() throws Exception {
        for (AbstractStage stage : stages) {
            boolean result = stage.execute();
            if (!result) {
                return false;
            }
        }
        return true;
    }
}
