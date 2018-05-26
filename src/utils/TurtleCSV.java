package utils;

import annotations.CSVHeader;
import annotations.CSVHeaderPosition;
import tests.Member;
import tests.PolicyDetails;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class TurtleCSV {

    private String del;

    private static final String NEW_LINE    =   "\n";

    public TurtleCSV(String del) {
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
        return appendToSb(objs, sb, fields);

    }

    private String appendToSb(List<Object> objs, StringBuilder sb, Field[] fields) {
        for(Object obj: objs) {
            try {
                sb.append(Arrays.stream(fields).map(f -> "\"" + getFieldVal(f, obj, sb) +"\"").collect(Collectors.joining(del)));
                sb.append(NEW_LINE);
            } catch (Exception e) {

            }
        } return Base64.getEncoder().encodeToString(sb.toString().getBytes());
    }

    private void addHeader(StringBuilder sb, Field[] fields) {
        sb.append(Arrays.stream(fields).map(f -> f.getAnnotation(CSVHeader.class).value()).collect(Collectors.joining(del))).append("\n");
    }

    private String getFieldVal(Field field, Object obj, StringBuilder sb) {
        try {
            if(field.getType() == List.class) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                Type clazz =type.getActualTypeArguments()[0];
                //System.out.println(((Class)clazz).getSuperclass());
                if(type != null) {
                    if(String.class == clazz) { //List<String>
                        return  ((List<String>) field.get(obj)).stream().collect(Collectors.joining(del));
                    } else if(Number.class == ((Class)clazz).getSuperclass()) { //List<Number>
                        return  ((List<Number>) field.get(obj)).stream().map(val -> String.valueOf(val)).collect(Collectors.joining(del));
                    } else { //Direct  or in-direct child of Object
                        List<Object> nestedObject = ((List<Object>) field.get(obj));
                        Class nestedObjClazz = nestedObject.get(0).getClass();
                        Field[] fields = nestedObjClazz.getDeclaredFields();
                        appendToSb(nestedObject, sb, fields);
                    }
                }
            }
            field.setAccessible(true);
            return field.get(obj).toString();
        } catch(Exception e) {
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

    public static void main(String args[]) {
        TurtleCSV t = new TurtleCSV(",");

        PolicyDetails pd1 = new PolicyDetails();
        pd1.setfName("praveen");
        pd1.setlName("kamath");
        pd1.setPolicyNum("12349494");
        pd1.setAge(20);
        List<String> previousPolicies = new ArrayList<>();
        previousPolicies.add("pol1");
        previousPolicies.add("pol2");
        previousPolicies.add("pol3");
        previousPolicies.add("pol4");
        pd1.setPreviousPolicies(previousPolicies);
        List<Integer> ages = new ArrayList<>();
        ages.add(10);
        ages.add(20);
        ages.add(30);
        Member m1 = new Member();
        m1.setFname("praveen");
        m1.setLname("kamath");
        List<Member> members = new ArrayList();
        pd1.setMemebers(members);
        pd1.setAges(ages);

        List<Double> policyPremiums = new ArrayList<>();
        policyPremiums.add(10.0);
        policyPremiums.add(20.1);
        policyPremiums.add(30.2);
        pd1.setPolicyPremiums(policyPremiums);

        PolicyDetails pd2 = new PolicyDetails();
        pd2.setfName("Beer");
        pd2.setlName("Pratap");
        pd2.setPolicyNum("47474747");
        pd2.setAge(23);
        List<String> previousPolicies2 = new ArrayList<>();
        previousPolicies2.add("pol1");
        previousPolicies2.add("pol2");
        previousPolicies2.add("pol3");
        previousPolicies2.add("pol4");
        pd2.setPreviousPolicies(previousPolicies2);


        List list = new ArrayList();
        list.add(pd1);
        //list.add(pd2);
        byte[] b = Base64.getDecoder().decode(t.write(list, true));
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File("/Users/praveenkamath/Documents/test.csv"));
            fw.write(new String(b));
            fw.flush();
            System.out.println("File written");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(new String(b));
    }
}


