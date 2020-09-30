package com.canzhang.sample.manager.permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;


public class PermissionFragment extends BaseFragment {
    private static final String DANGEROUS_ACTION = "zc.intent.action.DANGER_TEST";
    private static final String DANGEROUS_PERMISSION = "zc.permission.DANGEROUS_TEST";
    private static final String NORMAL_PERMISSION = "zc.permission.NORMAL_TEST";

    private static final String NORMAL_ACTION = "zc.intent.action.NORMAL_TEST";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_permission_test, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {


        view.findViewById(R.id.bt_check_phone_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isHasPermission = checkReadPhoneState();
                showToast("权限：" + isHasPermission);
            }
        });
        view.findViewById(R.id.bt_get_phone_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE},
//                        1001);
                //注意在fragment请求权限，不要使用ActivityCompat的方法，会导致onRequestPermissionsResult 调用不到。
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        1001);
            }
        });

        /**
         * 测试自定义权限（第一步就是要在清单文件内加入自定义权限声明）
         */
        view.findViewById(R.id.bt_get_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(NORMAL_ACTION));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        view.findViewById(R.id.bt_get_danger_no_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(DANGEROUS_ACTION));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        view.findViewById(R.id.bt_get_danger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(mContext, DANGEROUS_PERMISSION)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]
                                    {DANGEROUS_PERMISSION}, 1002);
                        } else {
                            startActivity(new Intent(DANGEROUS_ACTION));
                        }
                    } else {
                        startActivity(new Intent(DANGEROUS_ACTION));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public boolean checkReadPhoneState() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isRefuse = grantResults[0] == PackageManager.PERMISSION_DENIED;
        Log.e("Test", "requestCode:" + requestCode + " isRefuse:" + isRefuse);
        for (String permission : permissions) {
            Log.e("Test", "permissions:" + permission);
        }

        switch (requestCode) {
            case 1002://自定义权限
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(DANGEROUS_ACTION));
                }
                break;
        }


    }
}
