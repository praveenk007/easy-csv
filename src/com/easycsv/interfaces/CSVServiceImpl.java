package com.easycsv.interfaces;

import com.easycsv.services.ICSVService;
import com.easycsv.utils.CSVWriteUtils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

/**
 * @author praveenkamath
 **/
public class CSVServiceImpl implements ICSVService {

    @Override
    public void writeToFile(List<Object> objects, String path, boolean applyHeader) throws Exception {
        CSVWriteUtils csvWriteUtils = new CSVWriteUtils(",");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            csvWriteUtils.write(objects, true, writer);
        }
    }

    @Override
    public void convertToCsv(List<Object> objects, boolean applyHeader) {

    }
}
