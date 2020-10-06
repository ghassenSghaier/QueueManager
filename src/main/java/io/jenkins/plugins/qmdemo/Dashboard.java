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
import hudson.model.Job;
import hudson.model.Node;
import hudson.model.Result;
import hudson.model.RootAction;
import hudson.model.Run;
import hudson.slaves.OfflineCause;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.actions.WorkspaceAction;
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.graph.FlowGraphWalker;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jfree.util.Log;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 *
 * @author lenovo
 */
@Extension
@ExportedBean
public class Dashboard implements RootAction {

    private final static Logger LOGGER = Logger.getLogger(Dashboard.class.getName());

    @JavaScriptMethod
    @Exported
    public List<NodeInfo> getNodesInfos() {
        List<NodeInfo> availabilityList = new ArrayList<>();
        for (Node n : Jenkins.get().getNodes()) {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setNodeName(n.getNodeName());
            if (n.toComputer().isOnline()) {
                nodeInfo.setStatus("online");
            } else {
                nodeInfo.setStatus("offline");
                if (n.toComputer().getOfflineCause() instanceof OfflineCause.UserCause) {
                    String username = ((OfflineCause.UserCause) n.toComputer().getOfflineCause()).getUser().getFullName();
                    nodeInfo.setOfflineBy(username);
                }
            }
            nodeInfo.setNbBuilds(Jenkins.get().getQueue().getBuildableItems(n.toComputer()).size());
            availabilityList.add(nodeInfo);
        }

        return availabilityList;
    }

    /**
     * REST API.
     *
     * @return new api instance.
     */
    public Api getApi() {
        return new Api(this);
    }

