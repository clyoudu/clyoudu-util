package github.clyoudu.taskpipeline;

import java.util.ArrayList;
import java.util.List;

import github.clyoudu.taskpipeline.task.AbstractStage;
import github.clyoudu.taskpipeline.task.LinearStage;
import github.clyoudu.taskpipeline.task.Stage;
import github.clyoudu.taskpipeline.task.TaskContext;
import org.junit.Test;

public class LinearTaskTest {

    @Test
    public void test() throws InterruptedException {
        TaskContext taskContext = new TaskContext();
        List<AbstractStage> stages = new ArrayList<>();
        LinearStage linearStage = new LinearStage(taskContext, null, "线性执行", stages);

        List<AbstractStage> stagesA = new ArrayList<>();
        Stage stageA = new LinearStage(taskContext, linearStage, "任务A", stagesA);
        stagesA.add(new Stage(taskContext, stageA, "A1"));
        stagesA.add(new Stage(taskContext, stageA, "A2"));
        stages.add(stageA);

        List<AbstractStage> stagesB = new ArrayList<>();
        Stage stageB = new LinearStage(taskContext, linearStage, "任务B", stagesB);
        stagesB.add(new Stage(taskContext, stageB, "B1"));
        stagesB.add(new Stage(taskContext, stageB, "B2"));
        stagesB.add(new Stage(taskContext, stageB, "B3"));
        stages.add(stageB);

        List<AbstractStage> stagesC = new ArrayList<>();
        Stage stageC = new LinearStage(taskContext, linearStage, "任务C", stagesC);
        stagesC.add(new Stage(taskContext, stageC, "C1"));
        stagesC.add(new Stage(taskContext, stageC, "C2"));
        stagesC.add(new Stage(taskContext, stageC, "C3"));
        stagesC.add(new Stage(taskContext, stageC, "C4"));
        stages.add(stageC);

        List<AbstractStage> stagesD = new ArrayList<>();
        Stage stageD = new LinearStage(taskContext, linearStage, "任务D", stagesD);
        stagesD.add(new Stage(taskContext, stageD, "D1"));
        stagesD.add(new Stage(taskContext, stageD, "D2"));
        stagesD.add(new Stage(taskContext, stageD, "D3"));
        stagesD.add(new Stage(taskContext, stageD, "D4"));
        stagesD.add(new Stage(taskContext, stageD, "D5"));
        stages.add(stageD);

        new Thread(linearStage::execute).start();
        while (true) {
            System.out.println(
                "Total Progress: " + taskContext.getProgress() + "(" + taskContext.getCurrentProgress() + "/" +
                    taskContext.getTotal() + "), Linear(" + linearStage.getProgress() + ") A(" + stageA.getProgress() +
                    "), B(" + stageB.getProgress() + "), C(" + stageC.getProgress() + "), D(" +
                    stageD.getProgress() + ")");
            if (taskContext.getCurrentProgress() == taskContext.getTotal() || taskContext.canceled()) {
                break;
            }
            Thread.sleep(1000L);
        }
    }

}
