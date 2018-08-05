package com.easycsv.interfaces.impl;

import com.easycsv.interfaces.ICSVService;
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
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            write(objects, writer, applyHeader);
        }
    }

    @Override
    public void writeToFile(List<Object> objects, BufferedWriter writer, boolean applyHeader) throws Exception {
        write(objects, writer, applyHeader);
    }

    @Override
    public void convertToCsv(List<Object> objects, boolean applyHeader) {

    }

    private void write(List<Object> objects, BufferedWriter writer, boolean applyHeader) throws Exception {
        if(writer != null) {
            CSVWriteUtils csvWriteUtils = new CSVWriteUtils(",");
            csvWriteUtils.write(objects, applyHeader, writer);
        }
        if(writer != null) {
            writer.flush();
        }
    }
}
