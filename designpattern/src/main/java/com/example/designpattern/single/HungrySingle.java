package com.example.designpattern.single;

public class HungrySingle {
    private static  HungrySingle hungrySingle = new HungrySingle();
    private HungrySingle(){

    }

    public static HungrySingle getInstance(){
        return  hungrySingle;
    }
}
