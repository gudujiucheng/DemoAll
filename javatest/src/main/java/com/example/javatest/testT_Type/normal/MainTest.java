package com.example.javatest.testT_Type.normal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MainTest {


    public static void main(String[] args) {
//        test(new Sub());
//        test(new Sub_Sub());
//        test(new Base());//编译不通过
//        test();

//        List list = new ArrayList();
//        List<?> list2 = new ArrayList<>();
//        list.add("");
//        list2.add("");
//        list2 = list;
//
//        List<String> listSting = new ArrayList<>();
//        test(listSting);
//        testx(listSting);


        List<String> listSting = new ArrayList<>();
        safeAdd(listSting, new Integer(1));
        String s = listSting.get(0);

    }


    private static int getNum(Set set1, Set set2) {
        int num = 0;
        for (Object obj : set1) {
            if (set2.contains(obj))
                num++;
        }
        //测试用代码
        set1.add("");//编译通过
        set1.add(null);
        return num;

    }

    private static int getNum02(Set<?> set1, Set<?> set2) {
        int num = 0;
        for (Object obj : set1) {
            if (set2.contains(obj))
                num++;
        }
        //测试用代码
//        set1.add("");//编译不通过
        set1.add(null);
        return num;

    }


    private static void safeAdd(List listSting, Object object) {
        listSting.add(object);
    }

    private static void test(List list) {
    }

    private static void testx(List<Object> list) {
    }


    private static <T extends Sub> void test(T t) {
        //这种用法就比较准
    }

    private static void test() {
        List<String> list = new ArrayList<String>();
        List<Integer> list2 = new ArrayList<Integer>();
        System.out.print(list.getClass() == list2.getClass());
        System.out.print("\n" + list.getClass());
    }

    /**
     * 有上限的通配符(继承自Base的类)
     *
     * @param para
     */
    public void testSub(Collection<? extends Base> para) {
        //仍然编译不通过，失去了添加的能力。只能大致清楚类型的范围。
//        para.add(new Base());//编译不通过
//        para.add(new Sub());//编译不通过
    }

    /**
     * 有下限的通配符（父类是Sub的类）
     *
     * @param para
     */
    public void testSuper(Collection<? super Sub> para) {
        para.add(new Sub());//编译通过
//        para.add(new Base());//编译不通过
        para.add(new Sub_Sub());//编译通过

    }


    private <T> void test(List<T> list, T t, List<?> list2) {
        list.add(t);//编译通过
//        list2.add(t);//编译不通过
    }


//    private  void fun(List<? extends Number> list){
//        list.add(new Integer(1));
//        Number num = list.get(0);
//    }
//    private  void fun02(List<?> list){
//        list.add(new Integer(1));//编译报错
//        Object o = list.get(0);//编译通过
//        Number num = list.get(0);//编译报错
//    }
//    void testUse(){
//        fun(new ArrayList<Integer>());//编译通过
//        fun(new ArrayList<Double>());//编译通过
//        fun(new ArrayList<String>());//编译报错
//    }


    void start() {
        test(new ArrayList<Dog>());//ok
        test(new ArrayList<Cat>());//ok
    }

//    void test(List<? extends Animal> list) {
//        list.add(new Dog());//编译报错
//    }
//
//    void test(List<? super Animal> list) {
//        list.add(new Dog());
//        list.add(new Live());
//    }


}


