package com.canzhang.sample.manager.kotlin;

public class JavaKotlinTest {
    public static void main(String[] args) {
        //java 调用则必须要通过 instance  更多参见：https://www.jianshu.com/p/a5b0da8bbba3?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
        KotlinKTestManager.StaticTest.instance.getNameStatic();
        KotlinKTestManager.StaticTest.instance.speakStatic("哈哈哈哈");
    }
}
