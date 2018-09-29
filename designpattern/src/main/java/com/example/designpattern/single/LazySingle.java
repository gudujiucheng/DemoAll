package com.example.designpattern.single;

public class LazySingle {

    private static LazySingle single;

    private LazySingle() {

    }

    public static LazySingle getInstance() {
        if (single == null) {
            synchronized (LazySingle.class) {
                if (single == null) {
                    single = new LazySingle();
                }

            }
        }
        return single;
    }

}
