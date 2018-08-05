package com.easycsv.abstracts;

import com.easycsv.interfaces.ICSVFileWriter;
import com.easycsv.models.Result;
import com.easycsv.models.Results;
import java.io.BufferedWriter;

/**
 * @author praveenkamath
 **/
public abstract class AbstractBaseWriter implements ICSVFileWriter {

    @Override
    public Result createOneThenZip(BufferedWriter writer) {
        return null;
    }

    @Override
    public Results createManyThenZip(int buckets, String directory) {
        return null;
    }
}
