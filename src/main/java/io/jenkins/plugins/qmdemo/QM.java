package io.jenkins.plugins.qmdemo;

import hudson.Extension;
import hudson.model.Api;
import hudson.model.InvisibleAction;
import hudson.model.Queue;
import hudson.model.RootAction;
import hudson.model.Run;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
import jenkins.advancedqueue.sorter.QueueItemCache;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.export.Exported;

@Extension
public class QM extends InvisibleAction implements RootAction {

    private final static Logger LOGGER = Logger.getLogger(QM.class.getName());

    @JavaScriptMethod
    @Exported
    public Collection<BuildInfo> getQueueItems() {

        Collection<BuildInfo> queueItems = new ArrayList<>();
        for (Queue.BuildableItem item : Jenkins.get().getQueue().getBuildableItems()) {
            if (item.task.getOwnerTask() instanceof WorkflowJob) {
                LOGGER.info("Logged");
                ExecutorStepExecution.PlaceholderTask pht = (ExecutorStepExecution.PlaceholderTask) item.task;
                Run<?, ?> qi = pht.runForDisplay();
                Date launchDate = new Date(qi.getTimeInMillis());
                queueItems.add(new BuildInfo(qi.getParent().getDisplayName(), qi.getNumber(), Jenkins.get().getRootUrl() + qi.getUrl(), launchDate.toString(), item.getId()));
            }
        }
        return queueItems;

    }

    @JavaScriptMethod
    public void reScheduleQueue(long queueId1, long queueId2, String operation) {
        LOGGER.info(operation);
        Queue.Item item1 = Jenkins.get().getQueue().getItem(queueId1);
        Queue.Item item2 = Jenkins.get().getQueue().getItem(queueId2);
        float weight1 = QueueItemCache.get().getItem(item1.getId()).getWeight();
        float weight2 = QueueItemCache.get().getItem(item2.getId()).getWeight();
        if (operation.equals("up")) {
            QueueItemCache.get().getItem(item2.getId()).setWeightSelection(weight1);
            QueueItemCache.get().getItem(item1.getId()).setWeightSelection(weight2);
        }
        if (operation.equals("down")) {
            QueueItemCache.get().getItem(item2.getId()).setWeightSelection(weight1);
            QueueItemCache.get().getItem(item1.getId()).setWeightSelection(weight2);
        }
        Jenkins.get().getQueue().maintain();
    }

    public Api getApi() {
        return new Api(this);
    }

    @Override
    public String getUrlName() {
        return getClass().getSimpleName();
    }

}
