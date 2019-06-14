package com.canzhang.sample;

import com.canzhang.sample.manager.view.webview.CookieTestManager;

/**
 * @Description: java  main 测试
 * @Author: canzhang
 * @CreateDate: 2019/5/15 18:22
 */
public class MainTest {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
//       String s =  CookieTestManager.getTopDomain("https://data.oa.canzhang.com/live/exhibition/left.html");
       String s =  CookieTestManager.getTopDomain(".canzhang.com");
       System.out.print(s);
    }


}
