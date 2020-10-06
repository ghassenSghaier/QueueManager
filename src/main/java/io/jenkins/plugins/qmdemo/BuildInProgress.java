package io.jenkins.plugins.qmdemo;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Api;
import hudson.model.Executor;
import hudson.model.InvisibleAction;
import hudson.model.Node;
import hudson.model.RootAction;
import hudson.model.Run;
import hudson.slaves.OfflineCause;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.export.ExportedBean;

/**
 *
 * @author lenovo
 */
@ExportedBean
@Extension
public class BuildInProgress extends InvisibleAction implements RootAction {

    private final static Logger LOGGER = Logger.getLogger(BuildInProgress.class.getName());

    @JavaScriptMethod
    public Collection<String> getAllNodeNames() {
        Collection<String> nodeNames = new ArrayList<>();
        for (Node n : Jenkins.get().getNodes()) {

            nodeNames.add(n.getDisplayName());
        }
        return nodeNames;
    }

    @JavaScriptMethod
    public Collection<BuildInfo> getBuildsInProgress(String nodeName) throws IOException {
        Node n = Jenkins.get().getNode(nodeName);
        SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<BuildInfo> buildsInfos = new ArrayList<>();
        for (Executor e : n.toComputer().getExecutors()) {
            LOGGER.info(e.getCurrentExecutable().getParent().getOwnerTask().getDisplayName());
            if(e.getCurrentExecutable().getParent().getOwnerTask() instanceof WorkflowJob)
            {
                ExecutorStepExecution.PlaceholderTask pht = (ExecutorStepExecution.PlaceholderTask) e.getCurrentExecutable().getParent();
                Run<?,?> item =pht.runForDisplay();
                String startDate = sdfDestination.format(new Date(item.getStartTimeInMillis()));
                String estimatedEndDate = sdfDestination.format(new Date(item.getStartTimeInMillis() + item.getEstimatedDuration()));
                BuildInfo bi = new BuildInfo(item.getParent().getDisplayName(), item.getNumber(), Jenkins.get().getRootUrl() + item.getUrl(), startDate, null, null);
                bi.setEstimatedEndAt(estimatedEndDate);
                bi.setProgress(item.getExecutor().getProgress());
                LOGGER.info(bi.toString());
                buildsInfos.add(bi);
            }
            if(e.getCurrentExecutable().getParent().getOwnerTask() instanceof AbstractProject)
            {
                ExecutorStepExecution.PlaceholderTask pht = (ExecutorStepExecution.PlaceholderTask) e.getCurrentExecutable().getParent();
                Run<?,?> item =pht.runForDisplay();
                String startDate = sdfDestination.format(new Date(item.getStartTimeInMillis()));
                String estimatedEndDate = sdfDestination.format(new Date(item.getStartTimeInMillis() + item.getEstimatedDuration()));
                BuildInfo bi = new BuildInfo(item.getParent().getDisplayName(), item.getNumber(), Jenkins.get().getRootUrl() + item.getUrl(), startDate, null, null);
                bi.setEstimatedEndAt(estimatedEndDate);
                bi.setProgress(item.getExecutor().getProgress());
                LOGGER.info(bi.toString());
                buildsInfos.add(bi);
                
            }
        }
        return buildsInfos;
    }

    @JavaScriptMethod
    public NodeInfo getNodeInfo(String nodeName) {

        NodeInfo nodeInfo = new NodeInfo();
        Node n = Jenkins.get().getNode(nodeName);
        nodeInfo.setNodeName(nodeName);
        if (n.toComputer().isOnline()) {
            nodeInfo.setStatus("online");
        } else {
            nodeInfo.setStatus("offline");
            if (n.toComputer().getOfflineCause() instanceof OfflineCause.UserCause) {
                String username = ((OfflineCause.UserCause) n.toComputer().getOfflineCause()).getUser().getFullName();
                nodeInfo.setOfflineBy(username);
            }
        }
        nodeInfo.setTotalExecutors(n.toComputer().countExecutors());
        nodeInfo.setBusyExecutors(n.toComputer().countBusy());
        nodeInfo.setIdelExecutors(n.toComputer().countIdle());
        return nodeInfo;
    }

    /**
     * REST API.
     *
     * @return new api instance.
     */
    public Api getApi() {
        return new Api(this);
    }

    public Date getBuildTerminatedAt(Run r) {
        return new Date(r.getStartTimeInMillis() + r.getDuration());
    }

    @Override
    public String getUrlName() {
        return getClass().getSimpleName();
    }

}
