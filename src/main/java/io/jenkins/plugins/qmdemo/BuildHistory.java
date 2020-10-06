/*
 * The MIT License
 *
 * Copyright 2020 lenovo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.jenkins.plugins.qmdemo;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Api;
import hudson.model.InvisibleAction;
import hudson.model.Job;
import hudson.model.Node;
import hudson.model.RootAction;
import hudson.model.Run;
import hudson.slaves.OfflineCause;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.actions.WorkspaceAction;
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.graph.FlowGraphWalker;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.export.ExportedBean;
/**
 *
 * @author lenovo
 */
@ExportedBean
@Extension
public class BuildHistory extends InvisibleAction implements RootAction {

    private final static Logger LOGGER = Logger.getLogger(BuildHistory.class.getName());

    @JavaScriptMethod
    public Collection<String> getAllNodeNames() {
        Collection<String> nodeNames = new ArrayList<>();
        for (Node n : Jenkins.get().getNodes()) {
            nodeNames.add(n.getDisplayName());
        }
        return nodeNames;
    }

    @JavaScriptMethod
    public Collection<BuildInfo> getBuildHistory(String nodeName) {
        Node n = Jenkins.get().getNode(nodeName);
        SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<BuildInfo> buildsInfos = new ArrayList<>();
        for (Job j : Jenkins.get().getAllItems(Job.class)) {
            if (j instanceof WorkflowJob) {
                for (Iterator<WorkflowRun> run = ((WorkflowJob) j).getBuilds().iterator(); run.hasNext();) {
                    WorkflowRun item = run.next();
                    if (!item.hasntStartedYet() && item.getResult() != null && !item.isBuilding()) {
                        System.out.println(item.getDisplayName());
                        FlowExecution exec = item.getExecution();
                        if (exec == null) {
                            System.out.print("null");
                        }
                        if (exec != null) {
                            if (exec.isComplete()) {
//                        if (exec == null) {
//                            continue;
//                        }
                                FlowGraphWalker w = new FlowGraphWalker(exec);
                                boolean test = false;
                                for (FlowNode f : w) {
                                    if (f instanceof StepStartNode) {
                                        WorkspaceAction action = f.getAction(WorkspaceAction.class);
                                        if (action != null) {
                                            String node = action.getNode();
                                            String workspace = action.getPath();
                                            if (node.equals(n.getNodeName())) {
                                                test = true;
                                            }
                                        }
                                    }
                                }
                                if (test) {

                                    String startDate = sdfDestination.format(new Date(item.getStartTimeInMillis()));
                                    String endDate = sdfDestination.format(new Date(item.getStartTimeInMillis() + item.getDuration()));
                                    buildsInfos.add(new BuildInfo(j.getDisplayName(), item.getNumber(), Jenkins.get().getRootUrl() +item.getUrl(), startDate, endDate, item.getResult().toString()));
                                }

                            }
                        }
                    }
                }
            }
            if (j instanceof AbstractProject) {
                for (Iterator<Run> run = j.getBuilds().node(n).iterator(); run.hasNext();) {
                    Run item = run.next();
                    if (!item.hasntStartedYet() && item.getResult() != null && !item.isBuilding()) {
                        String startDate = sdfDestination.format(new Date(item.getStartTimeInMillis() * 1000));
                        String endDate = sdfDestination.format(new Date(item.getStartTimeInMillis() + item.getDuration()));
                        buildsInfos.add(new BuildInfo(j.getDisplayName(), item.getNumber(), item.getBuildStatusUrl(), startDate, endDate, item.getResult().toString()));

                    }
                }
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
