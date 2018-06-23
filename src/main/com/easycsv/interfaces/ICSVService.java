package main.com.easycsv.interfaces;

import java.io.BufferedWriter;
import java.util.List;

/**
 * @author praveenkamath
 **/
public interface ICSVService {

    void writeToFile(List<Object> objects, String path, boolean applyHeader) throws Exception;

    void writeToFile(List<Object> objects, BufferedWriter writer, boolean applyHeader) throws Exception;

    void convertToCsv(List<Object> objects, boolean applyHeader);
}
