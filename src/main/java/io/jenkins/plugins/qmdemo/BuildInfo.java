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

/**
 *
 * @author lenovo
 */
public class BuildInfo {

    private String jobName;
    private int number;
    private String buildUrl;
    private String scheduledAt;
    private long queueId;
    private String startedAt;
    private String endedAt;
    private String estimatedEndAt;
    private int progress;
    private String result;

    public BuildInfo(String jobName, int number, String buildUrl, String startedAt, String endedAt, String result) {
        this.jobName = jobName;
        this.number = number;
        this.buildUrl = buildUrl;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.result = result;
    }

    public BuildInfo(String jobName, int number, String buildUrl, String scheduledAt, long queueId) {
        this.jobName = jobName;
        this.number = number;
        this.buildUrl = buildUrl;
        this.scheduledAt = scheduledAt;
        this.queueId = queueId;
    }
    
    

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
    }

    public String getEstimatedEndAt() {
        return estimatedEndAt;
    }

    public void setEstimatedEndAt(String estimatedEndAt) {
        this.estimatedEndAt = estimatedEndAt;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }

//    @Override
//    public String toString() {
//        return "BuildInfo{" + "jobName=" + jobName + ", number=" + number + ", buildUrl=" + buildUrl + ", startedAt=" + startedAt + ", endedAt=" + endedAt + ", result=" + result + '}';
//    }
    @Override
    public String toString() {
        return "BuildInfo{" + "jobName=" + jobName + ", number=" + number + ", buildUrl=" + buildUrl + ", startedAt=" + startedAt + ", estimatedEndAt=" + estimatedEndAt + ", progress=" + progress + '}';
    }

}
