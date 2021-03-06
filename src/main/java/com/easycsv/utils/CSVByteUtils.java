package com.easycsv.utils;

import com.easycsv.annotations.CSVHeader;
import com.easycsv.annotations.CSVHeaderPosition;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class CSVByteUtils {

    private String del;

    private static final String NEW_LINE    =   "\n";

    public CSVByteUtils(String del) {
        this.del = del;
    }

    public byte[] write(List<Object> objs, boolean applyHeader) throws  Exception {
        Class clazz = objs.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        FieldUtils.sortFields(fields);
        StringBuilder sb = new StringBuilder();
        if(applyHeader) {
            String header = getHeader(fields);
            sb.append(header).append(NEW_LINE);
        }
        sb.append(convertToCsv(objs, fields, false));
        return ZipUtils.compress(Base64.getEncoder().encodeToString(sb.toString().getBytes()));
    }

    private StringBuilder convertToCsv(Object obj, Field[] fields, boolean isNestedObject) {
        StringBuilder sb = new StringBuilder();
        appendDataToCsv(sb, obj, fields, isNestedObject);
        return sb;
    }

    private StringBuilder convertToCsv(List<Object> objs, Field[] fields, boolean isNestedObject) {
        StringBuilder sb = new StringBuilder();
        for(Object obj: objs) {
            appendDataToCsv(sb, obj, fields, isNestedObject);
        }
        return sb;
    }


    private void appendDataToCsv(StringBuilder sb, Object obj, Field[] fields, boolean isNestedObject) {
        try {
            sb.append(stringifyObjectFields(obj, fields));
            if(!isNestedObject)
                sb.append(NEW_LINE);
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
                        return convertToCsv(((List<Object>) field.get(obj)), fields, true).toString();
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


