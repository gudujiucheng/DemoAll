package com.canzhang.sample.manager.fragment_test.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;


public class TestFragment extends BaseFragment {

    private static final String TYPE_KEY = "type_key";
    public static final int TYPE_01 = 1;
    public static final int TYPE_02 = 2;
    public static final int TYPE_03 = 3;


    @IntDef({TYPE_01, TYPE_02, TYPE_03})
    public @interface Type {

    }

    private View mVRoot;
    private int mType;

    public static TestFragment newInstance(@Type int type) {
        TestFragment fragment = new TestFragment();
        Bundle info = new Bundle();
        info.putInt(TYPE_KEY, type);
        fragment.setArguments(info);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mType = arguments.getInt(TYPE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mVRoot == null) {
            log("mVRoot 为空");
            mVRoot = inflater.inflate(R.layout.sample_fragment_test, container, false);
            initView(mVRoot);
        } else {
            log("mVRoot 不为空");
            ViewGroup parentView = (ViewGroup) mVRoot.getParent();
            if (parentView != null) {
//                parentView.removeView(mVRoot);
                log("mVRoot  parentView 不为空");
            } else {
                log("mVRoot  parentView 为空");
            }

        }
        initData();
        return mVRoot;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup parentView = (ViewGroup) mVRoot.getParent();
        if (parentView != null) {
//                parentView.removeView(mVRoot);
            log("onResume    mVRoot  parentView 不为空");
        } else {
            log("onResume    mVRoot  parentView 为空");
        }
    }

    private void initData() {

    }

    private void initView(View view) {
        TextView textView = view.findViewById(R.id.tv_test);
        textView.setText("当前位置" + mType);
    }

}
