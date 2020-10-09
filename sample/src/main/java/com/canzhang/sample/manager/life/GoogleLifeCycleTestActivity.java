package com.canzhang.sample.manager.life;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.canzhang.sample.R;

public class GoogleLifeCycleTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_google_life_cycle_test);
        //添加观察者，建立关联
        getLifecycle().addObserver(new MyLocationListener(this));

        /**
         * AppCompatActivity与V4中的Fragment都已默认实现了LifeCyclerOwner接口，基本上可以应对所有的情况了，
         *
         * 如果是自定义的类，想实现生命周期的管理则可以实现LifecycleOwner。 具体参见：https://www.jianshu.com/p/78532ac86db6
         */
    }
}