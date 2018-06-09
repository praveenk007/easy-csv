package com.easycsv.utils;

import com.easycsv.annotations.CSVHeader;
import com.easycsv.annotations.CSVHeaderPosition;
import com.easycsv.annotations.CSVProperties;
import com.easycsv.constants.Constants;

import java.io.BufferedWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author praveenkamath
 **/
public class CSVWriteUtils {

    private String del;

    private static String DEFAULT_VAL       =   "";

    public CSVWriteUtils(String del) {
        this.del = del;
    }

    public void write(List<Object> objs, boolean applyHeader, BufferedWriter writer) throws  Exception {
        Class clazz = objs.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        CSVProperties csvProperties = (CSVProperties) clazz.getAnnotation(CSVProperties.class);
        if(csvProperties != null) {
            DEFAULT_VAL = csvProperties.defaultValue();
        }

        FieldUtils.sortFields(fields);
        if(applyHeader) {
            writer.write(getHeader(fields) + Constants.NEW_LINE);
        }
        convertToCsv(writer, objs, fields, false);
    }

    private StringBuilder convertToCsv(Object obj, Field[] fields, boolean isNestedObject) {
        StringBuilder sb = new StringBuilder();
        appendDataToCsv(sb, obj, fields, isNestedObject);
        return sb;
    }

    private void convertToCsv(BufferedWriter writer, List<Object> objs, Field[] fields, boolean isNestedObject) {
        for(Object obj: objs) {
            appendDataToCsv(writer, obj, fields, isNestedObject);
        }
    }


    private void appendDataToCsv(StringBuilder sb, Object obj, Field[] fields, boolean isNestedObject) {
        try {
            sb.append(stringifyObjectFields(obj, fields));
            if(!isNestedObject)
                sb.append(Constants.NEW_LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendDataToCsv(BufferedWriter writer, Object obj, Field[] fields, boolean isNestedObject) {
        try {
            writer.write(stringifyObjectFields(obj, fields));
            if(!isNestedObject)
                writer.write(Constants.NEW_LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String stringifyObjectFields(Object obj, Field[] fields) {
        return Arrays.stream(fields)
                .filter(f -> f.getAnnotation(CSVHeader.class) != null ||
                        f.getAnnotation(CSVHeaderPosition.class) != null)
                .map(f -> getFieldVal(f, obj))
                .collect(Collectors.joining(del));
    }

    private String getHeader(Field[] fields) {
        StringBuilder sb = new StringBuilder();
        addHeader(sb, fields, true);
        return sb.substring(0, sb.length() - 1);
    }

    private void addHeader(StringBuilder sb, Field[] fields, boolean isSuperClassFields) {
        if(!isSuperClassFields) {
            //Sort nested object fields
            FieldUtils.sortFields(fields);
        }
        for(Field field: fields) {
            Class type= field.getType();
            if(List.class == type) {
                ParameterizedType pType = (ParameterizedType) field.getGenericType();
                Class clazz = (Class) pType.getActualTypeArguments()[0];
                if(clazz.getName().startsWith("java.lang")) {
                    CSVHeader csvHeader = field.getAnnotation(CSVHeader.class);
                    if(csvHeader == null) {
                        continue;
                    }
                    sb.append(csvHeader.value()).append(del);
                } else {
                    //List<CustomObject>
                    addHeader(sb, clazz.getDeclaredFields(), false);
                }
            } else if(field.getType().isPrimitive() || type.getName().startsWith("java.lang") || (type.isArray() && (type.getComponentType().isPrimitive() || Number.class == type.getComponentType().getSuperclass() || String.class == type.getComponentType()))) {
                CSVHeader csvHeader = field.getAnnotation(CSVHeader.class);
                if(csvHeader == null) {
                    continue;
                }
                sb.append(csvHeader.value()).append(del);
            } else if(type.isArray() && !type.getComponentType().isPrimitive()) {
                //CustomObject[]
                addHeader(sb, type.getComponentType().getDeclaredFields(), false);
            } else {
                //CustomObject
                addHeader(sb, field.getType().getDeclaredFields(), false);
            }
        }
    }

    private String getFieldVal(Field field, Object obj) {

        try {
            field.setAccessible(true);
            if(field.getType() == List.class) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                if(type != null) {
                    Type clazz = type.getActualTypeArguments()[0];
                    if(String.class == clazz) { //List<String>
                        return  FieldUtils.surroundFieldValWithStringQualifier(((List<String>) field.get(obj)).stream().collect(Collectors.joining(del)), "\"");
                    } else if(Number.class == ((Class)clazz).getSuperclass()) { //List<Number>
                        return  FieldUtils.surroundFieldValWithStringQualifier(((List<Number>) field.get(obj)).stream().map(val -> String.valueOf(val)).collect(Collectors.joining(del)), "\"");
                    } else { //List<CustomObject>
                        Field[] fields = ((Class)clazz).getDeclaredFields();
                        FieldUtils.sortFields(fields);
                        return convertToCsv(((List<Object>) field.get(obj)).get(0), fields, true).toString();
                    }
                }
            } else if(field.getType().isArray()) {
                Class clazz = field.getType().getComponentType();
                if(clazz.isPrimitive() || String.class == clazz || Number.class == clazz.getSuperclass()) {
                    return FieldUtils.surroundFieldValWithStringQualifier(FieldUtils.convertArrayPrimitiveToCsv(field, obj, del), "\"");
                } else {
                    //CustomObject[]
                    Field[] fields = ((Class)clazz).getDeclaredFields();
                    FieldUtils.sortFields(fields);
                    return convertToCsv(Array.get(field.get(obj), 0), fields, true).toString();
                }
            } else if(field.getType().isPrimitive() || field.getType().getName().startsWith("java.lang")) {
                if(obj == null || field.get(obj) == null) {
                    return DEFAULT_VAL;
                }
                return field.get(obj).toString();
            } else {
                //custom
                Field[] fields = field.getType().getDeclaredFields();
                FieldUtils.sortFields(fields);
                return convertToCsv(field.get(obj), fields, true).toString();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
