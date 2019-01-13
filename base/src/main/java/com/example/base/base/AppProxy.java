package com.example.base.base;

import android.app.Application;

public class AppProxy {


    private AppProxy() {

    }

    private static class SingleHolder {
        //静态的不会被构造多次
        private static final AppProxy single = new AppProxy();
    }

    public static AppProxy getInstance() {
        //只有当用到的时候 才会创建加载
        return SingleHolder.single;
    }

    private Application application;


    public void setApplication(Application application) {

        this.application = application;

    }

    public Application getApplication(){
        return application;
    }


}
