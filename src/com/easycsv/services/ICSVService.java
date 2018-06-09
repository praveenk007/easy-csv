package com.easycsv.services;

import java.util.List;

/**
 * @author praveenkamath
 **/
public interface ICSVService {

    void writeToFile(List<Object> objects, String path, boolean applyHeader) throws Exception;

    void convertToCsv(List<Object> objects, boolean applyHeader);
}
