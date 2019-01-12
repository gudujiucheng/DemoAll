package com.canzhang.sample.manager.weex;

import android.os.Bundle;
import android.view.View;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

public class WeexActivity extends BaseActivity implements IWXRenderListener {



    WXSDKInstance mWXSDKInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weex);

        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
        //本地加载
//        mWXSDKInstance.render("WXSample", WXFileUtils.loadAsset("index2.js",this), null, null, WXRenderStrategy.APPEND_ASYNC);
         String TEST_URL = "http://10.1.18.19:8081/dist/index.js";//对应项目根目录下dist文件夹下的js文件
//         String TEST_URL = "http://10.1.18.19:8081/web/preview.html?page=index.js&wsport=8082";//对应项目根目录下dist文件夹下的js文件
        //网络加载
        mWXSDKInstance.renderByUrl("WXSample", TEST_URL, null, null, WXRenderStrategy.APPEND_ONCE);

    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        setContentView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityDestroy();
        }
    }
}
