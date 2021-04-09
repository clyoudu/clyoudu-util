package github.clyoudu.taskpipeline.task;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * @author leichen
 */
@Slf4j
public abstract class AbstractStage {

    private TaskContext taskContext;

    private AbstractStage parentStage;

    private String name;

    private boolean finished;

    private String status;

    private AtomicInteger finishedCount = new AtomicInteger(0);

    public AbstractStage(TaskContext taskContext, AbstractStage parentStage, String name) {
        this.taskContext = taskContext;
        this.parentStage = parentStage;
        this.name = name;
        getTaskContext().stageInc();
    }

    public String getName() {
        return name;
    }

    public AtomicInteger getFinishedCount() {
        return finishedCount;
    }

    public AbstractStage getParentStage() {
        return parentStage;
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public String getStatus() {
        return status;
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * 进度上报
     */
    protected abstract void finished();

    /**
     * 进度查询
     * @return 进度
     */
    protected abstract String getProgress();

    /**
     * 执行之前
     */
    protected abstract void before();

    /**
     * 执行之后
     */
    protected abstract void after();

    /**
     * 执行
     * @return 执行是否成功
     * @throws Exception
     */
    protected abstract boolean executeInternal() throws Exception;

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 执行
     */
    public boolean execute() {
        try {
            if (!taskContext.canceled() && !Thread.currentThread().isInterrupted()) {
                before();
                boolean result = executeInternal();
                if (!result) {
                    log.error("任务执行{}失败", name);
                    taskContext.cancel();
                    status = "failed";
                }
                after();
                status = "success";
                finished();
            } else {
                log.error("{}取消", name);
                status = "canceled";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            status = "canceled";
            log.error("{}取消", name);
        } catch (Exception e) {
            log.error("执行{}异常", name, e);
            taskContext.cancel();
            status = "exception";
        } finally {
            finished = true;
        }

        return "success".equals(getStatus());
    }

}
