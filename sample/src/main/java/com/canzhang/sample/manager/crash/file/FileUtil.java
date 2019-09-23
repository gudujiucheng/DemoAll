package com.canzhang.sample.manager.crash.file;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    //注意，此处的provider名称需要和AndroidManifest.里面的大小写保持一致
    public static final String FILE_PROVIDER = ".FileProvider";
    private final static String TAG = "FileUtil";
    private final static int BUFF_SIZE = 2048;


    /**
     * 获取文件对应的URI
     *
     * @param activity
     * @param file
     * @return
     */
    public static Uri getUri(Activity activity, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //注意，此处的provider名称需要和AndroidManifest.里面的大小写保持一致
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + FILE_PROVIDER,
                    file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 压缩文件
     *
     * @param fs          需要压缩的文件
     * @param zipFilePath 被压缩后存放的路径
     * @return 成功返回 true，否则 false
     */
    public static boolean zipFiles(File fs[], String zipFilePath) {
        if (fs == null) {
            throw new NullPointerException("fs == null");
        }
        boolean result = false;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFilePath)));
            for (File file : fs) {
                if (file == null || !file.exists()) {
                    continue;
                }
                if (file.isDirectory()) {
                    recursionZip(zos, file, file.getName() + File.separator);
                } else {
                    recursionZip(zos, file, "");
                }
            }
            result = true;
            zos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "zip file failed err: " + e.getMessage());
        } finally {
            try {
                if (zos != null) {
                    zos.closeEntry();
                    zos.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception {
        if (file.isDirectory()) {
            Log.i(TAG, "the file is dir name -->>" + file.getName() + " the baseDir-->>>" + baseDir);
            File[] files = file.listFiles();
            for (File fileSec : files) {
                if (fileSec == null) {
                    continue;
                }
                if (fileSec.isDirectory()) {
                    baseDir = file.getName() + File.separator + fileSec.getName() + File.separator;
                    Log.i(TAG, "basDir111-->>" + baseDir);
                    recursionZip(zipOut, fileSec, baseDir);
                } else {
                    Log.i(TAG, "basDir222-->>" + baseDir);
                    recursionZip(zipOut, fileSec, baseDir);
                }
            }
        } else {
            Log.i(TAG, "the file name is -->>" + file.getName() + " the base dir -->>" + baseDir);
            byte[] buf = new byte[BUFF_SIZE];
            InputStream input = new BufferedInputStream(new FileInputStream(file));
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            int len;
            while ((len = input.read(buf)) != -1) {
                zipOut.write(buf, 0, len);
            }
            input.close();
        }
    }
}