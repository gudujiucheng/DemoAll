package com.example.algorithm.leetcode;


import java.util.Stack;

public class L_20 {
    public static void main(String[] args) {
//        String s = "()[()]{}";
//        String s = "((";
        String s =  "){";
        System.out.print("结果：" + isValid(s));


    }

    public static boolean isValid(String s) {
        if (s == null || "".equals(s)) {
            return true;
        }
        char[] chars = s.toCharArray();
        int length = chars.length;
        if (length % 2 != 0) {
            return false;
        }
        Stack<Character> characters = new Stack<>();
        for (int i = 0; i < length; i++) {
            char temp = chars[i];
            switch (temp) {
                case '('://可以使用一个map存储 就方便多了
                case '[':
                case '{':
                    characters.push(temp);
                    break;
                case ')':
                case ']':
                case '}':
                    if(characters.empty()){
                        return false;
                    }
                    Character pop = characters.pop();
                    if (')' == temp) {
                        temp = '(';
                    } else if (']' == temp) {
                        temp = '[';
                    } else if ('}' == temp) {
                        temp = '{';
                    } else {
                        return false;
                    }
                    if (pop != temp) {
                        return false;
                    }

                    break;
                default:
                    return false;

            }


        }
        return characters.empty();
    }


}


