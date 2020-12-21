package com.canzhang.sample.manager.view.span;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;


/**
 * 测试 SpannableStringBuilder   图文混排 文字点击  点击效果等等
 */
public class SpanFragment extends BaseFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_span, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        TextView textView = view.findViewById(R.id.tv_test);
        SpannableStringBuilder ssb = new SpannableStringBuilder("我的后面添加图片：  ");
        ssb.setSpan(new ImageSpan(getContext(), R.mipmap.ic_launcher), 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ssb);

        textView = view.findViewById(R.id.tv_test2);
        ssb = new SpannableStringBuilder("我的中 间添加图片  ");
        ssb.setSpan(new ImageSpan(getContext(), R.mipmap.ic_launcher), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ssb);


        textView = view.findViewById(R.id.tv_test3);
        ssb = new SpannableStringBuilder("图片点击事件的处理  ");
        ssb.setSpan(new ImageSpan(getContext(), R.mipmap.ic_launcher), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getContext(), "图片点击事件的处理 ", Toast.LENGTH_SHORT).show();
            }
        }, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ssb);
        //需要添加这一行 点击才会生效，暂时先不管是干啥的  后面在研究
        textView.setMovementMethod(LinkMovementMethod.getInstance());


    }

}
