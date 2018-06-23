package main.com.easycsv.abstracts;

import main.com.easycsv.interfaces.ICSVFileWriter;
import main.com.easycsv.models.Result;
import main.com.easycsv.models.Results;

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
