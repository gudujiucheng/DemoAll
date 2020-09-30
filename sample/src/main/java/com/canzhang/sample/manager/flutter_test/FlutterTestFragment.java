package com.canzhang.sample.manager.flutter_test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

//import io.flutter.facade.Flutter;


public class FlutterTestFragment extends BaseFragment {


    public static Fragment newInstance() {
        Fragment fragment = new FlutterTestFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_flutter_test, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        LinearLayout llContainer = view.findViewById(R.id.ll_container);
        view.findViewById(R.id.bt_jump_flutter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               View  flutterView= Flutter.createView((Activity) mContext,getLifecycle(),"route1");
//                llContainer.addView(flutterView);
            }
        });

    }

}
