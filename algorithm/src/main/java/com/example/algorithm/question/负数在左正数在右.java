package com.example.algorithm.question;

public class 负数在左正数在右 {


    public static void main(String[] args){
        test01();

    }


/*    有一个整形数组，包含正数和负数，然后要求把数组内的所有负数移至正数的左边，且保证相对位置不变，
    要求时间复杂度为O(n), 空间复杂度为O(1)。
    例如，{10, -2, 5, 8, -4, 2, -3, 7, 12, -88, -23, 35}变化后是{-2, -4，-3, -88, -23,5, 8 ,10, 2, 7, 12, 35}。

    参考：https://blog.csdn.net/epubit17/article/details/80342004

    */

    private static void  test01() {
        int[] aar = new int[]{0, 10, 5, 8, 8, 4, 2, 3, 2, 2, 3, 0, 7, 1, 2, 3};
        int tempPosition = -1;//记录最左边负数在数组中的索引值   默认第一次是第0个 也就是  -1+1


        for (int i = 0; i < aar.length; i++) {
            int x = aar[i];
            if (x < 0) {//如果小于0 则依次替换前面的元素
                int tempValue = aar[tempPosition + 1];
                aar[tempPosition + 1] = x;
                aar[i] = tempValue;
                tempPosition++;
            }

        }


        for (int i = 0; i < aar.length; i++) {
            System.out.print(" " + aar[i]);
        }
    }
}
