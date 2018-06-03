package com.easycsv.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtils {

    public static byte[] compress(String data) throws Exception {
        byte[] compressed;

        try(ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
            GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data.getBytes());
            if(gzip != null) {
                gzip.close();
            }
            compressed = bos.toByteArray();
        } return compressed;
    }

    public static StringBuilder decompress(byte[] compressed) throws Exception {
        StringBuilder sb;
        try(ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
            GZIPInputStream gis = new GZIPInputStream(bis);
            BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"))) {
            sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
        } return sb;
    }
}
