package com.example.javatest.EnumTest;

public class EnumTest {

    public static void main(String[] args) {
        System.out.println(SupportSharePlatform.ALL_SUPPORT_PLATFORM);
    }
    //枚举遍历
    private static void forEnum() {
        SupportSharePlatform[] values = SupportSharePlatform.values();
        for (SupportSharePlatform e : values) {
            System.out.println(e.toString());
        }
    }



}