    public static Date getDateWithoutTimeUsingFormat(Date date)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        return formatter.parse(formatter.format(date));
    }

    public List<ResultsDetails> getResultsDetails(String result, Map<Date, Integer> a, Map<Date, Long> a1) {
        List<ResultsDetails> rds = new ArrayList<>();
        for (Map.Entry<Date, Integer> entry : a.entrySet()) {
            Date d = entry.getKey();
            rds.add(new ResultsDetails(d, entry.getValue(), a1.get(d), result));
        }
        return rds;
    }
    @JavaScriptMethod
    public Collection<String> getAllJobNames()
    {
        return Jenkins.get().getJobNames();
    }
    @JavaScriptMethod
    public JobExecutionInfos getJobExecutionInfos(String nodeName, @NotNull String jobName, @NotNull String stDate, @NotNull String enDate) throws ParseException {
        LOGGER.info("Hello");
        Node n = Jenkins.get().getNode(nodeName);
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(stDate);
        System.out.println("Date: " + startDate.getTime());
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(enDate);
        System.out.println("Date: " + endDate.getTime());
        Job<?, ?> j = Jenkins.get().getItemByFullName(jobName, Job.class);
        Logger.getLogger("Logger").info(j.getName());
        JobExecutionInfos jobExecutionsInfos = new JobExecutionInfos();
        int nbFBuilds = 0;
        int nbSBuilds = 0;
        int nbABuilds = 0;
        int nbUBuilds = 0;
        /**
         * ****************************************************
         */
        Map<Date, Integer> nbFailedBuilds = new HashMap<>();
        Map<Date, Integer> nbSucceededBuilds = new HashMap<>();
        Map<Date, Integer> nbAbortedBuilds = new HashMap<>();
        Map<Date, Integer> nbUnstableBuilds = new HashMap<>();
        /**
         * ****************************************************
         */
        Map<Date, Long> failedBuildsDuration = new HashMap<>();
        Map<Date, Long> successfulBuildsDuration = new HashMap<>();
        Map<Date, Long> abortedBuildsDuration = new HashMap<>();
        Map<Date, Long> unstableBuildsDuration = new HashMap<>();
        /**
         * ****************************************************
         */
        if (j instanceof WorkflowJob) {
            LOGGER.info("WorkflowJob");
            for (Iterator<WorkflowRun> run = ((WorkflowJob) j).getBuilds().byTimestamp(startDate.getTime(), endDate.getTime()).iterator(); run.hasNext();) {
                LOGGER.info("logged to for ");
                WorkflowRun item = run.next();
                Log.info(item.getDisplayName());
                if (item.getResult() != null) {
                    Date date = getDateWithoutTimeUsingFormat(new Date(item.getStartTimeInMillis()));
                    //System.out.println(item.getTimestamp());
                    FlowExecution exec = item.getExecution();
                    if (exec == null) {
                        System.out.print("null");
                    }
                    if (exec != null) {
//                        if (exec.isComplete()) {
                        if (!item.hasntStartedYet() && !item.isBuilding() && item.getResult() != null) {
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

                                Log.info(test);
                                if (test) {
                                    LOGGER.info("Logged to test");
                                    if (item.getResult().equals(Result.FAILURE)) {
                                        nbFBuilds++;
                                        if (nbFailedBuilds.containsKey(date)) {
                                            nbFailedBuilds.put(date, nbFailedBuilds.get(date) + 1);
                                        } else {
                                            nbFailedBuilds.put(date, 1);
                                        }
                                        if (failedBuildsDuration.containsKey(date)) {
                                            failedBuildsDuration.put(date, failedBuildsDuration.get(date) + item.getDuration());
                                        } else {
                                            failedBuildsDuration.put(date, item.getDuration());
                                        }

                                    }
                                    if (item.getResult().equals(Result.SUCCESS)) {
                                        nbSBuilds++;
                                        if (nbSucceededBuilds.containsKey(date)) {
                                            nbSucceededBuilds.put(date, nbSucceededBuilds.get(date) + 1);
                                        } else {
                                            nbSucceededBuilds.put(date, 1);
                                        }
                                        if (successfulBuildsDuration.containsKey(date)) {
                                            successfulBuildsDuration.put(date, successfulBuildsDuration.get(date) + item.getDuration());
                                        } else {
                                            successfulBuildsDuration.put(date, item.getDuration());
                                        }
                                    }
                                    if (item.getResult().equals(Result.ABORTED)) {
                                        nbABuilds++;
                                        if (nbAbortedBuilds.containsKey(date)) {
                                            nbAbortedBuilds.put(date, nbAbortedBuilds.get(date) + 1);
                                        } else {
                                            nbAbortedBuilds.put(date, 1);
                                        }
                                        if (abortedBuildsDuration.containsKey(date)) {
                                            abortedBuildsDuration.put(date, abortedBuildsDuration.get(date) + item.getDuration());
                                        } else {
                                            abortedBuildsDuration.put(date, item.getDuration());
                                        }
                                    }
                                    if (item.getResult().equals(Result.UNSTABLE)) {
                                        nbUBuilds++;
                                        if (nbUnstableBuilds.containsKey(date)) {
                                            nbUnstableBuilds.put(date, nbUnstableBuilds.get(date) + 1);
                                        } else {
                                            nbUnstableBuilds.put(date, 1);
                                        }
                                        if (unstableBuildsDuration.containsKey(date)) {
                                            unstableBuildsDuration.put(date, unstableBuildsDuration.get(date) + item.getDuration());
                                        } else {
                                            unstableBuildsDuration.put(date, item.getDuration());
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        if (j instanceof AbstractProject) {
            for (Iterator<Run> run = ((AbstractProject) j).getBuilds().byTimestamp(startDate.getTime(), endDate.getTime()).node(n).iterator(); run.hasNext();) {
                Run item = run.next();
                Date date = getDateWithoutTimeUsingFormat(new Date(item.getStartTimeInMillis()));
                if (item.getResult() != null && !item.isBuilding() && !item.hasntStartedYet()) {

                    if (item.getResult().equals(Result.FAILURE)) {
                        nbFBuilds++;
                        if (nbFailedBuilds.containsKey(date)) {
                            nbFailedBuilds.put(date, nbFailedBuilds.get(date) + 1);
                        } else {
                            nbFailedBuilds.put(date, 1);
                        }
                        if (failedBuildsDuration.containsKey(date)) {
                            failedBuildsDuration.put(date, failedBuildsDuration.get(date) + item.getDuration());
                        } else {
                            failedBuildsDuration.put(date, item.getDuration());
                        }

                    }
                    if (item.getResult().equals(Result.SUCCESS)) {
                        nbSBuilds++;
                        if (nbSucceededBuilds.containsKey(date)) {
                            nbSucceededBuilds.put(date, nbSucceededBuilds.get(date) + 1);
                        } else {
                            nbSucceededBuilds.put(date, 1);
                        }
                        if (successfulBuildsDuration.containsKey(date)) {
                            successfulBuildsDuration.put(date, successfulBuildsDuration.get(date) + item.getDuration());
                        } else {
                            successfulBuildsDuration.put(date, item.getDuration());
                        }
                    }
                    if (item.getResult().equals(Result.ABORTED)) {
                        nbABuilds++;
                        if (nbAbortedBuilds.containsKey(date)) {
                            nbAbortedBuilds.put(date, nbAbortedBuilds.get(date) + 1);
                        } else {
                            nbAbortedBuilds.put(date, 1);
                        }
                        if (abortedBuildsDuration.containsKey(date)) {
                            abortedBuildsDuration.put(date, abortedBuildsDuration.get(date) + item.getDuration());
                        } else {
                            abortedBuildsDuration.put(date, item.getDuration());
                        }
                    }
                    if (item.getResult().equals(Result.UNSTABLE)) {
                        nbUBuilds++;
                        if (nbUnstableBuilds.containsKey(date)) {
                            nbUnstableBuilds.put(date, nbUnstableBuilds.get(date) + 1);
                        } else {
                            nbUnstableBuilds.put(date, 1);
                        }
                        if (unstableBuildsDuration.containsKey(date)) {
                            unstableBuildsDuration.put(date, unstableBuildsDuration.get(date) + item.getDuration());
                        } else {
                            unstableBuildsDuration.put(date, item.getDuration());
                        }
                    }

                }
            }
        }
        jobExecutionsInfos.setNbFailedBuilds(nbFBuilds);
        jobExecutionsInfos.setNbSuccessfulBuilds(nbSBuilds);
        jobExecutionsInfos.setNbAbortedBuilds(nbABuilds);
        jobExecutionsInfos.setNbUnstableBuilds(nbUBuilds);

        List<ResultsDetails> a1 = getResultsDetails("failed", nbFailedBuilds, failedBuildsDuration);
        List<ResultsDetails> a2 = getResultsDetails("success", nbSucceededBuilds, successfulBuildsDuration);
        List<ResultsDetails> a3 = getResultsDetails("aborted", nbAbortedBuilds, abortedBuildsDuration);
        List<ResultsDetails> a4 = getResultsDetails("unstable", nbUnstableBuilds, unstableBuildsDuration);

        List<ResultsDetails> listC = new ArrayList<>();
        listC.addAll(a1);
        listC.addAll(a2);
        listC.addAll(a3);
        listC.addAll(a4);
        jobExecutionsInfos.setResultsDetails(listC);
        LOGGER.info(String.valueOf(jobExecutionsInfos.getNbSuccessfulBuilds()));
        LOGGER.info(String.valueOf(jobExecutionsInfos.getNbFailedBuilds()));
        LOGGER.info(String.valueOf(jobExecutionsInfos.getNbAbortedBuilds()));
        LOGGER.info(String.valueOf(jobExecutionsInfos.getNbUnstableBuilds()));
        for (ResultsDetails r : listC) {
            LOGGER.info(r.toString());
        }
        return jobExecutionsInfos;
    }

    public Jenkins get() {
        return Jenkins.get();
    }

    @Override
    public String getIconFileName() {
        return "gear.png";
    }

    @Override
    public String getUrlName() {
        return getClass().getSimpleName();
    }
    /**
     * Default display name.
     *
     * @return
     */
    @Override
    public String getDisplayName() {
        return "QM";
    }

}
