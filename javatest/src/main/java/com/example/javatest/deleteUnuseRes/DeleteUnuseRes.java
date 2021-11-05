package com.example.javatest.deleteUnuseRes;

/**
 * 删除无用资源
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeleteUnuseRes {


    public static void main(String[] args) {
        try {
            //路径参考：/Users/canzhang/AndroidStudioProjects/GameHelperAndroid/apps/app.cf/build/outputs/mapping/debug/resources.txt
            List<String> cfUnusedFiles = new DeleteUnuseRes().finUnusedFiles(new ArrayList<String>(),
                    "cf_resources.txt");
            System.out.println("cf无用文件长度：" + cfUnusedFiles.size());

            List<String> dnfUnusedFiles = new DeleteUnuseRes().finUnusedFiles(new ArrayList<String>(),
                    "dnf_resources.txt");
            System.out.println("dnf无用文件长度：" + dnfUnusedFiles.size());

            cfUnusedFiles.retainAll(dnfUnusedFiles);
            System.out.println("无用文件并集长度：" + cfUnusedFiles.size());

            deleteRes(cfUnusedFiles, "/Users/canzhang/AndroidStudioProjects/GameHelperAndroid/");

        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());

        }
    }


    public List<String> finUnusedFiles(List<String> unUsedList, String resPath) throws Exception {


        File resFile = new File(resPath);
        if (resFile.isDirectory()) {// 如果源目录是文件夹的话
            File[] files = resFile.listFiles();
            for (File file2 : files) {// 遍历源目录下的文件 递归调用
                finUnusedFiles(unUsedList, file2.toString());
            }
        } else {
            // 如果是文件的话 就直接复制到目的路径
            System.out.println(resFile.toString());
            BufferedReader br = new BufferedReader(new FileReader(resFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Skipped unused resource res")) {


                    //获取文件名
                    String fileName = line.substring(line.lastIndexOf("/") + 1, line.indexOf(":"));

                    //去掉类型  FIXME 非必要选项，用于筛查代码是否有引用
                    if (fileName.contains(".")) {
                        fileName = fileName.substring(0, fileName.indexOf("."));
                    } else {
                        System.out.println("没有点？？？：" + fileName);
                    }

                    //记录此文件
                    unUsedList.add(fileName);
                }

            }
            br.close();

        }
        return unUsedList;
    }

    private static void onlyDeleteRes(List<String> deleteList, String checkPath) {
        //需要检查的路径，检查所有同名文件，并删除
        File resFile = new File(checkPath);
        if (resFile.isDirectory()) {
            File[] files = resFile.listFiles();
            for (File file : files) {// 遍历源目录下的文件 递归调用
                onlyDeleteRes(deleteList, file.toString());
            }
        } else {
            String fileName = resFile.getName();
            if (fileName.contains(".") && deleteList.contains(fileName.substring(0, fileName.indexOf(".")))) {
                boolean delete = resFile.delete();
                if (delete) {
                    System.out.println("删除文件：" + fileName);
                    deleteList.remove(fileName.substring(0, fileName.indexOf(".")));
                }

            }
        }
    }

    private static void deleteRes(List<String> deleteList, String checkPath) {
        //需要检查的路径，检查所有同名文件，并删除
//        File resFile = new File(checkPath);
//        if (resFile.isDirectory()) {
//            File[] files = resFile.listFiles();
//            for (File file : files) {// 遍历源目录下的文件 递归调用
//                deleteRes(deleteList, file.toString());
//            }
//        } else {
//            String s = resFile.toString();
//            if (deleteList.contains(resFile.getName())) {
//
//                if(resFile.getName().endsWith(".png")||resFile.getName().endsWith(".webp")){
//                    boolean delete = resFile.delete();
//                    if(delete){
//                        System.out.println("删除文件："+resFile.getName());
//                    }
//                }
//            }
//        }

        int preSize = deleteList.size();
        num = 0;
        System.out.println("准备开始删除 处理前文件数量：" + preSize);
        try {
            findStringInPath(deleteList, checkPath);
        } catch (IOException e) {
            System.out.println("异常：" + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("处理后 剩余可删除的文件数量：" + deleteList.size() + " 处理前数量：" + preSize + " 扫描文件的总数量：" + num);
        for (String name : deleteList) {
            System.out.println("剩余文件名：" + name);
        }
        onlyDeleteRes(deleteList,checkPath);

        System.out.println("删除剩余量：" + deleteList.size() );
        for (String name : deleteList) {
            System.out.println("删除剩余的文件名：" + name);
        }
    }


    public static void findStringInPath(List<String> deleteList, String checkPath) throws IOException {
        if(deleteList.size()==0){
            return;
        }
        File resFile = new File(checkPath);
        if (resFile.isDirectory()) {
            String path = resFile.getName();
            if (path.startsWith(".gradle")
                    || path.startsWith(".idea")
                    || path.startsWith(".settings")
                    || path.startsWith(".git")
                    || path.equals("build")) {
                System.out.println("避免扫描：" + resFile.getPath() + " path:" + path);
                return;

            }

            File[] files = resFile.listFiles();
            for (File file : files) {// 遍历源目录下的文件 递归调用
                findStringInPath(deleteList, file.toString());
            }
        } else {

            Iterator<String> iterator = deleteList.iterator();
            while (iterator.hasNext()) {
                String name = iterator.next();
                if (findStringInFile(resFile, name)) {
                    //发现引用，该文件不可删除，移出集合
                    System.out.println("发现引用，该资源不可删除，移出集合：" + name);
                    iterator.remove();
                }
            }
        }
    }

    private static int num = 0;

    public static boolean findStringInFile(File file, String name) throws IOException {
        String fileName = file.getName();
        if (!fileName.endsWith(".java") && !fileName.endsWith(".kt") && !fileName.endsWith(".xml")) {
            return false;
        }
        if (fileName.contains(".") && fileName.substring(0, fileName.indexOf(".")).equals(name)) {
            System.out.println("自己本身 返回  fileName：" + fileName + " name：" + name);
            return false;
        }
//        System.out.println("检查文件名：" + name + " 扫描的文件：" + file.getName());
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("#") || line.startsWith("//")) {
                continue;
            }
            //指定字符串判断处
            if (line.contains(name)) {
                System.out.println("发现引用---------------->>>>>>引用文件：" + name + " 扫描的文件：" + fileName);
                return true;
            }
        }
        bufferedReader.close();
        num++;
        return false;
    }


}
