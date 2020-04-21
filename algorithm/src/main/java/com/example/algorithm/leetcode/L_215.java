package com.example.algorithm.leetcode;


public class L_215 {
    public static void main(String[] args) {
//        int[] arr = new int[]{3, 2, 1, 5, 6, 4};
        int[] arr = new int[]{5, 2, 4, 1, 3, 6,0};

        System.out.print("结果：" + findKthLargest(arr, 2));


    }

    public static int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        for (int i = 0; i <nums.length ; i++) {
            //排序 从小到大
            int temp = nums[i];
            int index= 0;
            int big =temp;
            for (int j = i+1; j <nums.length ; j++) {
                int num = nums[j];
                if(num>big){
                    index = j;
                    big=num;
                }

            }
            if(i== k-1){//优化解法
                return big;
            }
            if(index!=0){
                nums[i]=nums[index];
                nums[index] =temp;
            }
        }
        return nums[k];

    }
}


