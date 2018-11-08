package com.example.javatest.testT_Type.normal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainTest {

    public static void main(String[] args){
        test(new Sub());
        test(new Sub_Sub());
//        test(new Base());//编译不通过

        test();
    }
    private static <T extends Sub>void test(T t){
        //这种用法就比较准
    }

    private static void test(){
       List<String> list =  new ArrayList<String>();
       List<Integer> list2 =  new ArrayList<Integer>();
       System.out.print(list.getClass() ==list2.getClass());
       System.out.print("\n"+list.getClass());
    }

    /**
     * 有上限的通配符(继承自Base的类)
     * @param para
     */
    public void testSub(Collection<? extends Base> para){
        //仍然编译不通过，失去了添加的能力。只能大致清楚类型的范围。
//        para.add(new Base());//编译不通过
//        para.add(new Sub());//编译不通过
    }

    /**
     * 有下限的通配符（父类是Sub的类）
     * @param para
     */
    public void testSuper(Collection<? super Sub> para){
        para.add(new Sub());//编译通过
//        para.add(new Base());//编译不通过
        para.add(new Sub_Sub());//编译通过

    }




}


