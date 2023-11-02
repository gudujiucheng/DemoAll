package com.canzhang.sample.manager.android_11;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * IO 操作工具
 */
public class FileUtil {

    public static final String LOCAL_SCRIPT_DIR = Environment.getExternalStorageDirectory()
            + "/Tencent/SmartMonkey/LocalScript/";
    /**
     * DEFAULT FILE BUFFER
     */
    private static final int DEFAULT_FILE_BUFFER_SIZE = 1024;

    /**
     * DEFAULT ZIP FILE BUFFER
     */
    private static final int DEFAULT_ZIP_BUFFER_SIZE = 5 * 1024;

    /**
     * 写文件
     *
     * @param is
     * @param file
     */
    public static void writeFile(InputStream is, File file) {
        FileOutputStream fos = null;
        if (file.getParent() != null) {
            ensureDir(file.getParent());
        }
        try {
            fos = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            Log.e("writeFile", e.toString());
        } finally {
            safeClose(fos);
            safeClose(is);
        }
    }


    /**
     * 写文件， 老式IO方式
     *
     * @param file    文件
     * @param message 写入信息
     * @return 是否成功
     */
    public static boolean writeFile(File file, String message, boolean append) {
        if (TextUtils.isEmpty(message)) {
            return false;
        }
        if (file.getParent() != null) {
            ensureDir(file.getParent());
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("createFile error: ", "", e);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        FileOutputStream fout = null;
        boolean result = false;
        try {
            fout = new FileOutputStream(file, append);
            fout.write(message.getBytes());
            result = true;
        } catch (Exception e) {
            Log.e("writeFile ", "", e);
            throw new RuntimeException(e);
        } finally {
            safeClose(fout);
        }
        return result;
    }

    /**
     * 写文件
     *
     * @param fileName
     * @param message
     * @return true表示写成功，false反之
     */
    public static boolean writeFile(String fileName, String message) {
        return writeFile(new File(fileName), message, false);
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取Asset目录文件
     *
     * @param context
     * @param fileName
     * @return 返回AssetsFile
     */
    public static String readAssetsFile(Context context, String fileName) {
        //使用和读取缓存一样的大小来处理
        return readAssetsFile(context, fileName, DEFAULT_FILE_BUFFER_SIZE);
    }

    /**
     * 读文件， 老式IO方式
     *
     * @param fileName     文件名
     * @param initCapacity 读取缓存大小
     * @return 文件内容
     */
    public static String readAssetsFile(Context context, String fileName, int initCapacity) {
        InputStream fis = null;
        ByteArrayOutputStream bos = null;
        String result = null;
        try {
            AssetManager assetManager = context.getAssets();
            fis = assetManager.open(fileName);
            byte[] bytes = new byte[1024];
            bos = new ByteArrayOutputStream(initCapacity);
            int len;
            while ((len = fis.read(bytes)) > 0) {
                bos.write(bytes, 0, len);
            }
            result = new String(bos.toByteArray());
        } catch (Exception e) {
            Log.e("readFile", "", e);
        } finally {
            safeClose(bos);
            safeClose(fis);
        }

        return result;
    }


    public static String readFile(String fileName) {
        //使用和读取缓存一样的大小来处理
        return readFile(fileName, DEFAULT_FILE_BUFFER_SIZE);
    }

    /**
     * 读文件， 老式IO方式
     *
     * @param fileName     文件名
     * @param initCapacity 读取缓存大小
     * @return 文件内容
     */
    public static String readFile(String fileName, int initCapacity) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        String result = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            bos = new ByteArrayOutputStream(initCapacity);
            int len;
            while ((len = fis.read(bytes)) > 0) {
                bos.write(bytes, 0, len);
            }
            result = new String(bos.toByteArray());
            //            Log.d("readFile:" + result);
        } catch (Exception e) {
            Log.e("readFile", "", e);
        } finally {
            safeClose(bos);
            safeClose(fis);
        }

        return result;
    }

    /**
     * 查找当前的目录里面是否有有效的文件
     *
     * @param path 文件路径
     * @return 是否存在非0的文件
     */
    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File rootFile = new File(path);
        if (!rootFile.exists()) {
            return false;
        }

        if (rootFile.isFile()) {
            return rootFile.length() > 0;
        }

        if (rootFile.listFiles() == null) {
            return false;
        }

