/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : JobInformation.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import io.swagger.annotations.ApiModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description of Job
 */
@ApiModel()
public class JobInformation {

    private static final SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd hh:mm:ss");

    public static enum JobStatus {
        UNKNOWN, QUEUED, PROCESSING, FINISHED, FAILED;

        public boolean in(JobStatus... status) {
            for (JobStatus s : status) {
                if (this == s) {
                    return true;
                }
            }
            return false;
        }
    }

    public static final class StatusChange {

        public String changeTime;
        public JobInformation.JobStatus oldStatus;
        public JobInformation.JobStatus newStatus;
        public String changeInformation;

        public StatusChange(JobStatus oldStatus, JobStatus newStatus, String changeInformation) {
            this.oldStatus = oldStatus;
            this.newStatus = newStatus;
            this.changeInformation = changeInformation;
            this.changeTime = fd.format(new Date());
        }

        public String getChangeTime() {
            return changeTime;
        }

        @Override
        public String toString() {
            return "StatusChange{" + "changeTime=" + changeTime + ", oldStatus=" + oldStatus + ", newStatus=" + newStatus + ", changeInformation='"
                + changeInformation + '\'' + '}';
        }
    }

    private final String uuid;

    private String startTime;
    private String endTime;
    private JobStatus status = JobStatus.UNKNOWN;

    private List<StatusChange> statusChangeHistory = new ArrayList<>();
    private Object payload;
    private Object result;

    public JobInformation() {
        this.uuid = UUID.randomUUID().toString();
    }

    public JobInformation(String uuid) {
        this.uuid = uuid;
        startTime = fd.format(new Date());
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = fd.format(endTime);
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.setStatus(status, "Setting Status");
    }

    public void setStatus(JobStatus status, String message) {
        this.getStatusMessageCollector().add(new StatusChange(this.status, status, message));
        this.status = status;
    }

    public List<StatusChange> getStatusMessageCollector() {
        return statusChangeHistory;
    }

    public void setStatusMessageCollector(List<StatusChange> statusMessageCollector) {
        this.statusChangeHistory = statusMessageCollector;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getUuid() {
        return uuid;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<StatusChange> getStatusChangeHistory() {
        return statusChangeHistory;
    }

}

