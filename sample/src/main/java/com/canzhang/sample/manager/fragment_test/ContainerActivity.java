package com.canzhang.sample.manager.fragment_test;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.fragment_test.fragment.TestFragment;
import com.example.base.base.BaseActivity;

/**
 * @Description: fragment 容器
 * @Author: canzhang
 * @CreateDate: 2019/4/9 20:35
 */
public class ContainerActivity extends BaseActivity {
    private FrameLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_container);
        mContainer = findViewById(R.id.fl_container);
    }





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void openTestFragment(boolean isNeedAnimation, int type) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        //设置替换和退栈的动画
        if (isNeedAnimation) {
            ft.setCustomAnimations(R.anim.sample_anim_right_in, R.anim.sample_anim_right_out, R.anim.sample_anim_left_in, R.anim.sample_anim_left_out);
        }
        TestFragment  testFragment = (TestFragment) manager.findFragmentByTag(""+type);
        if (testFragment == null) {
            testFragment = TestFragment.newInstance(type);
        }else{
            Log.d("Test","不为空:"+testFragment.toString());
        }

        ft.replace(R.id.fl_container, testFragment, ""+type);
        if (isNeedAnimation) {
            ft.addToBackStack(null);
        }
        if (isDestroyed() ||isFinishing()) {
            return;
        }
        ft.commitAllowingStateLoss();
    }

    public int i = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void switchFragment(View view) {
        openTestFragment(true,i);
        i++;
    }
}
