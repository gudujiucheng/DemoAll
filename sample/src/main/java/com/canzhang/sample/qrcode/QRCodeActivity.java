package com.canzhang.sample.qrcode;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.canzhang.sample.qrcode.QrCodeUtils.createQRCodeBitmap;

/**
 * 二维码生成测试
 * 参考文章 ：https://www.cnblogs.com/xch-yang/p/9642255.html
 */
public class QRCodeActivity extends BaseActivity {
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_qrcode);
        constraintLayout =  findViewById(R.id.root_view);
        addView();

    }

    private void addView() {
        ImageView imageView =  new ImageView(this);
        Observable.just("xxxx").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {//转换base64

                return null;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });




        imageView.setImageBitmap(createQRCodeBitmap("测试",800, 800,"UTF-8","H", "1", Color.BLACK, Color.WHITE));
        constraintLayout.addView(imageView);
    }
}
