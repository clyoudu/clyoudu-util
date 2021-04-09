package github.clyoudu.taskpipeline.task;

import java.security.SecureRandom;

import lombok.extern.slf4j.Slf4j;

/**
 * @author leichen
 */
@Slf4j
public class Stage extends AbstractStage {

    public Stage(TaskContext taskContext, AbstractStage parentStage, String name) {
        super(taskContext, parentStage, name);
    }

    @Override
    protected void finished() {
        getTaskContext().postProgress(1);
        if (getParentStage() != null) {
            getParentStage().getFinishedCount().addAndGet(1);
        }
    }

    @Override
    public String getProgress() {
        return getFinishedCount().get() == 1 ? "100%" : "0%";
    }

    @Override
    protected void before() {
        log.info("{} start", getName());
    }

    @Override
    protected void after() {
        log.info("{} end", getName());
    }

    @Override
    protected boolean executeInternal() throws Exception {
        if (getName().equals("C1")) {
            //throw new RuntimeException("error");
        }

        Thread.sleep(new SecureRandom().nextInt(1000) * 10L);

        log.info("{}", getName());
        return true;
    }
}
