package com.example.javatest.digui;

import java.util.LinkedList;
import java.util.List;

public class MainTest {

    /**
     * 递归求所有子集   顺序一致才叫子集    理解递归
     *
     * @param args
     */
    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3};
        List<List<Integer>> subsets = subsets(nums);
        for (int i = 0; i < subsets.size(); i++) {
            System.out.println("子集：" + i + "______ " + subsets.get(i));
        }
    }

    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new LinkedList<>();
        if (nums.length == 0) {
            return result;
        }
        helper(nums, 0, new LinkedList<>(), result);
        return result;
    }

    private static void helper(int[] nums, int index, LinkedList<Integer> subSet, List<List<Integer>> result) {
        if (index == nums.length) {
            System.out.println("达到长度了："+subSet);
            result.add(new LinkedList<>(subSet));
        } else {
            helper(nums, index + 1, subSet, result);
            subSet.add(nums[index]);
            helper(nums, index + 1, subSet, result);
            subSet.removeLast();
        }
    }


}
