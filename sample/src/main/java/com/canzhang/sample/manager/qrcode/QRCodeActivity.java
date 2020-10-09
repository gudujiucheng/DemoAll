package com.canzhang.sample.manager.qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;
import com.example.base.utils.ImageUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


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
        addViewTest();

        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
        linearLayout.addView(view);
        addViewNoWhiteTest02();
        addViewNoWhiteTest();
        addViewNoWhiteTest03();
//        addView();

//        addViewTest02();
        addV();
        addV1();
        addV2();
        addV3();


    }

    private void addV() {
        final ImageView imageView = new ImageView(this);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageView.setImageBitmap(bmp);
        linearLayout.addView(imageView);
    }

    private void addV1() {
        final ImageView imageView = new ImageView(this);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageView.setImageBitmap(NoWhiteQrCodeUtils.scaleBitmap(bmp,0.5f));
        linearLayout.addView(imageView);
    }
    private void addV2() {//不是裁剪效果 会产生拉伸效果
        final ImageView imageView = new ImageView(this);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageView.setImageBitmap(NoWhiteQrCodeUtils.zoomImage(bmp,502,100));
        linearLayout.addView(imageView);
    }

    private void addV3() {//裁剪
        final ImageView imageView = new ImageView(this);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap tempBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),bmp.getHeight()/2);
        imageView.setImageBitmap(tempBitmap);
        linearLayout.addView(imageView);
    }

    String content = "123abcdefwd";


    int width = 400;
    int height =200;

    private void addViewTest() {
        final ImageView imageView = new ImageView(this);
        Bitmap qrCodeBitmap = QRCodeEncoder.syncEncodeBarcode(content, width, height, 0);
        imageView.setImageBitmap(qrCodeBitmap);
        linearLayout.addView(imageView);
    }

    private void addViewNoWhiteTest() {
        final ImageView imageView = new ImageView(this);
        Bitmap qrCodeBitmap = NoQrCodeUtils.getBarCodeWithoutPadding(width, width, height, content);
        imageView.setImageBitmap(qrCodeBitmap);
        linearLayout.addView(imageView);
    }

    private void addViewNoWhiteTest02() {
        final ImageView imageView = new ImageView(this);
        Bitmap qrCodeBitmap = NoWhiteQrCodeUtils.getBarCodeWithoutPadding(width, height, content);
        imageView.setImageBitmap(qrCodeBitmap);
        linearLayout.addView(imageView);
    }
    private void addViewNoWhiteTest03() {
        final ImageView imageView = new ImageView(this);
        Bitmap qrCodeBitmap = YBNoWhiteQrCodeUtils.getBarCodeWithoutPadding(width, width,height, content);
        imageView.setImageBitmap(qrCodeBitmap);
        linearLayout.addView(imageView);
    }


    private void addViewTest02() {//带有文字的条形码 转换成base64 在转换回来 会有问题
        final ImageView imageView = new ImageView(this);
        Bitmap qrCodeBitmap = QRCodeEncoder.syncEncodeBarcode("abs1234", 300, 150, 20);
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
                        Bitmap qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode("测试", 800);
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
                        log("线程：" + Thread.currentThread().getName() + "  base64:" + s);
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
