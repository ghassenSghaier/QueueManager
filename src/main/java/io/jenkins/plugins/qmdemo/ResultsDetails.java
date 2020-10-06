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

import java.util.Date;

/**
 *
 * @author lenovo
 */
public class ResultsDetails {

    private Date date;
    private int nbBuilds;
    private long totalDuration;
    private String result;

    public ResultsDetails() {
    }
    

    public ResultsDetails(Date date, int nbBuilds, long totalDuration, String result) {
        this.date = date;
        this.nbBuilds = nbBuilds;
        this.totalDuration = totalDuration;
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNbBuilds() {
        return nbBuilds;
    }

    public void setNbBuilds(int nbBuilds) {
        this.nbBuilds = nbBuilds;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultsDetails{" + "date=" + date + ", nbBuilds=" + nbBuilds + ", totalDuration=" + totalDuration + ", result=" + result + '}';
    }
    
    

}
