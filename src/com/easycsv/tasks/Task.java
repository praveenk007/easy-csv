package com.easycsv.tasks;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author praveenkamath
 **/
public class Task implements Callable<Integer> {

    private List<Object> objs;

    public Task(List<Object> objs) {
        this.objs = objs;
    }

    @Override
    public Integer call() throws Exception {
        if (Thread.currentThread().getName().equalsIgnoreCase("pool-1-thread-2")) {
            Thread.sleep(10000);
        } else {
            Thread.sleep(4000);
        }
        System.out.println("Thread executing :: " + Thread.currentThread().getName() + ", work :: " + objs.size() + ", " +objs.get(0) + " " + objs.get(objs.size()-1));
        return 123;
    }
}
