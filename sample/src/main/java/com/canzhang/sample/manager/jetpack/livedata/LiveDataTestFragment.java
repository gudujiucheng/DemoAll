package com.canzhang.sample.manager.jetpack.livedata;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试从activity 拿数据  livedata
 */
public class LiveDataTestFragment extends BaseFragment {

    private static final String TYPE_KEY = "type_key";
    public static final int TYPE_01 = 1;
    public static final int TYPE_02 = 2;
    public static final int TYPE_03 = 3;




    @IntDef({TYPE_01, TYPE_02, TYPE_03})
    public @interface Type {

    }

    private View mVRoot;
    private int mType;

    public static LiveDataTestFragment newInstance(@Type int type) {
        LiveDataTestFragment fragment = new LiveDataTestFragment();
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
        mVRoot = inflater.inflate(R.layout.sample_fragment_test, container, false);
        initView(mVRoot);
        return mVRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    private void initView(View view) {
        TextView textView = view.findViewById(R.id.tv_test);
        textView.setText("当前位置" + mType);
        //这里注意要使用宿主activity创建Provider 这样才能共享数据
        TestViewModel  mTestViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);//取出model 这里是根据类名获取的
        MutableLiveData<String> nameEvent = mTestViewModel.getNameEvent();
        nameEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i("LIVE_DATA", "onChanged: s = " + s);
                textView.setText(String.format("%s s:%s", textView.getText(), s));
            }
        });


    }
}
