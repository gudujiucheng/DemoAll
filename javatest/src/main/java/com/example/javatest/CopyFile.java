package com.example.javatest;

/**
 * @Description:软件著作权应用
 * @Author: canzhang
 * @CreateDate: 2020/7/27 10:45
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

public class CopyFile {

    int i= 0;

    private File resFile;
    private File desFile = new File("F:/360MoveData/Users/canzhang/Desktop/mock");
    File newFile = new File(desFile, "代码段" + ".doc");
    BufferedWriter bw;

    public static void main(String[] args) {
        try {
            new CopyFile().copyFiles("F:/workspace/lepay_android/app/src/main/java");
        } catch (Exception e) {

        }
    }


    public void copyFiles(String resPath) throws Exception {
        if(i>60)
            return;
        if (bw == null)
            bw = new BufferedWriter(new FileWriter(newFile));
        resFile = new File(resPath);

        if (!desFile.exists())
            desFile.mkdir();
        if (resFile.isDirectory()) {// 如果源目录是文件夹的话
            File[] files = resFile.listFiles();
            for (File file2 : files) {// 遍历源目录下的文件 递归调用
                copyFiles(file2.toString());
            }
        } else {// 如果是文件的话 就直接复制到目的路径


            System.out.println(resFile.toString());
//            if(!resFile.toString().endsWith(".m")){
//                return;
//            }
//            System.out.println("处理的文件："+resFile.toString());
            // 复制文件 字符流 复制图片会出错的
            BufferedReader br = new BufferedReader(new FileReader(resFile));


            String line = null;
            int nowLine = 0;
            int totalLine = getTotalLines(resPath);
            if (totalLine <= 120) {
                br.close();
                return;
            }
            while ((line = br.readLine()) != null) {
                if (nowLine == 10) {
                    line = "--------------------代码片段分割线-------------------------";
                }

                if (nowLine <= 10 || nowLine > totalLine - 60) {//取前120行 和后60行  中间加上分割线
                    bw.write(line);
                    bw.newLine();
                    bw.flush();//不能关掉  关掉就不能持续写入了
                }

                nowLine++;
            }
            br.close();
            i++;
        }
    }


    /**
     * 获取文件的行数
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private int getTotalLines(String fileName) throws IOException {
        FileReader in = new FileReader(fileName);
        LineNumberReader reader = new LineNumberReader(in);
        String strLine = reader.readLine();
        int totalLines = 0;
        while (strLine != null) {
            totalLines++;
            strLine = reader.readLine();
        }
        reader.close();
        in.close();
        return totalLines;
    }
}
