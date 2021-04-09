package github.clyoudu.taskpipeline.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leichen
 */
public class TaskContext {

    private AtomicInteger currentProgress = new AtomicInteger(0);

    private AtomicBoolean canceled = new AtomicBoolean(false);

    private int total = 0;

    private ExecutorService executor = Executors.newFixedThreadPool(6);

    public ExecutorService getExecutor() {
        return executor;
    }

    public void cancel() {
        canceled.set(true);
        executor.shutdownNow();
    }

    public boolean canceled() {
        return canceled.get();
    }

    public int postProgress(int progress) {
        return currentProgress.addAndGet(progress);
    }

    public String getProgress() {
        return new BigDecimal(100 * currentProgress.get()).divide(new BigDecimal(total), 2, RoundingMode.HALF_UP)
            .toPlainString() + "%";
    }

    public int stageInc() {
        return total++;
    }

    public int getTotal() {
        return total;
    }

    public int getCurrentProgress() {
        return currentProgress.get();
    }
}
