package github.clyoudu.taskpipeline.task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

/**
 * @author leichen
 */
@Slf4j
public class ParallelStage extends LinearStage {

    public ParallelStage(TaskContext taskContext, AbstractStage parentStage, String name, List<AbstractStage> stages) {
        super(taskContext, parentStage, name, stages);
    }

    @Override
    protected boolean executeInternal() throws Exception {
        CompletableFuture[] futures = new CompletableFuture[getStages().size()];
        for (int i = 0; i < futures.length; i++) {
            int finalI = i;
            futures[i] = CompletableFuture
                .supplyAsync(() -> getStages().get(finalI).execute(), getTaskContext().getExecutor());
        }
        CompletableFuture.allOf(futures).join();
        return true;
    }

    @Override
    protected void after() {
        log.info("{} start async", getName());
    }
}
