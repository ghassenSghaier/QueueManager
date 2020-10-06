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
public class JobExecutionInfos {

    private int nbSuccessfulBuilds;
    private int nbFailedBuilds;
    private int nbAbortedBuilds;
    private int nbUnstableBuilds;
    private List<ResultsDetails> resultsDetails;

    public JobExecutionInfos() {
    }

    public JobExecutionInfos(int nbSuccessfulBuilds, int nbFailedBuilds, int nbAbortedBuilds, int nbUnstableBuilds, List<ResultsDetails> resultsDetails) {
        this.nbSuccessfulBuilds = nbSuccessfulBuilds;
        this.nbFailedBuilds = nbFailedBuilds;
        this.nbAbortedBuilds = nbAbortedBuilds;
        this.nbUnstableBuilds = nbUnstableBuilds;
        this.resultsDetails = resultsDetails;
    }

    public int getNbSuccessfulBuilds() {
        return nbSuccessfulBuilds;
    }

    public void setNbSuccessfulBuilds(int nbSuccessfulBuilds) {
        this.nbSuccessfulBuilds = nbSuccessfulBuilds;
    }

    public int getNbFailedBuilds() {
        return nbFailedBuilds;
    }

    public void setNbFailedBuilds(int nbFailedBuilds) {
        this.nbFailedBuilds = nbFailedBuilds;
    }

    public int getNbAbortedBuilds() {
        return nbAbortedBuilds;
    }

    public void setNbAbortedBuilds(int nbAbortedBuilds) {
        this.nbAbortedBuilds = nbAbortedBuilds;
    }

    public int getNbUnstableBuilds() {
        return nbUnstableBuilds;
    }

    public void setNbUnstableBuilds(int nbUnstableBuilds) {
        this.nbUnstableBuilds = nbUnstableBuilds;
    }

    public List<ResultsDetails> getResultsDetails() {
        return resultsDetails;
    }

    public void setResultsDetails(List<ResultsDetails> resultsDetails) {
        this.resultsDetails = resultsDetails;
    }

}
