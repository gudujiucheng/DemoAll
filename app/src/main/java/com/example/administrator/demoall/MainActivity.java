package com.example.administrator.demoall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.demoall.filemanager.FileTestActivity;
import com.example.administrator.demoall.myadapter.BaseAdapter;
import com.example.administrator.demoall.myadapter.BaseViewHolder;
import com.example.administrator.demoall.myadapter.test.TestAdapter;
import com.example.administrator.demoall.myadapter.test.TestBean;
import com.meituan.android.walle.WalleChannelReader;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 这里需要改造，添加上来Arouter
 * TODO 以后多一些大模块的分支 相互解耦，方便不需要的时候可以屏蔽掉，提高编译速度
 * TODO 添加base模块
 */
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
//                startActivity(new Intent(MainActivity.this,RxjavaTestActivity.class));
//                startActivity(new Intent(MainActivity.this,StorageActivity.class));
//                showTipsDialog(MainActivity.this, "xxxxxxx");
//                showOpenSettingTipsDialog(MainActivity.this,"xxxxxxx");
//                startActivity(new Intent(MainActivity.this,MMKVActivity.class));
                startActivity(new Intent(MainActivity.this,FileTestActivity.class));
//                startActivity(new Intent(MainActivity.this,WebviewActivity.class));
//                startService(new Intent(MainActivity.this,PreLoadService.class));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<TestBean> lists = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            lists.add(new TestBean().setType(i%2));
        }
//        recyclerView.setAdapter(new BaseAdapter<TestBean,BaseViewHolder>(R.layout.item_other,lists) {
//
//
//            @Override
//            protected void convert(BaseViewHolder helper, TestBean item) {
//
//            }
//        });


         BaseAdapter adapter;
        recyclerView.setAdapter(adapter = new TestAdapter(lists));
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, int position) {
                Toast.makeText(MainActivity.this,"当前位置："+position,Toast.LENGTH_SHORT).show();
            }
        });




//        recyclerView.setAdapter(new BaseQuickAdapter(lists) {
//            @Override
//            protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, Object item) {
//
//            }
//
//            @Override
//            protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, String item) {
//
//            }
//        });

    }


    public static void showTipsDialog(final Activity mActivity, String tips) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppThemeDialog);
        builder.setMessage(tips)
                .setPositiveButton("确定", null)
                .setCancelable(true);
        builder.create().show();
    }

    public static void showOpenSettingTipsDialog(final Activity mActivity, String tips) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppThemeDialog);
        builder.setMessage(tips)
                .setPositiveButton("h", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("d", null).setCancelable(false);
        builder.create().show();
    }

    public void showChannel(View view) {
        //获取渠道
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        Toast.makeText(this,"当前包的渠道是："+channel,Toast.LENGTH_LONG).show();
    }
}

