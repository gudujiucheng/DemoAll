package com.canzhang.sample.manager.img;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 图片压缩测试：https://www.jianshu.com/p/cd0cd8a406d4
 */
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
        return view;
    }


    private void initView(View view) {
        ImageView iv = view.findViewById(R.id.iv);
        compress(view);
        RGB_565(view);
        matrix(view);
        inSampleSize(view);
        getImgMsg(view);

    }

    /**
     * 获取图片信息
     *
     * @param view
     */
    private void getImgMsg(View view) {
        view.findViewById(R.id.bt_get_img_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream inputStream = getAssetsInputStream(mContext, "test_img.png");
//                iv.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                copyFileToCacheDir(mContext, inputStream);
            }
        });
    }

    /**
     * RGB_565 压缩法
     *
     * @param view
     */
    private void RGB_565(View view) {
        EditText editText = view.findViewById(R.id.et_test_RGB_565);
        view.findViewById(R.id.bt_get_img_size_RGB_565).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num = Float.valueOf(editText.getText().toString());
                InputStream inputStream = getAssetsInputStream(mContext, "img/test.jpg");
                BitmapFactory.Options options = new BitmapFactory.Options();
                String config;
                if (num == 1) {
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    config = "RGB_565";
                } else if (num == 2) {
                    options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                    config = "ARGB_4444";
                } else {
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    config = "ARGB_8888";
                }

                Bitmap bm = BitmapFactory.decodeStream(inputStream, null, options);
                log("压缩后图片的大小" + (bm.getByteCount() / 1024f / 1024f)
                        + "M 宽度为" + bm.getWidth() + " 高度为" + bm.getHeight()+" 模式："+config);
                bm.recycle();
                bm =null;
            }
        });
    }

    /**
     * 缩放压缩（基于bitmap的 不太适合内存压缩，因为这个时候bitmap已经加载到内存中了）
     *
     * @param view
     */
    private void matrix(View view) {
        EditText editText = view.findViewById(R.id.et_test_matrix);

        view.findViewById(R.id.bt_get_img_size_matrix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num = Float.valueOf(editText.getText().toString());
                InputStream inputStream = getAssetsInputStream(mContext, "img/ic_launcher.png");
                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                Matrix matrix = new Matrix();
                matrix.setScale(num, num);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), matrix, true);

                log("压缩后图片的大小" + (bm.getByteCount() / 1024f / 1024f)
                        + "M 宽度为" + bm.getWidth() + " 高度为" + bm.getHeight() + " 缩放比" + num);
            }
        });
    }

    /**
     * 采样率压缩
     *
     * @param view
     */
    private void inSampleSize(View view) {
        EditText editText = view.findViewById(R.id.et_test_inSampleSize);
        view.findViewById(R.id.bt_get_img_size_inSampleSize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream inputStream = getAssetsInputStream(mContext, "img/ic_launcher.png");
                BitmapFactory.Options options = new BitmapFactory.Options();
                int inSampleSize;
                options.inSampleSize = inSampleSize = Integer.valueOf(editText.getText().toString());
                Bitmap bm = BitmapFactory.decodeStream(inputStream, null, options);
                log("压缩后图片的大小" + (bm.getByteCount() / 1024f / 1024f)
                        + "M 宽度为" + bm.getWidth() + " 高度为" + bm.getHeight() + " 当前采样率为" + inSampleSize);
            }
        });
    }

    /**
     * 有损压缩
     *
     * @param view
     */
    private void compress(View view) {
        EditText editText = view.findViewById(R.id.et_test);
        view.findViewById(R.id.bt_get_img_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream inputStream = getAssetsInputStream(mContext, "img/ic_launcher.png");
                Bitmap bit = BitmapFactory.decodeStream(inputStream);
                log("压缩前 bitmap 大小" + (bit.getByteCount() / 1024f / 1024f)
                        + "M 宽度为" + bit.getWidth() + " 高度为" + bit.getHeight());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int quality = Integer.valueOf(editText.getText().toString());
                //这里如果选择png模式的话，则大小不会变
                bit.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                byte[] bytes = baos.toByteArray();
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                log("压缩后 bitmap 大小" + (bm.getByteCount() / 1024f / 1024f)
                        + "M 宽度为" + bm.getWidth() + " 高度为" + bm.getHeight()
                        + " bytes.length=  " + (bytes.length / 1024) + "KB"
                        + " quality=" + quality);
            }
        });
    }


    /**
     * 根据路径获取Bitmap图片
     *
     * @param context
     * @param path
     * @return
     */
    public InputStream getAssetsInputStream(Context context, String path) {
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open(path);
        } catch (IOException e) {
            e.printStackTrace();
            log(e.getMessage());
        }
        log("get getAssetsInputStream");
        return inputStream;
    }


    public void copyFileToCacheDir(Context context, InputStream inputStream) {

        log("copyFileToCacheDir");
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }

        String filePath = cacheDir.getAbsolutePath() + File.separator + "canzhang" +
                File.separator + "photo" + File.separator;
        String fileName = filePath + System.currentTimeMillis() + ".jpg";
        File tempFile = new File(fileName);
        if (!tempFile.getParentFile().exists()) {
            File parentFile = new File(filePath);
            parentFile.mkdirs();
        }
        try {
            //封装数据源
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //封装目的地
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));

            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);
                bos.flush();
            }
            bos.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            log(e.getMessage());
        }

        /**
         * 测试读取修改图片信息
         */
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "canzhang");//随便塞入没用
        hashMap.put(ExifInterface.TAG_IMAGE_WIDTH, "777");//某些字段没用
        hashMap.put(ExifInterface.TAG_MODEL, "nukia");//可以
        hashMap.put(ExifInterface.TAG_MAKE, "canzhang");//可以
        ImageUtils.setExif(tempFile.getAbsolutePath(), hashMap);
        ImageUtils.getExif(tempFile.getAbsolutePath());


    }
}
