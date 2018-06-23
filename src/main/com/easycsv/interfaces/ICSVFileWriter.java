package main.com.easycsv.interfaces;

import main.com.easycsv.models.Result;

/**
 * A contract which defines different file write implementations
 *
 * @author praveenkamath
 **/
public interface ICSVFileWriter {

    /**
     * Creates files and zips them
     * @return
     */
    Result splitAndZip(int buckets, String directory);
}
