package com.example.algorithm.demo;

public class 快速排序_ {

    public static void main(String[] args) {
//        int[] arr = new int[]{1, 3, 1, 5, 7, 9, 11,88, 4, 3};
        int[] arr = new int[]{1, 3};
        quickSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(" " + arr[i]);
        }
    }
    //------------标准递归写法-----------

    public static void quickSort(int[] arr) {
        qsort(arr, 0, arr.length - 1);
    }

    private static void qsort(int[] arr, int left, int right) {
        System.out.println(String.format("qsort() -> left:%s, right:%s", left, right));
        if (left < right) {
            //将数组分为两部分
            int pivot = partition(arr, left, right);
            //递归排序左子数组
            qsort(arr, left, pivot - 1);
            //递归排序右子数组
            qsort(arr, pivot + 1, right);
        }
    }

    private static int partition(int[] arr, int left, int right) {
        int base = arr[left];//选择一个基准点
        while (left < right) {//不相等就继续循环，只到相遇
            while (left < right && arr[right] >= base) {
                --right;
            }
            //交换比基准小的记录到左端 （这里第一轮相当于占用基准坑位）
            arr[left] = arr[right];
            while (left < right && arr[left] <= base) {
                ++left;
            }
            //交换比基准大的记录到右端
            arr[right] = arr[left];

        }
        //扫描完成，把基准数值放到最后剩余的坑位中 （相遇 left = right）
        arr[left] = base;
        //返回的是枢轴的位置
        return left;
    }


    private static int partitionX(int[] arr, int left, int right) {
        int start = left;
        int base = arr[left];//选择一个基准点
        while (left < right) {//不相等就继续循环，只到相遇
            while (left < right && arr[right] >= base) {//这里要找到一个小于基准数的，否则一直循环，直到相遇。
                --right;
            }
            while (left < right && arr[left] <= base) {//这里要找一个大于基准数的，否则一直循环，只到相遇
                ++left;
            }

            if (left < right) {//两个坑位互换
                int temp  = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }

        //扫描完成，基准坑位和中轴换一下
        arr[start] = arr[left];
        arr[left] = base;
        //返回的是枢轴的位置
        return left;
    }
}

