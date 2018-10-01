package com.example.algorithm.question;

import java.util.ArrayList;

public class Question {

    public static void main(String[] args) {
        test();
    }



    private static void test() {
//        打印不大于n的最大素数_.printMaxPrime(1000);
//        找出两个相同的数字_.printSameNum(new int[]{1,5,3,234,343,54456,565,23,4,234});
        报数_.getTheLastOne(new ArrayList<Integer>(){{add(1);add(3);add(5);add(7);add(9);}},3);
    }









/*    有一个整形数组，包含正数和负数，然后要求把数组内的所有负数移至正数的左边，且保证相对位置不变，
    要求时间复杂度为O(n), 空间复杂度为O(1)。
    例如，{10, -2, 5, 8, -4, 2, -3, 7, 12, -88, -23, 35}变化后是{-2, -4，-3, -88, -23,5, 8 ,10, 2, 7, 12, 35}。*/

private static void  test01(int[] aar){//TODO  未完待续
    int tempPosition = -1;

    for (int i = 0; i <aar.length ; i++) {
        int x= aar[i];
        if(x<0&&i!=0){
            tempPosition = i;

        }

    }


}




}
