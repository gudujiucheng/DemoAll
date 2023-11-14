package com.example.administrator.demoall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.demoall.fqladapter.BaseTypeFooterAdapter;
import com.example.administrator.demoall.fqladapter.test.CouponItemAdapter;
import com.example.administrator.demoall.myadapter.test.TestBean;
import com.example.base.utils.FileUtil;
import com.example.base.utils.PictureUtils;
import com.google.android.material.tabs.TabLayout;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showIntent();
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tab_coupon);
        for (int i = 0; i < 3; i++) {
            tabLayout.addTab(tabLayout.newTab().setText("哈哈哈" + i));
        }


        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<TestBean> lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add(new TestBean().setType(i % 2));
        }

        CouponItemAdapter adapter;
        recyclerView.setAdapter(adapter = new CouponItemAdapter(this, lists, new BaseTypeFooterAdapter.OnItemClickListener<TestBean>() {
            @Override
            public void onItemClick(View view, TestBean item, int position) {
                Toast.makeText(MainActivity.this, "当前位置：" + position, Toast.LENGTH_SHORT).show();
            }
        }));
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseTypeFooterAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Toast.makeText(MainActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
            }
        }, recyclerView);


    }

    private void showIntent() {
        Intent intent = getIntent();
        Log.e("Test", "---------------->>getPackage" + intent.getPackage());
        Log.e("Test", "---------------->>getCategories" + intent.getCategories());
        Log.e("Test", "---------------->>getClipData" + intent.getClipData());
        Log.e("Test", "---------------->>getScheme" + intent.getScheme());
        Log.e("Test", "---------------->>getData" + intent.getData());
        Log.e("Test", "---------------->>getDataString" + intent.getDataString());
        Log.e("Test", "---------------->>getType" + intent.getType());
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        FloatingView.get().detach(this);
    }

    public void test(View view) {

//        LoadingDialog.get(this).show();

        //fenqile:// 这个是打不开的
        //fenqile://app 这样才能打得开
//        Intent action = new Intent(Intent.ACTION_VIEW, Uri.parse("fenqile://app/webview?url=\"https://www.baidu.com/\""));
//        startActivity(action);
//
//        FloatingView floatingView = new FloatingView();
//        floatingView.customView(R.layout.lx_floating_view);
//        floatingView.customView(new LXCustomView(MainActivity.this));
//        floatingView.getView().setLayoutParams(floatingView.getParams());

//        FloatingView.get().attach(this).add();


        //测试miui相关问题。


//        testMiui12();
//        test2();
//        isMiUiOS();

//        testAES();

//        startActivity(new Intent(this, PermissionActivity.class));
        //测试从公共目录获取文件
//        AndPermission.with(this)
//                .runtime()
//                .permission(Permission.READ_EXTERNAL_STORAGE)
//                .onGranted(permissions -> {
//                    //这个Download 似乎比较特殊，在android 13上 ，读写都能成功（不同app之间）
//                    String path = Environment.getExternalStorageDirectory() + "/Download/actionhelp.json";
//                    String s =  FileUtil.readFile(path,1024);
//                    Toast.makeText(this,"读取结果："+s,Toast.LENGTH_LONG).show();
//
//                })
//                .onDenied(permissions -> {
//                    Toast.makeText(this,"权限获取被拒绝",Toast.LENGTH_LONG).show();
//                })
//                .start();


        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                    String s = FileUtil.readStringFromPublicDownLoadPath(this, "cannnzhang");
                    Toast.makeText(this,"读取结果："+s,Toast.LENGTH_LONG).show();
                })
                .onDenied(permissions -> {
                    Toast.makeText(this,"权限获取被拒绝",Toast.LENGTH_LONG).show();
                })
                .start();



    }


    public void test02(View view) {

        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                   FileUtil.saveStringToPublicDownLoadPath(this, "testxxx","来自app 存储的内容");
                })
                .onDenied(permissions -> {
                    Toast.makeText(this,"权限获取被拒绝",Toast.LENGTH_LONG).show();
                })
                .start();
    }



    int i = 0;
    String str = null;

    private void testAES() {
        if (i % 2 == 0) {
            str = AESUtil.aesEncryptString("{\"device_info\":{\"device_id\":\"1E909482-1BFF-46DE-89DF-01345BF50319\",\"device_type\":\"ios\"},\"offset\":0,\"offset_id\":\"0\",\"limit\":10,\"action\":\"shoppingContentNew\",\"id\":\"PRPG201905070031005\",\"cache_version\":\"0\"}", "f@p&3&d33bf@xi!n");
            Toast.makeText(MainActivity.this, "加密结果：" + str, Toast.LENGTH_SHORT).show();
        } else {
            String s = null;
            try {
                s = AESUtil.aesDecryptString(str,"f@p&3&d33bf@xi!n");
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "有异常", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "解密结果：" + s, Toast.LENGTH_SHORT).show();
        }
        i++;
    }

    @SuppressLint("MissingPermission")
    private void testMiui12() {
        String result = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("MIUI_Test", "androidId:" + result);
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA)
                .onGranted(permissions -> {
                    Log.e("Test", "权限获取成功");
                    Toast.makeText(MainActivity.this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    getPhoneMsg();


                })
                .onDenied(permissions -> {
                    Log.e("Test", "权限获取被拒绝");
                    Toast.makeText(MainActivity.this, "权限获取被拒绝", Toast.LENGTH_SHORT).show();
                })
                .start();
    }

    @SuppressLint("MissingPermission")
    private void getPhoneMsg() {
        TelephonyManager manager = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
//        StringBuilder builder = new StringBuilder();
//        String deviceId = manager.getDeviceId();//不会统计 （风控手机会统计）
//        String simSerialNumber = manager.getSimSerialNumber();//不会统计（风控手机会统计）
//        String subscriberId = manager.getSubscriberId();//不会统计（风控手机会统计）
        String line1Number = manager.getLine1Number();//会触发两次------------（两个手机都会有统计）

//        String simOperator = manager.getSimOperator();//都不会统计
//        int phoneType = manager.getPhoneType();//都不会统计
//        int networkType = manager.getNetworkType();//都不会统计
//        String networkOperator = manager.getNetworkOperator();//都不会统计

//        String imei = null;
//        String serial = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            imei = manager.getImei(0);// 风控收集会记录一次
//            serial = Build.getSerial();//两个手机都不会调用
//        } else {
//            Method method = null;
//            try {
//                method = manager.getClass().getMethod("getImei", int.class);
//                imei = (String) method.invoke(manager, 0);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            serial = Build.SERIAL;
//        }

//        builder
//                .append("deviceId：").append(deviceId).append("\n")
//                .append("simSerialNumber：").append(simSerialNumber).append("\n")
//                .append("simOperator：").append(simOperator).append("\n")
//                .append("phoneType：").append(phoneType).append("\n")
//                .append("networkType：").append(networkType).append("\n")
//                .append("networkOperator：").append(networkOperator).append("\n")
//                .append("subscriberId：").append(subscriberId).append("\n")
//                .append("line1Number：").append(line1Number).append("\n")
//                .append("imei：").append(imei).append("\n")
//                .append("serial：").append(serial).append("\n");
//
//
//        Log.e("MIUI_Test", builder.toString());
    }


    public void test2() {
        String result = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1001);
        } else {
            Log.e("Test", "已经有权限了，直接打开");
            Toast.makeText(this, "已经有权限了，直接打开", Toast.LENGTH_SHORT).show();
            getPhoneMsg();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限获取成功 x", Toast.LENGTH_SHORT).show();
                getPhoneMsg();
            } else {
                Toast.makeText(this, "权限获取失败 x", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * android p手机测试不会抛异常
     *
     * @return
     */
    private static boolean isMiUiOS() {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String miuiVersion = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            Log.e("isMiUiOS", miuiVersion);
            return (!TextUtils.isEmpty(miuiVersion));
        } catch (Exception var4) {
            Log.e("isMiUiOS", var4.getMessage());
        }
        return false;
    }


    public void test03(View view) {


        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_footer_loading);
                    PictureUtils.saveBitmapToPicture(this,bitmap,"can/test/sample_footer_loading.png");
                })
                .onDenied(permissions -> {
                    Toast.makeText(this, "权限获取失败 x", Toast.LENGTH_SHORT).show();
                })
                .start();
    }

    public void test04(View view) {
        PictureUtils.getImagesInRelativePath(this, "can/test/");
    }

    public void test05(View view) {
        PictureUtils.deleteImagesInRelativePath(this, "can/test/");
    }
}

