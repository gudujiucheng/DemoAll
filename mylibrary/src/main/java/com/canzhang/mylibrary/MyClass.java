package com.canzhang.mylibrary;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class MyClass {
    public MyClass() {
    }

    public static void main(String[] argus) {
        System.out.println("start---------->>>>>>");
        try {
            writeContent(readFileContent("./Input.txt"));
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void writeContent(String data) throws Exception {
        String decryptedData = null;
        System.out.println("input = " + data);
        DESKeySpec dks = null;

        try {
            dks = new DESKeySpec("&F%R@A#$".getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(2, secretKey);
            byte[] result = cipher.doFinal(Base64.getMimeDecoder().decode(data));
            System.out.println("output = " + new String(result));
            FileOutputStream os = new FileOutputStream(new File("./Output.txt"));
            ByteArrayInputStream in = new ByteArrayInputStream(result);

            try {
                GZIPInputStream ungzip = new GZIPInputStream(in);
                byte[] buffer = new byte[256];

                int n;
                while ((n = ungzip.read(buffer)) >= 0) {
                    os.write(buffer, 0, n);
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            os.close();
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        return file.exists() ? Str2FileUtils.readTextFile(file) : null;
    }
}
