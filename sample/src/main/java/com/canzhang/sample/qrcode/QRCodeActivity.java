package com.canzhang.sample.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;
import com.example.base.utils.ImageUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 二维码生成测试
 * 参考文章 ：https://www.cnblogs.com/xch-yang/p/9642255.html
 */
public class QRCodeActivity extends BaseActivity {
    ConstraintLayout constraintLayout;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_qrcode);
        constraintLayout = findViewById(R.id.root_view);
        compositeDisposable = new CompositeDisposable();
        addView();



    }


    private void addView() {
        final ImageView imageView = new ImageView(this);
        //将转换的动作放到子线程
        Disposable subscribe = Observable.just("xxxx")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) {//转换base64
                        log("线程" + Thread.currentThread().getName() + " " + s);
                        Bitmap qrCodeBitmap = QrCodeUtils.createQRCodeBitmap("测试", 800, 800, "UTF-8", "H", "1", Color.BLACK, Color.WHITE);

                        if (qrCodeBitmap != null) {
                            String stringBase64 = ImageUtils.bitmapToBase64(qrCodeBitmap);
                            return stringBase64;
                        }

                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//可以切换多次
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        log("线程：" + Thread.currentThread().getName()+"  base64:" + s );
                        Bitmap bitmap = ImageUtils.base64ToBitmap(s);
                        imageView.setImageBitmap(bitmap);

                    }
                });
        compositeDisposable.add(subscribe);

        constraintLayout.addView(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
