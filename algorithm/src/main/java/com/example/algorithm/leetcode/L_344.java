package com.example.algorithm.leetcode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * https://leetcode-cn.com/problems/reverse-string/
 */
public class L_344 {
    public static void main(String[] args) {
        reverseString(new char[]{'H', 'a', 'n', 'n', 'a', 'h' });
    }

    public static void reverseString(char[] s) {
        for (int i = 0; i < s.length; i++) {
            if (i == s.length - 1 - i ||i==s.length-i) {
                return;
            }
            char start = s[i];
            char end = s[s.length - 1 - i];
            s[i] = end;
            s[s.length - 1 - i] = start;

        }

        for (int i = 0; i <s.length ; i++) {
            System.out.print(s[i]);
        }



    }

}
