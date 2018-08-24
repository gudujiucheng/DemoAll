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
}
