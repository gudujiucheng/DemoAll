package com.canzhang.sample.manager.gif;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

/**
 * 有关gif的测试
 */
public class GifFragment extends BaseFragment {


    public static Fragment newInstance() {
        Fragment fragment = new GifFragment();
        Bundle info = new Bundle();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_gif, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    ImageView ivGif;

    private static int index = 0;
    long startTime;

    public static String gifUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565414279953&di=0d3b6b58386810c22365244f2a228c33&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160409%2F60b9806423ea4053aa74a0f4a10932ce_th.jpg";

    private void initView(View view) {
        ivGif = view.findViewById(R.id.iv_gif);


        view.findViewById(R.id.bt_load_gif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = System.currentTimeMillis();
                GifUtils.loadOneTimeGif(gifUrl, ivGif, new GifUtils.GifListener() {
                    @Override
                    public void gifPlayComplete() {
                        showToast("gif加载完成");
                        Log.e("GIF_TAG", "本次耗时：" + (System.currentTimeMillis() - startTime) + " index:" + index);
                    }
                });
                index++;

            }
        });


    }

    private String getUrl() {
        if (index % 3 == 0) {
            return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565413651068&di=be621b7e88ac9d200a114e4efd67844e&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137b758e1d61ca801219c771357f0.gif";
        } else if (index % 3 == 1) {
            return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565414279953&di=0d3b6b58386810c22365244f2a228c33&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160409%2F60b9806423ea4053aa74a0f4a10932ce_th.jpg";
        } else {
            return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565415175531&di=9c2b70779df944842436603af9824295&imgtype=0&src=http%3A%2F%2Fmmbiz.qpic.cn%2Fmmbiz%2FRIQJDM7oiajdno4vw8Mf2kNBnkcmXpUMwAgia0lsibudxXicYx29ZtQ013scO0JsK5pvHmAoicwwK3ibyCjRNmERsonw%2F640%3Fwx_fmt%3Dgif";
        }
    }


}
