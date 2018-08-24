package com.example.algorithm.question;

import java.util.List;

public class 报数_ {
    /**
     *
     * @param list n个人（编号1-n）围成一圈报数，报到m的人出队，求最后剩下的那个人
     * @param m
     */
    public static  void getTheLastOne(List<Integer> list, int m){
        int i =0;
       while(list.size()>1){
           if(i==m){
             list.remove(Integer.valueOf(i));
             i=0;
           }else{
               i++;
           }

       }
       System.out.print(" "+list.get(0));

    }
}
