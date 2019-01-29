package com.example.javatest.String_test;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MainStringTest {
    public static void main(String[] args){
//        testIndex();

        System.out.print(getPlatformList("cdxxxxxxxxxxxxxxxxxxxxxx"));

    }

    private static void testIndex() {
        StringBuilder stringBuilder = new StringBuilder("qweqweqcddd");
        int index = stringBuilder.indexOf("c");

        stringBuilder.replace(index,index+1,"");
        System.out.print("index:"+index+"  result:"+stringBuilder.toString());
    }


    /**
     * 去重复
     * @param text
     * @return
     */
    public static String removeRepeatedChar(String text) {
        char[] str = text.toCharArray();
        if(str.length == 0) {
            return "";
        }
        List<Character> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < str.length; i++) {
            if(!list.contains(str[i])){
                list.add(str[i]);
                sb.append(str[i]);
            }
        }
        return sb.toString();
    }



    private static StringBuilder getPlatformList(String platformList) {
        String list = removeRepeatedChar(platformList);
        StringBuilder shareNumber = new StringBuilder(list);
        //微信网页分享和小程序分享只能保留一个，优先保留微信网页分享
        int miniProgramIndex = shareNumber.indexOf("d");
        int weChatIndex = shareNumber.indexOf("4");
        if(miniProgramIndex!=-1&&weChatIndex!=-1){
            shareNumber.replace(miniProgramIndex,miniProgramIndex+1,"");
        }
        if (shareNumber.length()==0) {
            shareNumber = new StringBuilder("4573");
        } else if ((1 < shareNumber.length() && shareNumber.length() < 4)) {
            int size = shareNumber.length();

            String defaultString;
            if (shareNumber.indexOf("c") > -1) {
                defaultString = "c457";
            } else {
                defaultString = "4573";
            }

            for (int i = 0; i < defaultString.length(); i++) {
                char s = defaultString.charAt(i);
                if (isAdd(shareNumber,s, size)) {
                    shareNumber.append(s);
                    if (shareNumber.length() >= 4) {
                        break;
                    }
                }
            }
        } else {
            shareNumber = new StringBuilder(list);
        }
        return shareNumber;
    }


    public static  boolean isAdd(StringBuilder shareNumber ,char arg, int size) {
        for (int j = 0; j < size; j++) {
            char temp =  shareNumber.charAt(j);
            if (arg == temp) {//已存在
                return false;
            }else if(shareNumber.indexOf("d")!=-1&&"4".equals(arg+"")){
                //已经存在小程序方式，则不加微信网页方式
                return false;
            }else if(shareNumber.indexOf("4")!=-1&&"d".equals(arg+"")){
                //已经存在微信网页方式，则不添加小程序方式
                return false;
            }
        }
        return true;
    }
}
