package com.canzhang.sample.manager.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    LinearLayout linearLayout;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_qrcode);
        linearLayout = findViewById(R.id.root_view);
        compositeDisposable = new CompositeDisposable();
        addView();
        addViewTest();
        addViewTest02();


    }
    private void addViewTest(){
        final ImageView imageView = new ImageView(this);
        Bitmap qrCodeBitmap = QRCodeEncoder.syncEncodeBarcode("abs1234",300,150,20);
        imageView.setImageBitmap(qrCodeBitmap);
        linearLayout.addView(imageView);
    }

    private void addViewTest02(){//带有文字的条形码 转换成base64 在转换回来 会有问题
        final ImageView imageView = new ImageView(this);
        Bitmap qrCodeBitmap = QRCodeEncoder.syncEncodeBarcode("abs1234",300,150,20);
        String stringBase64 = ImageUtils.bitmapToBase64(qrCodeBitmap);
        Bitmap bitmap = ImageUtils.base64ToBitmap(stringBase64);
        imageView.setImageBitmap(bitmap);
        linearLayout.addView(imageView);
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
//                        Bitmap qrCodeBitmap = QrCodeUtils.createQRCodeBitmap("测试", 800, 800, "UTF-8", "H", "1", Color.BLACK, Color.WHITE);
                        Bitmap qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode("测试",800);
//                        Bitmap qrCodeBitmap = QRCodeEncoder.syncEncodeBarcode("abs哈哈1234",300,150,20);

                        if (qrCodeBitmap != null) {
                            return ImageUtils.bitmapToBase64(qrCodeBitmap);
                        }

                        return "";
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

        linearLayout.addView(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
