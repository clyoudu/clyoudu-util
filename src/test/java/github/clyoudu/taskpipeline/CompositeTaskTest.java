package github.clyoudu.taskpipeline;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.clyoudu.fileutil.FileUtil;
import github.clyoudu.taskpipeline.task.AbstractStage;
import github.clyoudu.taskpipeline.task.LinearStage;
import github.clyoudu.taskpipeline.task.ParallelStage;
import github.clyoudu.taskpipeline.task.Stage;
import github.clyoudu.taskpipeline.task.TaskContext;
import org.junit.Test;

public class CompositeTaskTest {

    @Test
    public void test() throws InterruptedException {
        String s = FileUtil.readClasspathFile("/taskpipeline/test.json");
        JSONObject json = JSON.parseObject(s);

        TaskContext taskContext = new TaskContext();
        AbstractStage stage = getStage(json, null, taskContext);
        new Thread(stage::execute).start();

        while (true) {
            System.out.println(
                "Total Progress: " + taskContext.getProgress() + "(" + taskContext.getCurrentProgress() + "/" +
                    taskContext.getTotal() + ")");
            if (taskContext.getCurrentProgress() == taskContext.getTotal() ||
                (taskContext.canceled() && taskContext.getExecutor().isTerminated())) {
                break;
            }
            Thread.sleep(1000L);
        }

    }

    private AbstractStage getStage(JSONObject json, AbstractStage parentStage, TaskContext taskContext) {
        String name = json.getString("name");
        String type = json.getString("type");
        JSONArray stages = json.getJSONArray("stages");
        switch (type) {
            case "linear":
                List<AbstractStage> stageList1 = new ArrayList<>();
                LinearStage linearStage = new LinearStage(taskContext, parentStage, name, stageList1);
                getStages(stages, linearStage, taskContext, stageList1);
                return linearStage;
            case "parallel":
                List<AbstractStage> stageList = new ArrayList<>();
                ParallelStage parallelStage = new ParallelStage(taskContext, parentStage, name, stageList);
                getStages(stages, parallelStage, taskContext, stageList);
                return parallelStage;
            case "single":
                return new Stage(taskContext, parentStage, name);
            default:
                return null;
        }
    }

    private void getStages(JSONArray stages, AbstractStage parentStage, TaskContext taskContext,
        List<AbstractStage> stageList) {
        for (int i = 0; i < stages.size(); i++) {
            JSONObject stage = stages.getJSONObject(i);
            stageList.add(getStage(stage, parentStage, taskContext));
        }
    }

}
