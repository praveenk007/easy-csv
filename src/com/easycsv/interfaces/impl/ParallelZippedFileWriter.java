package com.easycsv.interfaces.impl;

import com.easycsv.interfaces.ICSVFileWriter;
import com.easycsv.tasks.Task;
import com.easycsv.tests.Member;

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

    private int buckets;

    private List<Object> objects;

    public ParallelZippedFileWriter(int buckets, List<Object> objects) {
        this.buckets = buckets;
        this.objects = objects;
    }

    @Override
    public void write() {
        int bucketCapacity = getBucketCapacity();
        System.out.println("Bucket cap :: "+bucketCapacity);
        int size = objects.size();
        List<Callable<Integer>> calls = new ArrayList<>();
        for(int index = 0; index < buckets; index ++) {
            if(index == buckets-1 && size % buckets > 0) { //&& size % buckets < bucketCapacity / 2) {
                calls.add(new Task(objects.subList(index * bucketCapacity, size)));
            } else {
                calls.add(new Task(objects.subList(index * bucketCapacity, (index + 1) * bucketCapacity)));
            }
        }
        ExecutorService executor = Executors.newFixedThreadPool(buckets);
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
        } return (objects.size() / buckets);
    }

    public static void main(String args[]) {
        List<Object> objects = new ArrayList<>();
        for(int index = 0; index < 12213; index++) {
            Member mem = new Member();
            mem.setFname("pk"+index);
            objects.add(mem);
        }
        ParallelZippedFileWriter z = new ParallelZippedFileWriter(10, objects);
        z.write();
    }
}
