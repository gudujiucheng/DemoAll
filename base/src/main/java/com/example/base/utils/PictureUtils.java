package com.example.base.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片管理类
 */
public class PictureUtils {

    private static final String TAG = "PictureUtils";


    /**
     * 删除相对路径下的图片 （这个api 只有自己存的自己才能删除，如果是别的app存的则不能删除，还需要特别意图授权才能删除）
     *
     * @param context
     * @param relativePath
     */
    public static void deleteImagesInRelativePath(Context context, String relativePath) {
        Log.d(TAG, "delete img on relativePath:" + relativePath + " current api:" + Build.VERSION.SDK_INT);
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            String selection = MediaStore.Images.Media.RELATIVE_PATH + "=?";
            String[] selectionArgs = new String[]{Environment.DIRECTORY_PICTURES + File.separator + relativePath};
            int deleteNum = context.getContentResolver().delete(collection, selection, selectionArgs);
            Log.d(TAG, "delete img on relativePath:" + relativePath + " delete num:" + deleteNum);
        } else {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + relativePath);
            if (directory.exists()) {
                String[] children = directory.list();
                if (children != null) {
                    for (String child : children) {
                        new File(directory, child).delete();
                        Log.d(TAG, "delete img on relativePath:" + relativePath + " delete file:" + child);
                    }
                }

            }
        }
    }

    /**
     * 读取是无论是否自己存的，都可以读取
     */

    /**
     * 获取相对路径下的图片
     *
     * @param context
     * @param relativePath 相对路径
     * @return
     */
    public static List<Uri> getImagesInRelativePath(Context context, String relativePath) {
        List<Uri> imageUris = new ArrayList<>();

        Log.d(TAG, "read img from relativePath:" + relativePath + " current api:" + Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String path = Environment.DIRECTORY_PICTURES + File.separator + relativePath;
            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

            Cursor query = context.getContentResolver().query(
                    collection,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.RELATIVE_PATH + " = ?",
                    new String[]{path},
                    null
            );

            if (query != null) {
                try {
                    while (query.moveToNext()) {
                        int idColumn = query.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                        long id = query.getLong(idColumn);
                        Uri imageUri = ContentUris.withAppendedId(collection, id);
                        imageUris.add(imageUri);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "read img from relativePath:" + relativePath + " has error  :" + e.getMessage(), e);
                } finally {
                    query.close();
                }
            }
        } else {
            File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator);
            File saveDir = new File(pictures, relativePath);

            if (saveDir.exists() && saveDir.isDirectory()) {
                File[] imageFiles = saveDir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".png") ||
                                name.toLowerCase().endsWith(".jpg") ||
                                name.toLowerCase().endsWith(".jpeg") ||
                                name.toLowerCase().endsWith(".webp") ||
                                name.toLowerCase().endsWith(".gif");
                    }
                });

                if (imageFiles != null) {
                    for (File imageFile : imageFiles) {
                        Uri imageUri = Uri.fromFile(imageFile);
                        imageUris.add(imageUri);
                    }
                }
            }
        }
        if (imageUris.size() > 0) {
            for (Uri uri : imageUris) {
                Log.d(TAG, "read img from relativePath:" + relativePath + " uri:" + uri);
            }
        } else {
            Log.e(TAG, "not find  img from relativePath:" + relativePath);
        }


        return imageUris;
    }


    public static Uri saveBitmapToPicture(Context context, Bitmap bitmap, String filePath) {
        // 图片信息
        ContentValues imageValues = new ContentValues();
        File file = new File(filePath);
        String fileName = file.getName();
        String relativePath = file.getParent();
        String mimeType = getMimeType(fileName);
        if (mimeType != null) {
            imageValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        }
        long date = System.currentTimeMillis() / 1000;
        imageValues.put(MediaStore.Images.Media.DATE_ADDED, date);
        imageValues.put(MediaStore.Images.Media.DATE_MODIFIED, date);

        // 保存的位置
        Uri collection;
        OutputStream outputStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            imageValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + relativePath);
            imageValues.put(MediaStore.Images.Media.IS_PENDING, 1);
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri uri = context.getContentResolver().insert(collection, imageValues);

            try {
                if (uri != null) {
                    outputStream = context.getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream);
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "save img " + filePath + " has error  :" + e.getMessage(), e);
            }

            imageValues.clear();
            imageValues.put(MediaStore.Images.Media.IS_PENDING, 0);
            context.getContentResolver().update(uri, imageValues, null, null);
        } else {

            // 老版本
            File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File saveDir = relativePath != null ? new File(pictures, relativePath) : pictures;

            if (!saveDir.exists() && !saveDir.mkdirs()) {
                Log.e(TAG, "save: error: can't create Pictures directory");
                return null;
            }
            // 文件路径查重，重复的话在文件名后拼接数字
            File imageFile = new File(saveDir, fileName);
            String fileNameWithoutExtension = imageFile.getName().replaceFirst("[.][^.]+$", "");
            String fileExtension = imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1);

            Uri queryUri = queryMediaImageBefore28(context.getContentResolver(), imageFile.getAbsolutePath());
            int suffix = 1;
            while (queryUri != null) {
                String newName = fileNameWithoutExtension + "(" + suffix++ + ")." + fileExtension;
                imageFile = new File(imageFile.getParent(), newName);
                queryUri = queryMediaImageBefore28(context.getContentResolver(), imageFile.getAbsolutePath());
            }

            imageValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
            // 保存路径
            String imagePath = imageFile.getAbsolutePath();
            Log.v(TAG, "save file: " + imagePath);
            imageValues.put(MediaStore.Images.Media.DATA, imagePath);
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            try {
                outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "save img " + filePath + " has error  :" + e.getMessage(), e);
            }
        }

        Uri uri = context.getContentResolver().insert(collection, imageValues);

        Log.d(TAG, "save img " + filePath + "  result:" + uri);
        // 插入图片信息
        return uri;
    }


    private static String getMimeType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return null;
        }
    }


    /**
     * Android Q以下版本，查询媒体库中当前路径是否存在
     *
     * @return Uri 返回null时说明不存在，可以进行图片插入逻辑
     */
    private static Uri queryMediaImageBefore28(ContentResolver contentResolver, String imagePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return null;
        }

        File imageFile = new File(imagePath);
        if (imageFile.canRead() && imageFile.exists()) {
            Log.v(TAG, "query: path: " + imagePath + " exists");
            // 文件已存在，返回一个file://xxx的uri
            return Uri.fromFile(imageFile);
        }

        // 保存的位置
        Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // 查询是否已经存在相同图片
        Cursor query = contentResolver.query(
                collection,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA},
                MediaStore.Images.Media.DATA + " == ?",
                new String[]{imagePath},
                null
        );
        if (query != null) {
            try {
                while (query.moveToNext()) {
                    int idColumn = query.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                    long id = query.getLong(idColumn);
                    Uri existsUri = ContentUris.withAppendedId(collection, id);
                    Log.v(TAG, "query: path: " + imagePath + " exists uri: " + existsUri);
                    return existsUri;
                }
            } finally {
                query.close();
            }
        }
        return null;
    }


}
