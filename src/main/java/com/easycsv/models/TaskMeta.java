package com.easycsv.models;

/**
 * @author praveenkamath
 **/
public class TaskMeta {

    public TaskMeta() {
    }

    public TaskMeta(int records, int status, String message, String filePath) {
        this.records = records;
        this.status = status;
        this.message = message;
        this.filePath = filePath;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private int     records;

    private int     status;

    private String  message;

    private String  filePath;

    @Override
    public String toString() {
        return "TaskMeta{" +
                "records=" + records +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
