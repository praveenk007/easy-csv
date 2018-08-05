package com.easycsv.utils;

import com.easycsv.annotations.CSVHeaderPosition;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Reflection based field utility. Can be used to process class fields.
 *
 * @author praveenkamath
 **/
public class FieldUtils {

    /**
     * Converts array of primitive data-types into CSV string
     * @param field
     * @param object
     * @param delimiter
     * @return
     */
    public static String convertArrayPrimitiveToCsv(Field field, Object object, String delimiter) {
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            Object arrayObj = field.get(object);
            for(int index = 0; index < Array.getLength(arrayObj); index++) {
                sb.append(Array.get(arrayObj, index)).append(delimiter);
            } return  sb.toString().substring(0, sb.length() - 1);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Surrounds a field value with specified qualifier
     * @param csv field value
     * @param qualifier
     * @return
     */
    public static String surroundFieldValWithStringQualifier(String csv, String qualifier) {
        if(csv == null) {
            csv = "-";
        }
        return qualifier + csv + qualifier;
    }

    /**
     * Sorts all fields based on position specified in field annotation
     * @param fields
     */
    public static void sortFields(Field[] fields) {
        Arrays.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field f1, Field f2) {
                CSVHeaderPosition or1 = f1.getAnnotation(CSVHeaderPosition.class);
                CSVHeaderPosition or2 = f2.getAnnotation(CSVHeaderPosition.class);
                if (or1 != null && or2 != null) {
                    return or1.value() - or2.value();
                } else if (or1 != null && or2 == null) {
                    return -1;
                } else if (or1 == null && or2 != null) {
                    return 1;
                }
                return 0;
            }
        });
    }
}
