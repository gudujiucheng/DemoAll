package com.example.administrator.demoall.MMKV;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.R;
import com.tencent.mmkv.MMKV;

import java.util.Arrays;

/**
 * https://github.com/Tencent/MMKV/wiki/android_tutorial_cn
 */
public class MMKVActivity extends AppCompatActivity {

    public static  final  String MMKV_TAG = "MMKV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmkv);
        SharedPreferences sharedPreferences = getSharedPreferences("Test",MODE_PRIVATE);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = sharedPreferences.getString("Test","没获取到");
                Log.e("Test","MMKVActivity :"+s);
            }
        });
        findViewById(R.id.tv_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                testMMKV();
//                deleteAndQuery();
//                createSelfMMKV();
//                moreProgress();

                //在指定进程启动service  会重新走一次application的on create
//                startService(new Intent(MMKVActivity.this,MyService.class));
                sharedPreferences.edit().putString("Test1","我是来自进程1的数据").apply();
                Log.e("Test","MMKVActivity :插入数据成功");
                startActivity(new Intent(MMKVActivity.this,ProcessActivity.class));

            }
        });


    }

    /**
     * 这里首先要知道 sp在普通多进程会存在什么问题，2多进程实现原理
     */
    private void moreProgress() {

    }


    /**
     * 区别存储，根据tag单独创建自己的实例
     */
    private void createSelfMMKV() {
        //如果不同业务需要区别存储，也可以单独创建自己的实例：
        MMKV mmkv = MMKV.mmkvWithID("MyID");
        mmkv.encode("bool", true);
        System.out.println("allKeys: " + Arrays.toString(mmkv.allKeys()));

        //原来的
        MMKV kv = MMKV.defaultMMKV();
        System.out.println("allKeys: " + Arrays.toString(kv.allKeys()));

//        allKeys: [bool]
//        allKeys: [char, double, string, float, bytes]
    }

    /**
     * 删除、查询、查询所有的、是否包含
     */
    private void deleteAndQuery() {
        MMKV kv = MMKV.defaultMMKV();
        //查询所有的key
        System.out.println("allKeys: " + Arrays.toString(kv.allKeys()));

        //移除一个key
        kv.removeValueForKey("bool");
        System.out.println("bool: " + kv.decodeBool("bool"));
        //查询所有的key
        System.out.println("allKeys: " + Arrays.toString(kv.allKeys()));
//        I/System.out: allKeys: [string, bool, float, long, int, bytes, double]
//        I/System.out: bool: false
//        I/System.out: allKeys: [string, float, long, int, bytes, double]


        //支持同时移除多个
        kv.removeValuesForKeys(new String[]{"int", "long"});
        System.out.println("allKeys: " + Arrays.toString(kv.allKeys()));
//        I/System.out: allKeys: [string, float, long, int, bytes, double]
//        I/System.out: allKeys: [string, float, bytes, double]

        // 支持查看是否包含某个key值
        boolean hasBool = kv.containsKey("bool");
        System.out.println(hasBool);
    }

    /**
     *
     * 基础使用
     *
     * 支持的基础类型 boolean、int、long、float、double、byte[]
     * 支持的其他类型：String、Set<String>
     */
    private void testMMKV() {
        MMKV kv = MMKV.defaultMMKV();


        char s = 'a';
        kv.encode("char",s);//不支持char 类型，会被自动转换成int 类型

        System.out.println("char: " +  kv.decodeInt("char"));
        input(s);

        //-----------------------分割线---------------------------
        kv.encode("bool", true);
        System.out.println("bool: " + kv.decodeBool("bool"));

        kv.encode("int", Integer.MIN_VALUE);
        System.out.println("int: " + kv.decodeInt("int"));

        kv.encode("long", Long.MAX_VALUE);
        System.out.println("long: " + kv.decodeLong("long"));

        kv.encode("float", -3.14f);
        System.out.println("float: " + kv.decodeFloat("float"));

        kv.encode("double", Double.MIN_VALUE);
        System.out.println("double: " + kv.decodeDouble("double"));

        kv.encode("string", "Hello from mmkv");
        System.out.println("string: " + kv.decodeString("string"));

        byte[] bytes = {'m', 'm', 'k', 'v'};
        kv.encode("bytes", bytes);
        System.out.println("bytes : " + new String(kv.decodeBytes("bytes")));
    }

    private void input(int a){
        System.out.println("a: " +a);
    }
}
