package com.example.algorithm.leetcode;


public class L_224 {
    public static void main(String[] args) {

//        String str = "1 + 1";
        String str =  " 20-1 + 2 ";
       System.out.print("结果："+calculate(str));
    }

    public static int calculate(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        }
        String s1 = s.replaceAll(" ", "");

        int result =0;

        if(s1.contains("(")){


        }else{
            result = jisuan(s1);
        }



        return result ;

    }

    /**
     * 计算表达式的值
     * @param str 没有括号和空格的表达式
     * @return
     */
    private static int jisuan(String str) {
        char[] chars = str.toCharArray();
        int preResult = 0;
        boolean isAdd = true;
        for (int i = 0; i < chars.length; i++) {
            char temp = chars[i];
            if ('+' == temp) {
                isAdd = true;
            } else if ('-' == temp) {
                isAdd = false;
            } else {//数字
                int tempInt = Integer.valueOf(temp+"");
                preResult = isAdd ? preResult + tempInt : preResult - tempInt;
            }
        }

        return preResult;
    }

}


