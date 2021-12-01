package com.example.javatest.deleteUnuseRes;

/**
 * 删除无用资源
 */

import android.annotation.SuppressLint;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Task {


    public static void main(String[] args) {
        num = 0;
        onlyDeleteRes("/Users/canzhang/AndroidStudioProjects/GameHelperAndroid/");
    }


    private static int num;

    @SuppressLint("NewApi")
    private static void onlyDeleteRes(String checkPath) {
        //需要检查的路径，检查所有同名文件，并删除
        File resFile = new File(checkPath);
        if (resFile.isDirectory()) {

            File[] files = resFile.listFiles();

            if (!resFile.getPath().contains("build")
                    && resFile.getName().startsWith("drawable")//FIXME 开头
//                    && resFile.getPath().contains("dnfip")//FIXME 开头
//                    && resFile.getName().equals("output")//FIXME 开头

                    && files != null && files.length > 0) {


//                if (!resFile.getPath().contains("apps") && !resFile.getPath().contains("app.cf")) {
//                    return;
//                }


                try {
                    for (int i = 0; i < files.length; i++) {
                        boolean output = files[i].getName().endsWith(".9.png");
                        if (output) {
                            System.out.println("当前路径：" + resFile.getPath()+"    "+num++);
                            return;
                        }
                    }
//                    long start = System.currentTimeMillis();
//                    Process pr = Runtime.getRuntime().exec("super-tinypng", null, resFile);
//                    InputStreamReader isr = new InputStreamReader(pr.getInputStream());
//                    BufferedReader br = new BufferedReader(isr);
//                    String line = null;
//                    while ((line = br.readLine()) != null) {
//                        System.out.println(line);
//                    }
//                    br.close();
//                    // 等待刚刚执行的命令的结束
//                    while (true) {
//                        if (pr.waitFor() == 0) break;
//                    }
//                    System.out.println("执行完毕：" + resFile.getAbsolutePath() + " 耗时：" + (System.currentTimeMillis() - start) + "ms " + num++);


//                    if (resFile.getPath().contains("output")) {
//                        fun(resFile, new File(resFile.getPath().replace("output", "")));
//                        deleteDir(resFile);
//                    }


                } catch (Exception e) {
                    System.out.println("异常：" + e.getMessage());
                    e.printStackTrace();
                }
            }

            for (File file : files) {// 遍历源目录下的文件 递归调用
                onlyDeleteRes(file.toString());
            }
        }
    }


    public static void fun(File srcFolder, File destFolder) {
        File[] fileArray = srcFolder.listFiles();
        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
        for (File file : fileArray) {
            if (file.isDirectory()) {
                String folderName = file.getName();
                File newDestFolder = new File(destFolder, folderName);
                fun(file, newDestFolder);
            } else {
                String fileName = file.getName();
                File destFile = new File(destFolder, fileName);
                copy(file, destFile);
            }
        }
    }

    public static void copy(File file, File destFile) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (file.getName().endsWith(".9.png")) {//点九图有问题  暂时不压缩
            return;
        }
        if (destFile.exists()) {
            destFile.delete();
        }
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * 删除空目录
     *
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


}
