package com.canzhang.sample.manager.lifetest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

/**
 * 内存泄露测试
 */
public class LifeTestFragment extends BaseFragment {
    private TextView textView;
    private OnMsgStatusChangedListener mOnMsgStatusChangedListener = new OnMsgStatusChangedListener() {
        @Override
        public void onMsgStatusChanged(String text) {//非静态内部类，持有外部类的引用，所以可以直接调用外部类的成员变量，或者方法，但是同时处理不当可能会造成内存泄露，
            // 比如此例子，listener持有fragment实例，然后开启点击按钮开启的任务又是耗时任务，等到回调的时候，fragment都可能已经destroy了，但是由于持有引用内存反而一直得不到释放。
            showToast("回调来了");
            if (textView != null) {
                Log.e("Test", "textView 不为空");
            }
            textView.setText(text);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_life_test, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        textView = view.findViewById(R.id.tv_test);
        MsgStatus.getInstance().addListener(mOnMsgStatusChangedListener);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgStatus.getInstance().getStatusFromNet();
                showToast("启动倒计时执行任务");
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Test", "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Test", "onDestroyView");
        //正确做法
        MsgStatus.getInstance().removeListener(mOnMsgStatusChangedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Test", "onDestroy");
    }
}
