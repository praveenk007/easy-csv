package main.com.easycsv.interfaces.impl;

import main.com.easycsv.abstracts.AbstractBaseWriter;
import main.com.easycsv.interfaces.ICSVService;
import main.com.easycsv.models.Result;
import main.com.easycsv.models.Results;
import main.com.easycsv.models.TaskMeta;

import java.io.BufferedWriter;
import java.util.List;

/**
 * @author praveenkamath
 **/
public class ZippedFileWriter extends AbstractBaseWriter {

    private List<Object>    objects;

    private boolean         applyHeader;

    /**
     *
     * @param objects       List of records to print
     * @param applyHeader   denotes whether to apply header or not
     */
    public ZippedFileWriter(List<Object> objects, boolean applyHeader) {
        this.objects        =   objects;
        this.applyHeader    =   applyHeader;
    }

    @Override
    public Result createOneThenZip(BufferedWriter writer) {
        ICSVService csvService = new CSVServiceImpl();
        TaskMeta taskMeta;
        try {
            csvService.writeToFile(objects, writer, applyHeader);
            taskMeta = new TaskMeta(objects.size(), 200, "Success", null);
            return new Result(taskMeta, 200, "Success", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(null, 500, "Failed! "+e.getMessage(), null);
        }
    }
}
