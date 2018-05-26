package utils;

import annotations.CSVHeader;
import annotations.CSVHeaderPosition;

import java.lang.reflect.Array;
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
        System.out.println(Arrays.toString(fields));
        sortFields(fields);
        StringBuilder sb = new StringBuilder();
        if(applyHeader) {
            addHeader(sb, fields);
        }
        sb.append(appendToSb(objs, fields, false));
        return Base64.getEncoder().encodeToString(sb.toString().getBytes());

    }

    private StringBuilder appendToSb(List<Object> objs, Field[] fields, boolean isNestedObject) {
        StringBuilder sb = new StringBuilder();
        for(Object obj: objs) {
            try {
                sb.append(stringifyObjectFields(obj, fields));
                if(!isNestedObject)
                    sb.append(NEW_LINE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb;
    }

    private String stringifyObjectFields(Object obj, Field[] fields) {
        return Arrays.stream(fields).map(f -> getFieldVal(f, obj)).collect(Collectors.joining(del));
    }

    private String getFeildValWithQualifier(String csv) {
        return "\"" + csv +"\"";
    }

    private void addHeader(StringBuilder sb, Field[] fields) {
        sb.append(Arrays.stream(fields).map(f -> f.getAnnotation(CSVHeader.class).value()).collect(Collectors.joining(del))).append("\n");
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
                        return appendToSb(((List<Object>) field.get(obj)), fields, true).toString();
                    }
                }
            } return field.get(obj).toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void sortFields(Field[] fields) {
        System.out.println("Sorting ...");
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


