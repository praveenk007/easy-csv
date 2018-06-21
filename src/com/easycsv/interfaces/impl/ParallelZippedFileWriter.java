package com.easycsv.interfaces.impl;

import com.easycsv.constants.Constants;
import com.easycsv.enums.FileFormatEnum;
import com.easycsv.interfaces.ICSVFileWriter;
import com.easycsv.models.Result;
import com.easycsv.models.TaskMeta;
import com.easycsv.tasks.Task;
import com.easycsv.utils.FileUtils;
import com.easycsv.utils.RandomIdGenerator;
import com.easycsv.utils.ZipUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /**
     *
     * @param buckets       number of files
     * @param objects       List of records to print
     * @param dir           root directory to write temp files and zip file
     * @param applyHeader   denotes whether to apply header or not
     */
    public ParallelZippedFileWriter(int buckets, List<Object> objects, String dir, boolean applyHeader) {
        this.buckets        =   buckets;
        this.objects        =   objects;
        this.dir            =   dir;
        this.applyHeader    =   applyHeader;
    }

    @Override
    public Result writeAndZip() {
        int bucketCapacity = getBucketCapacity();
        int size = objects.size();
        String id = RandomIdGenerator.randomAlphaNumeric(5);
        String zipPath  =   dir + File.separator + id + ".zip";
        String tempDir = dir + File.separator + id;
        FileUtils.mkDir(tempDir);
        ExecutorService executor = Executors.newFixedThreadPool(buckets);
        try {
            List<Future<TaskMeta>> futures = executor.invokeAll(createTasks(tempDir, size, bucketCapacity));
            List<TaskMeta> taskMetas = getResultsFromFutures(futures);
            zip(taskMetas, zipPath);
            return new Result(taskMetas, 200, null, zipPath);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Result(null, 500, e.getMessage(), null);
        } finally {
            if(executor != null && !executor.isShutdown()) {
                executor.shutdown();
            }
            System.out.println(FileUtils.deleteDir(tempDir));
        }
    }

    /**
     * Collects result of all async tasks
     * @param futures
     * @return List of task metas
     */
    private List<TaskMeta> getResultsFromFutures(List<Future<TaskMeta>> futures) {
        return futures.stream().map(f-> {
            try {
                return f.get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        })  .filter(taskMeta -> taskMeta != null)
            .collect(Collectors.toList());
    }

    /**
     * Zips all buckets/files
     * @param results results of all async tasks
     * @param zipPath path to create zip file
     */
    private void zip(List<TaskMeta> results, String zipPath) {
        List<String> paths = results.stream()
                .filter(result -> result.getStatus() == 200)
                .map(result -> result.getFilePath())
                .collect(Collectors.toList());
        ZipUtils.zipThem(paths, zipPath);
    }

    /**
     * Creates a list of async tasks
     * @param tempDir
     * @param totalRecords
     * @param bucketCapacity
     * @return
     */
    private List<Callable<TaskMeta>> createTasks(String tempDir, int totalRecords, int bucketCapacity) {
        List<Callable<TaskMeta>> calls = new ArrayList<>();
        IntStream.range(0, buckets).forEach(index -> {
            String path = tempDir + File.separator + "bucket-" + (index+1) + Constants.DOT + FileFormatEnum.csv;
            if(index == buckets - 1 && totalRecords % buckets > 0) {
                calls.add(new Task(objects.subList(index * bucketCapacity, totalRecords), path, applyHeader));
            } else {
                calls.add(new Task(objects.subList(index * bucketCapacity, (index + 1) * bucketCapacity), path, applyHeader));
            }
        }); return calls;
    }

    /**
     * Calculates capacity of a bucket/file
     * @return
     */
    private int getBucketCapacity() {
        return objects == null || objects.isEmpty() ? 0 : objects.size() / buckets;
    }
}
