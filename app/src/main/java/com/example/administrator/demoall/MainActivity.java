package com.example.administrator.demoall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.rxjava.RxjavaTestActivity;
import com.example.administrator.demoall.weex.WeexTestActivity;
import com.example.myview.ChannelView.ChannelActivity;

import com.example.administrator.demoall.webview.WebviewActivity;
import com.example.myview.test.ViewTestActivity;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
public class MainActivity extends AppCompatActivity {
    public static String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,ChannelActivity.class));
//                startActivity(new Intent(MainActivity.this, ViewTestActivity.class));
//                startActivity(new Intent(MainActivity.this,WeexTestActivity.class));
//              TestActivity.startTestActivity(MainActivity.this);
                startActivity(new Intent(MainActivity.this,RxjavaTestActivity.class));
            }
        });

    }




}

