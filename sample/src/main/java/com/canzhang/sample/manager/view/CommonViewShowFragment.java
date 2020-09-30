package com.canzhang.sample.manager.view;

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


/**
 * 通用展示类型的view展示
 */
public class CommonViewShowFragment extends BaseFragment {
    private static final String TYPE_KEY = "type_key";
    public static final int DASH_LINE = 1;


    @IntDef({DASH_LINE})
    public @interface Type {

    }

    private int mType;

    public static Fragment newInstance(@Type int type) {
        Fragment fragment = new CommonViewShowFragment();
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
        View view = inflater.inflate(R.layout.sample_common_view_show_fragment, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        switch (mType) {
            case DASH_LINE:
                view.findViewById(R.id.ll_dash_line).setVisibility(View.VISIBLE);
                break;
        }

    }

}
