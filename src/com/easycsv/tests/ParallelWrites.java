package com.easycsv.tests;

import com.easycsv.interfaces.impl.ParallelZippedFileWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author praveenkamath
 **/
public class ParallelWrites {

    public static void main(String args[]) {
        List<Object> objects = new ArrayList<>();
        for(int index = 0; index < 12213; index++) {
            Member mem = new Member();
            mem.setFname("pk"+index);
            objects.add(mem);
        }
        String dir = "/Users/praveenkamath/Documents/testcsv";
        ParallelZippedFileWriter z = new ParallelZippedFileWriter(13, objects, dir, true);
        long start = System.currentTimeMillis();
        System.out.println(z.writeAndZip());
        System.out.println("Total time : " + (System.currentTimeMillis() - start) );
    }
}
