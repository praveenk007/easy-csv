package main.com.easycsv.utils;

import java.io.File;
import java.util.Arrays;

/**
 * This utility consists of common functions which may be used during file operation
 *
 * @author praveenkamath
 **/
public class FileUtils {

    /**
     * Creates a directory
     * @param dir
     * @return
     */
    public static boolean mkDir(String dir) {
        return new File(dir).mkdir();
    }

    /**
     * Deletes a dir along with the files inside it
     * @param dir
     * @return
     */
    public static boolean deleteDir(String dir) {
        File file = new File(dir);
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.stream(files).forEach(dirFile -> deleteFile(dirFile));
            file.delete();
            return true;
        } return false;
    }

    /**
     * Deletes a file
     * @param file
     * @return
     */
    public  static boolean deleteFile(File file) {
        return file.delete();
    }
}
