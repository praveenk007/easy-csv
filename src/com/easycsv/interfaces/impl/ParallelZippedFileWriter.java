package com.easycsv.interfaces.impl;

import com.easycsv.interfaces.ICSVFileWriter;
import com.easycsv.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author praveenkamath
 **/
public class ParallelZippedFileWriter implements ICSVFileWriter {

    private int workers;

    private List<Object> objects;

    public ParallelZippedFileWriter(int workers, List<Object> objects) {
        this.workers = workers;
        this.objects = objects;
    }

    @Override
    public void write() {
        int bucketCapacity = getBucketCapacity();
        int size = objects.size();

        if(size % workers != 0) {

        }
        List<Callable<Integer>> calls = new ArrayList<>();


        calls.add(new Task(objects));
        calls.add(new Task(objects));calls.add(new Task(objects));calls.add(new Task(objects));

        ExecutorService executor = Executors.newFixedThreadPool(workers);
        try {
            executor.invokeAll(calls);
            System.out.println("done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(executor != null && !executor.isShutdown()) {
                executor.shutdown();
            }
        }
    }

    private int getBucketCapacity() {
        if(objects == null || objects.isEmpty()) {
            return 0;
        } return objects.size() / workers;
    }

    public static void main(String args[]) {
        List<Object> objects = new ArrayList<>();
        List<Callable<Integer>> calls = new ArrayList<>();
        calls.add(new Task(objects));
        calls.add(new Task(objects));calls.add(new Task(objects));calls.add(new Task(objects));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            List<Future<Integer>> futures = executor.invokeAll(calls);
            System.out.println("all processes completed, analyzing future");
            for(Future<Integer> future: futures) {
                System.out.println(future.isDone());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
