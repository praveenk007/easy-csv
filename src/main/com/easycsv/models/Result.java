package main.com.easycsv.models;

import java.util.List;

/**
 * @author praveenkamath
 **/
public class Result {

    public Result() {
    }

    public Result(List<TaskMeta> taskMetas, int status, String message, String zipPath) {
        this.taskMetas = taskMetas;
        this.status = status;
        this.message = message;
        this.zipPath = zipPath;
    }

    private List<TaskMeta> taskMetas;

    private int     status;

    private String  message;

    private String  zipPath;

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

    public List<TaskMeta> getTaskMetas() {
        return taskMetas;
    }

    public void setTaskMetas(List<TaskMeta> taskMetas) {
        this.taskMetas = taskMetas;
    }

    @Override
    public String toString() {
        return "Result{" +
                "taskMetas=" + taskMetas +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", zipPath='" + zipPath + '\'' +
                '}';
    }
}
