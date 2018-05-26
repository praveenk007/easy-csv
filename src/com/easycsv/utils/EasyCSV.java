package com.easycsv.utils;

import com.easycsv.annotations.CSVHeader;
import com.easycsv.annotations.CSVHeaderPosition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class EasyCSV {

    private String del;

    private static final String NEW_LINE    =   "\n";

    public EasyCSV(String del) {
        this.del = del;
    }

    public String write(List<Object> objs, boolean applyHeader) {
        Class clazz = objs.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        sortFields(fields);
        StringBuilder sb = new StringBuilder();
        if(applyHeader) {
            String header = getHeader(fields);
            sb.append(header).append(NEW_LINE);
        }
        sb.append(convertToCsv(objs, fields, false));
        return Base64.getEncoder().encodeToString(sb.toString().getBytes());

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
        return Arrays.stream(fields).map(f -> getFieldVal(f, obj)).collect(Collectors.joining(del));
    }

    private String getFeildValWithQualifier(String csv) {
        return "\"" + csv +"\"";
    }

    private String getHeader(Field[] fields) {
        StringBuilder sb = new StringBuilder();
        addHeader(sb, fields, true);
        return sb.substring(0, sb.length() - 2);
    }

    private void addHeader(StringBuilder sb, Field[] fields, boolean isSuperClassFields) {
        Arrays.toString(fields);
        if(!isSuperClassFields) {
            //Sort nested object fields
            sortFields(fields);
        }
        for(Field field: fields) {
            if(field.getType() == List.class) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                Class clazz = (Class) type.getActualTypeArguments()[0];
                if(clazz.getName().startsWith("java.lang")) {
                    sb.append(field.getAnnotation(CSVHeader.class).value()).append(del);
                } else {
                    //List<CustomObject>
                    addHeader(sb, clazz.getDeclaredFields(), false);
                }
                Field[] childObjectFields = ((Class)clazz).getDeclaredFields();
            } else if(field.getType().isPrimitive() || field.getType().getName().startsWith("java.lang")) {
                sb.append(field.getAnnotation(CSVHeader.class).value()).append(del);
            } else {
                //Custom java object
                addHeader(sb, ((Class) field.getType()).getDeclaredFields(), false);
            }
        }
    }

    private String getFieldVal(Field field, Object obj) {
        try {
            field.setAccessible(true);
            if(field.getType() == List.class) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                Type clazz = type.getActualTypeArguments()[0];
                if(type != null) {
                    if(String.class == clazz) { //List<String>
                        return  getFeildValWithQualifier(((List<String>) field.get(obj)).stream().collect(Collectors.joining(del)));
                    } else if(Number.class == ((Class)clazz).getSuperclass()) { //List<Number>
                        return  getFeildValWithQualifier(((List<Number>) field.get(obj)).stream().map(val -> String.valueOf(val)).collect(Collectors.joining(del)));
                    } else { //List<CustomObject>
                        Field[] fields = ((Class)clazz).getDeclaredFields();
                        sortFields(fields);
                        return convertToCsv(((List<Object>) field.get(obj)), fields, true).toString();
                    }
                }
            } else if(field.getType().isPrimitive() || field.getType().getName().startsWith("java.lang")) {
                return field.get(obj).toString();
            } else {
                //custom
                Field[] fields = ((Class)field.getType()).getDeclaredFields();
                sortFields(fields);
                return convertToCsv(((Object) field.get(obj)), fields, true).toString();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
    private static void sortFields(Field[] fields) {
        Arrays.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field f1, Field f2) {
                CSVHeaderPosition or1 = f1.getAnnotation(CSVHeaderPosition.class);
                CSVHeaderPosition or2 = f2.getAnnotation(CSVHeaderPosition.class);
                if (or1 != null && or2 != null) {
                    return or1.value() - or2.value();
                } else
                if (or1 != null && or2 == null) {
                    return -1;
                } else
                if (or1 == null && or2 != null) {
                    return 1;
                }
                return 0;
            }
        });
    }
}

