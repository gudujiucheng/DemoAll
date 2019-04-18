package com.canzhang.sample.manager.view.editText;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

/**
 * 测试EditText
 */
public class TestEditTextFragment extends BaseFragment {
    private static final String TYPE_KEY = "type_key";
    private int mType;

    public static Fragment newInstance(int type) {
        Fragment fragment = new TestEditTextFragment();
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
        View view = inflater.inflate(R.layout.sample_fragment_edittext_test, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {

        EditText et = view.findViewById(R.id.et_test);
        final Button bt = view.findViewById(R.id.bt_test);

        bt.setEnabled(false);
        //先注册监听
        et.addTextChangedListener(new TextWatcher() {
            /**
             *
             * @param s 改变之前的值
             * @param start 改变的起始位置
             * @param count
             * @param after
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                log("beforeTextChanged:" + " s:" + s + " start:" + start + " count:" + count + " after:" + after);
            }

            /**
             *
             * @param s 改变之后的值
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                log("onTextChanged:" + " s:" + s + " start:" + start + " count:" + count + " before:" + before);
                if (TextUtils.isEmpty(s)) {
                    bt.setEnabled(false);
                    log("------------------------------------------->>>false");
                } else {
                    bt.setEnabled(true);
                    log("------------------------------------------->>>true");
                }
            }

            /**
             *
             * @param s 这个也是改变后的值
             */
            @Override
            public void afterTextChanged(Editable s) {
                log("onTextChanged:" + " Editable:" + s.toString());
            }
        });
        et.setText("6666666666");


    }

}
