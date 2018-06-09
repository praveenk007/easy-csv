package com.easycsv.tasks;

import com.easycsv.interfaces.CSVServiceImpl;
import com.easycsv.services.ICSVService;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author praveenkamath
 **/
public class Task implements Callable<Integer> {

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
    public Integer call() throws Exception {
        //System.out.println("[call] Thread :: "+ Thread.currentThread().getName());
        csvService.writeToFile(objs, path, applyHeader);
        return 123;
    }
}
