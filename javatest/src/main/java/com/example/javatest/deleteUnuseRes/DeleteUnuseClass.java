package com.example.javatest.deleteUnuseRes;

/**
 * 删除无用资源
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class DeleteUnuseClass {

    public static void main(String[] args) {
        try {

            List<String> cfUnusedFiles = new DeleteUnuseClass().finUnusedFiles(new ArrayList<String>(),
                    "cf_usage.txt");
            System.out.println("cf无用文件长度：" + cfUnusedFiles.size());

            List<String> dnfUnusedFiles = new DeleteUnuseClass().finUnusedFiles(new ArrayList<String>(),
                    "dnf_usage.txt");
            System.out.println("dnf无用文件长度：" + dnfUnusedFiles.size());

            cfUnusedFiles.retainAll(dnfUnusedFiles);
            System.out.println("无用文件并集长度：" + cfUnusedFiles.size());

            deleteRes(cfUnusedFiles, "/Users/canzhang/AndroidStudioProjects/GameHelperAndroid/");

        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());

        }
    }


    private String lastLine;
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
                if (!line.contains("$") && !line.contains(":") && !line.contains(" ")) {
                    //如果下一行依然是标准类，则直接添加上次记录，反之则清空上次记录
                    if(lastLine!=null){
                        System.out.println("记录无用类：" +lastLine);
                        unUsedList.add(lastLine);
                    }

                    //首先读到一个疑似无用类，然后我们记录这一行,并存储上一行的记录
                    lastLine = line;


                }else {
                    lastLine =null;
                }

            }
            br.close();

        }
        return unUsedList;
    }

    static int num = 0;


    private static void deleteRes(List<String> deleteList, String checkPath) {
        //需要检查的路径，检查所有同名文件，并删除
        File resFile = new File(checkPath);
        if (resFile.isDirectory()) {
            File[] files = resFile.listFiles();
            for (File file : files) {// 遍历源目录下的文件 递归调用
                deleteRes(deleteList, file.toString());
            }
        } else {
            String name = resFile.getPath();
            if (name.endsWith(".java") || name.endsWith(".kt")) {
                name = name.replaceAll("/", ".");
                name = name.substring(0, name.lastIndexOf("."));
//                System.out.println("xxx：" + name);
                String finalName = name;
                boolean match = deleteList.stream().anyMatch(s -> finalName.endsWith(s));
                if (match) {
//                    boolean delete = resFile.delete();
//                    if (delete) {
                        System.out.println("删除文件：" + finalName + "     " + num++);
//                    }
                }
            }


        }

    }


}
