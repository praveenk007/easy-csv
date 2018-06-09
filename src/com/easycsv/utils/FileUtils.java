package com.easycsv.utils;

import java.io.File;

/**
 * @author praveenkamath
 **/
public class FileUtils {

    public static boolean mkDir(String dir) {
        return new File(dir).mkdir();
    }
}
