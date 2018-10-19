package com.example.algorithm.demo;

import static com.example.algorithm.demo.MainTest.printList;

/**
 * 冒泡排序
 */
public class 冒泡排序_ {


    public static void bubble(int[] list){
        int length = list.length;

        for (int i = 0; i <length ; i++) {//第一步：定义要循环比较多少次
            //设置真正开始比较的一串数组
            for (int j = 1; j <length-i ; j++) {
                int temp = list[j-1];//依次取出循环表元素两两互相比较
                if(temp>list[j]){//将大的放在后面
                    list[j-1] = list[j];
                    list[j] =temp;
                }

            }

        }

        printList(list);
    }

    /**
     * 标准写法
     * @param a
     */
    public void bubbleSort(int[] a){
        int length=a.length;
        int temp;
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a.length-i-1;j++){
                if(a[j]>a[j+1]){
                    temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                }
            }
        }
    }
    /**
     * 选择排序   对比下加深理解
     * 选择排序是选择一个数字 依次与后面的数字进行比较选出最值，最后才和最初比较的值互换。
     * 而冒泡则是两两比较，只要大就立即互换。
     * @param a
     */
    public void selectSort(int[] a) {
        int length = a.length;
        for (int i = 0; i < length; i++) {//循环次数
            int key = a[i];
            int position=i;
            for (int j = i + 1; j < length; j++) {//选出最小的值和位置
                if (a[j] < key) {
                    key = a[j];
                    position = j;
                }
            }
            a[position]=a[i];//交换位置
            a[i]=key;
        }
    }















 //-----------------------------练习冒泡排序
 public static void main(String[] args){

        test();

 }
    //2018年10月11日23:22:53 练习
    private static void test02() {
        int[] arr = new int[]{1,4,8,2,3,9,5};
        int length = arr.length;
        int count=0;
        for (int i = 0; i <length-1 ; i++) {//length-1其实只是需要 这些次就够了   最后一次没必要比较了  最后一次就只剩下一个数字了
            for (int j = 1; j <length-i ; j++) {
                if(arr[j-1]>arr[j]){
                    int temp = arr[j-1];
                    arr[j-1] = arr[j];
                    arr[j] = temp;
                    count++;
                }
            }

        }
        System.out.print("互换了几次:"+count+"\n");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(" "+arr[i]);
        }
    }


    //2018年10月11日23:22:21 练习
    private static void test() {
        int[] arr = new int[]{1,4,8,2,3,9,5};
        //首先回忆冒泡思想
        //1、从头开始相邻数据相互比较 较大数字上浮   2、依次重复上述动作，注意去除最后面那个最大的数字
        int count=0;
        for (int i = 0; i <arr.length ; i++) {//外循环 负责记录循环了几次的

            for (int j = 1; j <arr.length-i ; j++) {//内循环真正交换冒泡的
                if(arr[j-1]>arr[j]){
                    int temp = arr[j-1];
                    arr[j-1] =arr[j];
                    arr[j] = temp;
                   count++;
                }
            }

/*
* 错误写法分析：1、取arr[i]来依次和其他值进行比较，这是不对的，应该取新的截断数组的第一个数arr[j-1]，依次和后面的比
* */
//            for (int j = 1; j <arr.length-i ; j++) {//内循环真正交换冒泡的
//                if(arr[i]>arr[j]){
//                    int temp = arr[i];
//                    arr[i] =arr[j];
//                    arr[j] = temp;
//                }
//            }


        }
        System.out.print("互换了几次:"+count+"\n");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(" "+arr[i]);
        }

    }


}
