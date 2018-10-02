package com.example.algorithm.question;

public class 找出两个相同的数字_ {

    public static void main(String[] args) {
        test();
    }


    private static void test() {
        找出两个相同的数字_.printSameNum(new int[]{1,5,3,234,343,54456,565,23,4,234});
    }

//    按位异或运算符: ^
//    两个相同的数按位异或结果为0
//    任何数和零按位异或结果为这个数本身
    public static void printSameNum(int[] arr){
       outer: for (int i = 0; i <arr.length ; i++) {
            int a = arr[i];
            for (int j = 0; j <arr.length ; j++) {
                int b = arr[j];
                if(i!=j&&((a^b)==0)){
                    System.out.print(a);
                    break outer;
                }
            }

        }

    }
}
