package com.example.algorithm.question;

import java.util.ArrayList;

public class 打印不大于n的最大素数_ {


    public static void main(String[] args) {
        test();
    }


    private static void test() {
        打印不大于n的最大素数_.printMaxPrime(1000);
    }




//    质数（prime number）又称素数，有无限个。质数定义为在大于1的自然数中，除了1和它本身以外不再有其他因数。
    /**
     * 打印不大于n的所有素数
     * 参考：https://www.jb51.net/article/62991.htm
     * @param n
     */
    public static void  printPrime(int n){

        for(int i = 2; i < n ; i++){

            int count = 0;//记录没有余数的次数

            for(int j = 2 ; j<=i; j++){

                if(i%j==0){//没有余数的情况
                    count++;
                }
                if(j==i & count == 1){//如果j = i，并且出现没有余数的情况仅有一次（也就是j==i这一次），这也就说明了这个数是素数
                    System.out.print(i+" ");
                }
                if(count > 1){//都已经大于1了，说明不是素数，没有必要继续下去了
                    break;
                }
            }


        }

    }


    /**
     * 打印不大于n的最大素数
     * @param n
     */
    public static void printMaxPrime(int n){

        // 外层循环，outer作为标识符
        outer:for (int i = n; i >0 ; i--) {
            int count = 0;
            for (int j =2; j <=i ; j++) {
                if(i%j==0){
                    count++;
                }
                if(count==1&&j==i){//j==i的时候也就是循环完毕了
                    System.out.print("不大于n的最大质数是："+i);
                    break outer;//已经找到最大的，跳出outer标签所标识的循环
                }
                if(count>1)
                    break;//结束内循环
            }

        }

    }
}
