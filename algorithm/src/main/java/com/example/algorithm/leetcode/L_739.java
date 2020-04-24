package com.example.algorithm.leetcode;


import java.util.Stack;

public class L_739 {
    public static void main(String[] args) {
        int[] ints = dailyTemperaturesX(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);

        }
    }

    public static int[] dailyTemperatures(int[] T) {
        if (T == null || T.length == 0) {
            return T;
        }
        int length = T.length;
        int[] ints = new int[length];
        for (int i = 0; i < length; i++) {
            int cur = T[i];
            for (int j = i + 1; j < length; j++) {
                int temp = T[j];
                if (temp > cur) {
                    ints[i] = j - i;
                    break;
                }

            }
        }

        return ints;
    }


    //官方解法，相对不太好理解
    public static int[] dailyTemperaturesX(int[] T) {
        if (T == null || T.length == 0) {
            return T;
        }
        int length = T.length;
        int[] ints = new int[length];
        Stack<Integer> stack = new Stack<>();
        for (int i = length - 1; i > 0; i--) {//倒序压如
            while (!stack.isEmpty() && T[i] >= T[stack.peek()]) {//遍历栈 如果有小于当前天气值的，则直接移除，因为没啥意义了，我们的目的是要统计高于前几天天气的，所以小于当前值的不会进入后面判断流程
                stack.pop();
            }
            //经过上面的逻辑，小于当前天气的都已经出栈，剩下的大于的，最顶上的那个索引减去当前索引就是相隔天数了
            ints[i] = stack.isEmpty() ? 0 : stack.peek() - i;//这里不会出栈，因为后面可能还会用到
            stack.push(i);//入栈
        }

        return ints;
    }


}


