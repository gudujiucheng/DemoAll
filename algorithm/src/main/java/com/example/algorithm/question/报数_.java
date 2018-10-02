package com.example.algorithm.question;

import java.util.ArrayList;
import java.util.List;

public class 报数_ {


    public static void main(String[] args) {
        报数_.getTheLastOne(new ArrayList<Integer>(){{add(1);add(3);add(5);add(7);add(9);}},3);
    }

    /**
     *
     * @param list n个人（编号1-n）围成一圈报数，报到m的人出队，求最后剩下的那个人
     * @param m
     */
    public static  void getTheLastOne(List<Integer> list, int m){//FIXME
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
