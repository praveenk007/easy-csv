package main.com.easycsv.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public static void zipThem(List<String> filePaths, String zipPath) {
        try(FileOutputStream fos = new FileOutputStream(zipPath);ZipOutputStream zos = new ZipOutputStream(fos) ) {
            filePaths.forEach(filePath -> {
                try {
                    zos.putNextEntry(new ZipEntry(new File(filePath).getName()));
                    byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
