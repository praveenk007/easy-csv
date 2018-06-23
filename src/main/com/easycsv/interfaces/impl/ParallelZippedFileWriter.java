package main.com.easycsv.interfaces.impl;

import main.com.easycsv.constants.Constants;
import main.com.easycsv.enums.FileFormatEnum;
import main.com.easycsv.interfaces.ICSVFileWriter;
import main.com.easycsv.models.Result;
import main.com.easycsv.models.TaskMeta;
import main.com.easycsv.tasks.Task;
import main.com.easycsv.utils.FileUtils;
import main.com.easycsv.utils.RandomIdGenerator;
import main.com.easycsv.utils.ZipUtils;
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

    private List<Object>    objects;

    private boolean         applyHeader;

    /**
     *
     * @param objects       List of records to print
     * @param applyHeader   denotes whether to apply header or not
     */
    public ParallelZippedFileWriter(List<Object> objects, boolean applyHeader) {
        this.objects        =   objects;
        this.applyHeader    =   applyHeader;
    }

    @Override
    public Result splitAndZip(int buckets, String dir) {
        int bucketCapacity = getBucketCapacity(buckets);
        int size = objects.size();
        String id = RandomIdGenerator.randomAlphaNumeric(5);
        String zipPath  =   dir + File.separator + id + Constants.DOT + FileFormatEnum.zip;
        String tempDir = dir + File.separator + id;
        FileUtils.mkDir(tempDir);
        ExecutorService executor = Executors.newFixedThreadPool(buckets);
        try {
            List<Future<TaskMeta>> futures = executor.invokeAll(createTasks(tempDir, size, bucketCapacity, buckets));
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
    private List<Callable<TaskMeta>> createTasks(String tempDir, int totalRecords, int bucketCapacity, int buckets) {
        List<Callable<TaskMeta>> calls = new ArrayList<>();
        IntStream.range(0, buckets).forEach(index -> {
            String path = tempDir + File.separator + "bucket-" + (index+1) + Constants.DOT + FileFormatEnum.csv;
             calls.add(index == buckets - 1 && totalRecords % buckets > 0 ?
                     new Task(objects.subList(index * bucketCapacity, totalRecords), path, applyHeader) :
                     new Task(objects.subList(index * bucketCapacity, (index + 1) * bucketCapacity), path, applyHeader)
            );
        }); return calls;
    }

    /**
     * Calculates capacity of a bucket/file
     * @return
     */
    private int getBucketCapacity(int buckets) {
        return objects == null || objects.isEmpty() ? 0 : objects.size() / buckets;
    }
}
