package com.example.javatest.for_while;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainForWhileTest {

    public static  void  main(String[] args){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            list.add(i);
        }


        Iterator<Integer> iterator = list.iterator();
        int j = 0;
        while (iterator.hasNext()) {
            System.out.print("\n第几次："+j++);
            int appItemBean = iterator.next();
            if (appItemBean==5) {
                iterator.remove();
                break;
            }
        }

        for (int i = 0; i <list.size() ; i++) {
            System.out.print("  s："+list.get(i));
        }


//        for (int i = 0; i < list.size(); i++) {
//            System.out.print("\n第几次："+i);
//            if (list.get(i)==10) {
//                return ;
////                break;
//            }
//        }
    }
}
