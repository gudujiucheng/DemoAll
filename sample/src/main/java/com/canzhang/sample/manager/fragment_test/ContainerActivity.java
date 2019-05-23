package com.canzhang.sample.manager.fragment_test;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.fragment_test.fragment.TestFragment;
import com.example.base.base.BaseActivity;

/**
 * 简书：https://www.jianshu.com/p/808ea22eb5dc
 *
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


    /**
     * The specified child already has a parent. You must call removeView() on the child's parent first.
     * 发生场景： 需要持有几个fragment，来回快速切换（包含回退），并且有有转场动画的场景下，就会出现此问题
     */
    private Fragment fragment00, fragment01, fragment02;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void openTestFragment(boolean isNeedAnimation, int type) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        //设置替换和退栈的动画
        if (isNeedAnimation) {
            ft.setCustomAnimations(R.anim.sample_anim_right_in, R.anim.sample_anim_right_out, R.anim.sample_anim_left_in, R.anim.sample_anim_left_out);
        }
        Fragment tempFragment = null;
        switch (type) {
            case 0:
                fragment00 = manager.findFragmentByTag("" + type);
                if (fragment00 == null) {
                    fragment00 = TestFragment.newInstance(type);
                } else {
                    Log.d("Test", "fragment00 不为空");
                }
                tempFragment = fragment00;
                break;
            case 1:
                fragment01 = manager.findFragmentByTag("" + type);
                if (fragment01 == null) {
                    fragment01 = TestFragment.newInstance(type);
                } else {
                    Log.d("Test", "fragment01 不为空");
                }
                tempFragment = fragment01;
                break;
            case 2:
                fragment02 = manager.findFragmentByTag("" + type);
                if (fragment02 == null) {
                    fragment02 = TestFragment.newInstance(type);
                } else {
                    Log.d("Test", "fragment02 不为空");
                }
                tempFragment = fragment02;
                break;
            default:
                Log.d("Test", "异常");
                tempFragment = TestFragment.newInstance(type);
                break;
        }


        ft.replace(R.id.fl_container, tempFragment, "" + type);
        if (isNeedAnimation) {
            ft.addToBackStack(null);
        }
        if (isDestroyed() || isFinishing()) {
            return;
        }
        ft.commitAllowingStateLoss();
    }

    public int i = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void switchFragment(View view) {
        openTestFragment(true, i % 3);
        i++;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (i > 0)
            i--;
    }

    public void createFragment(View view) {//创建fragment 观察生命周期
        new TestFragment();
    }
}
