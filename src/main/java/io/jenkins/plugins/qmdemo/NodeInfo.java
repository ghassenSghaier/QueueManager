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

import java.util.List;

/**
 *
 * @author lenovo
 */
public class NodeInfo {

    private String nodeName;
    private String status;
    private String offlineBy;
    private int nbBuilds;
    private int totalExecutors;
    private int busyExecutors;
    private int idelExecutors;    
    private List<ResultsDetails> resultsDetails;    
    

    public NodeInfo() {
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfflineBy() {
        return offlineBy;
    }

    public void setOfflineBy(String offlineBy) {
        this.offlineBy = offlineBy;
    }

    public int getNbBuilds() {
        return nbBuilds;
    }

    public void setNbBuilds(int nbBuilds) {
        this.nbBuilds = nbBuilds;
    }

    public List<ResultsDetails> getResultsDetails() {
        return resultsDetails;
    }

    public void setResultsDetails(List<ResultsDetails> resultsDetails) {
        this.resultsDetails = resultsDetails;
    }    

    public int getTotalExecutors() {
        return totalExecutors;
    }

    public void setTotalExecutors(int totalExecutors) {
        this.totalExecutors = totalExecutors;
    }

    public int getBusyExecutors() {
        return busyExecutors;
    }

    public void setBusyExecutors(int busyExecutors) {
        this.busyExecutors = busyExecutors;
    }

    public int getIdelExecutors() {
        return idelExecutors;
    }

    public void setIdelExecutors(int idelExecutors) {
        this.idelExecutors = idelExecutors;
    }
    
    
    

}
