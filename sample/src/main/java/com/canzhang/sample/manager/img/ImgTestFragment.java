package com.canzhang.sample.manager.img;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;
import com.example.base.base.BaseFragment;

import java.io.File;


public class ImgTestFragment extends BaseFragment {


    public static Fragment newInstance() {
        return new ImgTestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_img_test, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        File fileStreamPath = getActivity().getFileStreamPath("test_img.png");
        String absolutePath = fileStreamPath.getAbsolutePath();
        String path = fileStreamPath.getPath();
        LogUtils.log("absolutePath:" + absolutePath);
        view.findViewById(R.id.bt_get_img_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.getExif(path);
            }
        });

    }

}
