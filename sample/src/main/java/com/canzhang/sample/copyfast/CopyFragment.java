package com.canzhang.sample.copyfast;

import android.os.Bundle;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class CopyFragment extends BaseFragment {
    private static final String TYPE_KEY = "type_key";
    public static final int TYPE_01 = 1;
    public static final int TYPE_02 = 2;
    public static final int TYPE_03 = 3;


    @IntDef({TYPE_01, TYPE_02, TYPE_03})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    private int mType;

    public static Fragment newInstance(@Type int type) {
        Fragment fragment = new CopyFragment();
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
        View view = inflater.inflate(R.layout.sample_fragment_copy, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {

    }

}
