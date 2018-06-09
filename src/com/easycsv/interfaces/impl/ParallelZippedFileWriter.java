package com.easycsv.interfaces.impl;

import com.easycsv.interfaces.ICSVFileWriter;
import com.easycsv.tasks.Task;
import com.easycsv.tests.Member;
import com.easycsv.utils.FileUtils;
import com.easycsv.utils.RandomIdGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This implementation divides list of objects into blocks.
 * Each block is processed and written into a file asynchronously, thus, speeding up the entire process
 * Number of blocks to create is passed by the client
 *
 * @author praveenkamath
 **/
public class ParallelZippedFileWriter implements ICSVFileWriter {

    private int             buckets;

    private List<Object>    objects;

    private String          dir;

    private boolean         applyHeader;

    public ParallelZippedFileWriter(int buckets, List<Object> objects, String dir, boolean applyHeader) {
        this.buckets        =   buckets;
        this.objects        =   objects;
        this.dir            =   dir;
        this.applyHeader    =   applyHeader;
    }

    @Override
    public void write() {
        int bucketCapacity = getBucketCapacity();
        System.out.println("Bucket cap :: "+bucketCapacity);
        int size = objects.size();
        List<Callable<Integer>> calls = new ArrayList<>();
        String tempDir = dir + File.separator + RandomIdGenerator.randomAlphaNumeric(5);
        boolean isDirCreated = FileUtils.mkDir(tempDir);
        if(!isDirCreated) {
            FileUtils.mkDir(dir);
        }
        for(int index = 0; index < buckets; index ++) {
            String path = tempDir + File.separator + "bucket-" + (index+1) + ".csv";
            if(index == buckets-1 && size % buckets > 0) { //&& size % buckets < bucketCapacity / 2) {
                calls.add(new Task(objects.subList(index * bucketCapacity, size), path, applyHeader));
            } else {
                calls.add(new Task(objects.subList(index * bucketCapacity, (index + 1) * bucketCapacity), path, applyHeader));
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
        String dir = "/Users/praveenkamath/Documents/testcsv";
        ParallelZippedFileWriter z = new ParallelZippedFileWriter(13, objects, dir, true);
        long start = System.currentTimeMillis();
        z.write();
        System.out.println("Total time : " + (System.currentTimeMillis() - start) );
    }
}
