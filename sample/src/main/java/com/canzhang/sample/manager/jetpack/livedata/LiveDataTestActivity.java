package com.canzhang.sample.manager.jetpack.livedata;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;

/**
 * 官网：https://developer.android.com/topic/libraries/architecture/viewmodel
 */
public class LiveDataTestActivity extends BaseActivity {

    private TextView mTvName;
    TestViewModel mTestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_live_data_test);
        mTvName = findViewById(R.id.tv_name);

        //这个数据仓库是存在activtiy持有的一个map里面的，activity没有销毁之前，都是存在的（当然如果是activity变更配置 也还是存在的，变更配置添加判断，不会清理数据）
        mTestViewModel = new ViewModelProvider(this).get(TestViewModel.class);//取出model 这里是根据类名获取的
        MutableLiveData<String> nameEvent = mTestViewModel.getNameEvent();
        nameEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i("LIVE_DATA", "onChanged: s = " + s);
                mTvName.setText(s);
            }
        });

        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mTestViewModel.getNameEvent().postValue("哈哈哈哈哈哈 切到主线程回调");//切换到主线程回调
//                mTestViewModel.getNameEvent().setValue("哈哈哈哈哈哈 当前子线程回调");//在当前线程回调
            }
        }).start();
    }

    private Fragment fragment00, fragment01, fragment02;

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
                    fragment00 = LiveDataTestFragment.newInstance(type);
                } else {
                    Log.d("Test", "fragment00 不为空");
                }
                tempFragment = fragment00;
                break;
            case 1:
                fragment01 = manager.findFragmentByTag("" + type);
                if (fragment01 == null) {
                    fragment01 = LiveDataTestFragment.newInstance(type);
                } else {
                    Log.d("Test", "fragment01 不为空");
                }
                tempFragment = fragment01;
                break;
            case 2:
                fragment02 = manager.findFragmentByTag("" + type);
                if (fragment02 == null) {
                    fragment02 = LiveDataTestFragment.newInstance(type);
                } else {
                    Log.d("Test", "fragment02 不为空");
                }
                tempFragment = fragment02;
                break;
            default:
                Log.d("Test", "异常");
                tempFragment = LiveDataTestFragment.newInstance(type);
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

    public void switchFragment(View view) {
        openTestFragment(true, i % 3);
        i++;
    }
}