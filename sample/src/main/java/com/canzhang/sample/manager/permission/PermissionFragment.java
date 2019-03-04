package com.canzhang.sample.manager.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;


public class PermissionFragment extends BaseFragment {


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
        Button btCheckPhoneState = view.findViewById(R.id.bt_check_phone_state);
        Button btGetPhoneState = view.findViewById(R.id.bt_get_phone_state);
        btCheckPhoneState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isHasPermission = checkReadPhoneState();
                showToast("权限：" + isHasPermission);
            }
        });
        btGetPhoneState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE},
//                        1001);
                //注意在fragment请求权限，不要使用ActivityCompat的方法，会导致onRequestPermissionsResult 调用不到。
               requestPermissions( new String[]{Manifest.permission.READ_PHONE_STATE},
                        1001);
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

    }
}
