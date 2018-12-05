package com.example.javatest.for_while;

import java.util.ArrayList;
import java.util.List;

public class MainForWhileTest {

    public static  void  main(String[] args){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            list.add(i);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.print("\n第几次："+i);
            if (list.get(i)==10) {
                return ;
//                break;
            }
        }
    }
}
