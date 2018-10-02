package com.example.algorithm.question.todo;

/*作为一个手串艺人，有金主向你订购了一条包含n个杂色串珠的手串——每个串珠要么无色，要么涂了若干种颜色。
为了使手串的色彩看起来不那么单调，金主要求，手串上的任意一种颜色（不包含无色），在任意连续的m个串珠里至多出现一次（注意这里手串是一个环形）。
手串上的颜色一共有c种。现在按顺时针序告诉你n个串珠的手串上，每个串珠用所包含的颜色分别有哪些。请你判断该手串上有多少种颜色不符合要求。
即询问有多少种颜色在任意连续m个串珠中出现了至少两次。

        输入描述：

        第一行输入n，m，c三个数，用空格隔开。(1<=n<=10000,1<=m<=1000,1<=c<=50) 

        接下来n行每行的第一个数num_i(0<=num_i<=c)表示第i颗珠子有多少种颜色。
        接下来依次读入num_i个数字，每个数字x表示第i颗柱子上包含第x种颜色(1<=x<=c)

        输出描述：

        一个非负整数，表示该手链上有多少种颜色不符需求。

        输入例子：

        5 2 3   (串珠数、任意连续要求数、共多少种颜色)

        3 1 2 3 （第一颗珠子有 3种颜色分别是 1、2、3   下同）

        0 

        2 2 3

        1 2 

        1 3

        输出例子：

        2

        ---------------------

        本文来自 粉笔头的粉粉笔 的CSDN 博客 ，全文地址请点击：https://blog.csdn.net/heqiang60/article/details/81113308?utm_source=copy */


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class 串珠问题 {

    /**
     * 定义珠子
     */
    public static class Node {
        public Node() {

        }

        int colorNum;
        List<Integer> colorList;
    }


    public static void main(String[] args) {
        test02();
    }


    public static void test02() {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        s = s.replaceAll(" ", "");

        int n = s.charAt(0) - '0';//多少个珠子
        int m = s.charAt(1) - '0';//要求的连续数
        int c = s.charAt(2) - '0';//共有多少颜色

        //创建n个串珠
        List<Node> nodeList = new ArrayList<Node>();
        for (int i = 0; i < n; i++) {
            nodeList.add(new Node());
        }
        int temp = n;
        //循环读取接下来的列，读取每一列包含的颜色
        while (temp > 0) {
            Scanner in = new Scanner(System.in);
            String inString = in.nextLine();
            int colorNum = inString.charAt(0) - '0';
            List<Integer> colorList = new ArrayList<Integer>();
            for (int i = 0; i < colorNum+1; i++) {
                if (i != 0)
                    colorList.add(inString.charAt(i) - '0');
            }

            Node node = nodeList.get(n - temp);
            node.colorNum = colorNum;
            node.colorList = colorList;
            temp--;
        }

        //数据填充完毕，开始分析最后问题 ，任意m个连续珠子，不能出现相同颜色

        //第一步取任意连续的两个珠子

        int repeatNum = 0;
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            Node nextNode;
            if (i == nodeList.size() - 1) {
                nextNode = nodeList.get(0);
            } else {
                nextNode = nodeList.get(i + 1);
            }
            //找到连续的两个node，然后拿出颜色作对比

            List<Integer> colorList = node.colorList;
            List<Integer> colorList02 = nextNode.colorList;
            int num = 0;
            for (int j = 0; j < colorList.size(); j++) {//遍历
                if (colorList02.contains(colorList.get(j))) {
                    num++;
                }
            }
            if (num >= 1) {
                repeatNum++;
            }


        }

        System.out.println("重复的次数：" + repeatNum);
    }




    //这个太复杂  不好看懂
    private static void test() {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        s = s.replaceAll(" ", "");
        int n = s.charAt(0) - '0';//转换成int 值    会自动转换
        int m = s.charAt(1) - '0';
        int c = s.charAt(2) - '0';
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String s1 = sc.nextLine();
            s1 = s1.replaceAll(" ", "");
            int num = s1.charAt(0) - '0';
            List<Integer> ll = new ArrayList<>();
            if (num == 0) {
                ll.add(num);
            } else {
                for (int j = 0; j < num; j++) {
                    int numi = s1.charAt(j + 1) - '0';
                    ll.add(numi);
                }
            }
            list.add(ll);
        }
        sc.close();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            int j = 0;
            while (j < (m - 1)) {
                if ((i + j) >= list.size() - 1) {
                    for (int k = 0; k < list.get(i + j + 1 - list.size())
                            .size(); k++) {
                        if (list.get(i).contains(
                                list.get(i + j + 1 - list.size()).get(k))) {
                            count++;
                            j = j + m;
                            break;
                        }
                    }

                } else {
                    for (int k = 0; k < list.get(i + j + 1).size(); k++) {
                        if (list.get(i).contains(list.get(i + j + 1).get(k))) {
                            count++;
                            j = j + m;
                            break;
                        }
                    }
                }
                j++;
            }
        }
        System.out.println(count);
    }
}




