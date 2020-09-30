package com.canzhang.sample.manager.view.font;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

import java.lang.reflect.Method;

/**
 * 字体实验：注意老版本内存问题，针对老版本调用一次就是创建一份内存占用，并且不会回收，针对新版本是没有这个问题的，具体版本界限不详。
 * https://www.jianshu.com/p/d75418a38500
 */
public class FontTestFragment extends BaseFragment {


    public static Fragment newInstance() {
        Fragment fragment = new FontTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_font, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        final TextView tvFont = view.findViewById(R.id.tv_font);
        view.findViewById(R.id.bt_font_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFont.setTypeface(getMoneyTypeface(mContext, "fonts/DINPro-Medium.otf"));
                tvFont.setText("字体：fonts/DINPro-Medium.otf  2019年5月17日15:06:21");
            }
        });
        view.findViewById(R.id.bt_font_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFont.setTypeface(getMoneyTypeface(mContext, "fonts/Farrington-7B-Qiqi.ttf"));
                tvFont.setText("字体：fonts/Farrington-7B-Qiqi.ttf  2019年5月17日15:06:21");
            }
        });
        /**
         * 循环设置字体，查看内存占用状况
         *
         * adb shell dumpsys meminfo com.canzhang.sample 查看内存分配情况
         */
        view.findViewById(R.id.bt_font_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 10; i++) {//快速循环是不会创建多份内存占用，应该是对应文件加锁了导致的。
                    tvFont.setTypeface(getMoneyTypeface(mContext, "fonts/DINPro-Medium.otf"));
                }

            }
        });

    }


    public static Typeface getMoneyTypeface(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }


    /**
     * 测试字体缓存的key 在不同的activity是否相同（结论：是相同的：android-touchwiz-com.android.webview-com.canzhang.sample- ）
     * @param context
     */
    public static void printKey(Context context) {
        AssetManager mgr = context.getAssets();
        //1.加载Class对象
        try {
            Class stuClass = Class.forName("android.content.res.AssetManager");
            //这是个native方法，并且是hide标记了的，直接调用会报错，只能通过反射调用了
            Method m = stuClass.getMethod("getAssignedPackageIdentifiers");
            final SparseArray<String> pkgs = (SparseArray<String>) m.invoke(mgr);
            final StringBuilder builder = new StringBuilder();
            final int size = pkgs.size();
            for (int i = 0; i < size; i++) {
                builder.append(pkgs.valueAt(i));
                builder.append("-");
            }
            Log.d("FontTestFragment", "-------------->>>>>Key: " + builder.toString());
        } catch (Exception e) {
            Log.d("FontTestFragment", "Exception:"+e.getMessage() );
        }


    }

}
