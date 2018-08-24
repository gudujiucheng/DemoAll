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



}
