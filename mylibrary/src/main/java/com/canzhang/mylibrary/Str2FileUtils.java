package com.canzhang.mylibrary;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Str2FileUtils {
    public Str2FileUtils() {
    }

    public static String readTextFile(File file) {
        if (file != null && file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                char[] buf = new char[1];
                InputStreamReader isr = new InputStreamReader(bis);

                StringBuilder sb;
                String result;
                for(sb = new StringBuilder(); isr.read(buf) != -1; buf = new char[1]) {
                    result = new String(buf);
                    sb.append(result);
                }

                result = sb.toString();
                isr.close();
                fis.close();
                bis.close();
                return result;
            } catch (FileNotFoundException var7) {
                var7.printStackTrace();
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            return null;
        } else {
            return null;
        }
    }

    public static void writeTextFile(File file, String data) throws IOException {
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(data.getBytes());
            bos.flush();
            bos.close();
            out.close();
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        }

    }
}
