package com.easycsv.models;

import java.util.List;

/**
 * @author praveenkamath
 **/
public class Result {

    private TaskMeta taskMeta;

    private int     status;

    private String  message;

    private String  zipPath;

    public Result(TaskMeta taskMeta, int status, String message, String zipPath) {
        this.taskMeta   = taskMeta;
        this.status     = status;
        this.message    = message;
        this.zipPath    = zipPath;
    }

    public TaskMeta getTaskMeta() {
        return taskMeta;
    }

    public void setTaskMeta(TaskMeta taskMeta) {
        this.taskMeta = taskMeta;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }
}
