package com.easycsv.interfaces;

import com.easycsv.models.Result;
import com.easycsv.models.Results;
import java.io.BufferedWriter;

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
    Results createManyThenZip(int buckets, String directory);


    Result createOneThenZip(BufferedWriter writer);
}
