package com.example.algorithm.demo;

import static com.example.algorithm.demo.MainTest.printList;

/**
 * 选择排序
 */
public class 选择排序_ {


    /**
     * 简单选择排序
     * 1、遍历整个序列，将最小的数放在前面
     * 2、遍历剩下的序列，将最小的数字放在最前面
     * 3、重复二步，知道将最小的数字放在最前面
     * @param list
     */
    public static void select02(int[] list){

        for (int i = 0; i <list.length ; i++) {
            //首先取出一个值
            int temp = list[i];
            //然后依次和后面的数字进行比较
            for (int j = i+1; j <list.length ; j++) {
                int temp2 = list[j];
                //如果大于则互换
                if(temp>temp2){
                    list[j] = temp;
                    temp = temp2;
                }

            }
            //最后找到的最小值给list[i]
            list[i] = temp;

        }
        printList(list);
    }

    public static void select(int[] list) {
        for (int i = 0; i <list.length ; i++) {
            //首先取出来一个元素
            int a = list[i];
            //依次和后面的元素进行对比
            for (int j =i+1; j <list.length ; j++) {
                if(list[j]>a){//如果大于则互换
                    int temp = a;
                    a = list[j];//记录被替换掉的最大值
                    list[j] = temp;
                }

            }
            list[i] = a;//本次轮询的最大值赋值

        }
        printList(list);
    }


    /**
     * 范例写法 比较别人的优点
     * @param a
     */
    public void selectSort(int[] a) {
        //首先是长度只是取依次
        int length = a.length;
        for (int i = 0; i < length; i++) {
            int key = a[i];
            int position=i;
            for (int j = i + 1; j < length; j++) {
                if (a[j] < key) {
                    key = a[j];
                    //另外记录最小值position 而不是一直频繁的操作对象
                    position = j;
                }
            }
            a[position]=a[i];//交换位置
            a[i]=key;
        }
    }

}
