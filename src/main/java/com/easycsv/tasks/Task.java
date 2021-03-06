package com.easycsv.tasks;

import com.easycsv.interfaces.impl.CSVServiceImpl;
import com.easycsv.models.TaskMeta;
import com.easycsv.interfaces.ICSVService;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * This class
 *
 * @author praveenkamath
 **/
public class Task implements Callable<TaskMeta> {

    private List<Object>    objs;

    private String          path;

    private boolean         applyHeader;

    private ICSVService     csvService;

    public Task(List<Object> objs, String path, boolean applyHeader) {
        this.objs           =   objs;
        this.path           =   path;
        this.applyHeader    =   applyHeader;
        this.csvService     =   new CSVServiceImpl();
    }

    @Override
    public TaskMeta call() {
        try {
            csvService.writeToFile(objs, path, applyHeader);
            return new TaskMeta(objs.size(), 200, null, path);
        } catch (Exception e) {
            return new TaskMeta(0, 500, e.getMessage(), null);
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "objs=" + objs +
                ", path='" + path + '\'' +
                ", applyHeader=" + applyHeader +
                ", csvService=" + csvService +
                '}';
    }
}