        return isFileExistInternal(Arrays.asList(rootFile.listFiles()));
    }

    //尾递归寻找有效文件
    private static boolean isFileExistInternal(List<File> files) {
        if (files == null || files.size() <= 0) {
            return false;
        }

        List<File> dirList = new ArrayList<>();
        for (File file : files) {
            if (file == null) {
                continue;
            }

            if (file.isDirectory()) {
                Collections.addAll(dirList, file.listFiles());
            } else {
                if (file.length() > 0) {
                    return true;
                }
            }
        }

        return isFileExistInternal(dirList);
    }

    /**
     * 确认路径是否存在
     *
     * @param dirPath
     * @return
     */
    public static boolean ensureDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }

        File dirFile = new File(dirPath);
        return dirFile.exists() && dirFile.isDirectory() || (!dirFile.exists() || dirFile.delete()) && dirFile.mkdirs();
    }


    /**
     * 压缩文件为压缩包
     *
     * @param source      源文件
     * @param zipFilePath 被压缩成的文件名
     * @return 返回压缩后的文件
     */
    public static File zipFile(File source, String zipFilePath) {
        if (source == null || TextUtils.isEmpty(zipFilePath)) {
            return null;
        }

        File zipFile = new File(zipFilePath);
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile),
                    DEFAULT_ZIP_BUFFER_SIZE));
            zipFile(source, zipOut);
        } catch (Exception e) {
            zipFile = null;
        } finally {
            FileUtil.safeClose(zipOut);
        }

        return zipFile;
    }

    /**
     * 迭代，有性能风险，注意！,不可嵌套多层
     *
     * @param resFile
     * @param zipOut
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void zipFile(File resFile, ZipOutputStream zipOut) throws IOException {
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipOut);
            }
        } else {
            zipOneFile(resFile, zipOut);
        }
    }

    /**
     * 压缩一个文件
     *
     * @param resFile
     * @param zipOut
     * @throws IOException
     */
    private static void zipOneFile(File resFile, ZipOutputStream zipOut) throws IOException {
        byte[] buffer = new byte[DEFAULT_FILE_BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
                DEFAULT_FILE_BUFFER_SIZE);
        zipOut.putNextEntry(new ZipEntry(resFile.getName()));
        int realLength;
        while ((realLength = in.read(buffer)) != -1) {
            zipOut.write(buffer, 0, realLength);
        }
        in.close();
        zipOut.flush();
        zipOut.closeEntry();
    }

    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     * @return
     */
    public static boolean delete(File file) {
        return delete(file, null);
    }

    public static boolean delete(File file, String filterFile) {
        // 增加判null处理
        if (file == null) {
            return true;
        }

        if (file.isDirectory()) {
            String[] children = file.list();
            if (children == null) {
                return false;
            }
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = delete(new File(file, children[i]), filterFile);
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        if (file.exists() && (filterFile == null || !file.getPath().contains(filterFile))) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 文件不存在则创建
     *
     * @param path
     * @return
     */
    public static boolean createFile(String path) {
        try {
            File file = new File(path);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除最早的文件
     *
     * @param dir
     * @param maxCount
     */
    public static void deleteOldestFileInPath(File dir, int maxCount) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files.length >= maxCount) {
            File oldest = null;
            for (File file : files) {
                if (oldest == null) {
                    oldest = file;
                } else if (oldest.lastModified() > file.lastModified()) {
                    oldest = file;
                }
            }

            if (oldest != null) {
                oldest.delete();
            }
        }
    }

    /**
     * inputStream转Byte
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] inputStreamToByte(InputStream inputStream) throws Exception {
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        return bytes;
    }

    /**
     * inputStream转String
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String inputStreamToString(InputStream inputStream) throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String str = result.toString("UTF-8");
        return str;
    }

    public static File tryGetAvailableFeedbackFile(String folderPath, String fileName,
                                                   String suffix) {
        return FileUtil.ensureDir(folderPath) ? new File(folderPath + fileName + "." + suffix) :
                null;
    }

    /**
     * copy file
     *
     * @param src  source file
     * @param dest target file
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            createParentDir(dest.getPath());
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 创建 dir 目录
     *
     * @param dirPath dir 目录
     */
    public static File mkdirs(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }

    /**
     * 创建指定文件（目录）的父目录
     */
    public static File createParentDir(String path) {
        File directory = new File(path).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }

    //将Assets中的文件拷贝到path中，只支持单个文件
    public static void copyFileFromAsset(Context context, String srcFileName, String destPath) throws IOException {
        InputStream is = context.getAssets().open(srcFileName);
        File file = new File(destPath);

        //判断父目录是否存在，不存在则创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(destPath);
        byte[] buffer = new byte[1024];
        int byteCount = 0;
        // 循环从输入流读取 buffer字节
        while ((byteCount = is.read(buffer)) != -1) {
            // 将读取的输入流写入到输出流
            fos.write(buffer, 0, byteCount);
        }
        fos.flush();//刷新缓冲区
        fos.getFD().sync();
    }

    //-----------------------------兼容android高版本的api，使用最新的分区存储方案
    public static void saveFile(Context context, String relativePath, String content) {
        //使用分区存储
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveFileUsingMediaStore(context, relativePath, content);
        } else {
            saveFileUsingEnvironment(relativePath, content);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void saveFileUsingMediaStore(Context context, String fileName, String content) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

        if (uri != null) {
            try (OutputStream outputStream = contentResolver.openOutputStream(uri)) {
                if (outputStream != null) {
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("saveFileUsingMediaStore fail,the uri is null ");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String readFileUsingMediaStore(Context context, String fileName) {
        String selection = MediaStore.Downloads.DISPLAY_NAME + "=?";
        String[] selectionArgs = new String[]{fileName};
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID);
            long id = cursor.getLong(idColumn);
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id);

            try (InputStream inputStream = contentResolver.openInputStream(contentUri)) {
                return readFromInputStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }

        }
        return null;
    }

    private static void saveFileUsingEnvironment(String fileName, String content) {
        File file = new File(Environment.getExternalStorageDirectory(), "/Download/" + fileName);
        try {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(content.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String readFile(Context context, String relativePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return readFileUsingMediaStore(context, relativePath);
        } else {
            return readFileUsingEnvironment(relativePath);
        }
    }



    private static String readFileUsingEnvironment(String relativePath) {
        File file = new File(Environment.getExternalStorageDirectory(), "/Download/" + relativePath);
        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                return readFromInputStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
