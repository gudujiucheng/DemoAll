package com.canzhang.hb.go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.canzhang.hb.R;
import com.canzhang.hb.go.service.ForegroundService;
import com.canzhang.hb.go.utils.Logg;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Map;

/**
 * @author xj_luo
 */
public class MainActivity extends AppCompatActivity {
    private Intent mForegroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handleMaterialStatusBar();

        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> adapterStatusMap = initializationStatus.getAdapterStatusMap();
                for(Map.Entry<String, AdapterStatus> entry : adapterStatusMap.entrySet()){
                    String mapKey = entry.getKey();
                    AdapterStatus mapValue = entry.getValue();
                    Logg.i("CAN_TEST",mapKey+": des="+mapValue.getDescription()+" state="+mapValue.getInitializationState()+" latency="+mapValue.getLatency());
                }

            }
        });


        //启动服务
        if (!ForegroundService.serviceIsLive) {
            // Android 8.0使用startForegroundService在前台启动新服务
            mForegroundService = new Intent(this, ForegroundService.class);
            mForegroundService.putExtra("Foreground", "This is a foreground service.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(mForegroundService);
            } else {
                startService(mForegroundService);
            }
        } else {
            Toast.makeText(this, "前台服务正在运行中...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止服务
//        mForegroundService = new Intent(this, ForegroundService.class);
//        stopService(mForegroundService);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.fragment).navigateUp();
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handleMaterialStatusBar() {
        // Not supported in APK level lower than 21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //window.setStatusBarColor(0xffE46C62);
        window.setStatusBarColor(0xff00E676);

    }
}