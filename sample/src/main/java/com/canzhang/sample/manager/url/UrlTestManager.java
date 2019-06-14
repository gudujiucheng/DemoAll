package com.canzhang.sample.manager.url;

import android.app.Activity;
import android.net.Uri;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * url 相关测试
 */
public class UrlTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        list.add(test02());
        list.add(test03());
        return list;
    }


    /**
     * 带有这种 # 号 解析会有异常,导致参数值，以及后面的参数取不出来。
     */
    private String url = "https://cc.sale.canzhang.com/1902181929/index.html?canzhang_channel=AM.NADD2019050700028761.NADP2017121500001001#NADS2017121500001003#AI&event_id=AM.NADD2019050700028761";

    private String url02 = "https://cc.sale.canzhang.com/1902181929/index.html?canzhang_channel=AM.NADD2019050700028761.NADP2017121500001001&event_id=AM.NADD2019050700028761";

    private ComponentItem test() {

        return new ComponentItem("fql getQueryParameter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FUri parse = FUri.parse(url);
                String canzhang_channel = parse.getQueryParameter("canzhang_channel");
                String event_id = parse.getQueryParameter("event_id");

                log("canzhang_channel:" + canzhang_channel + " event_id:" + event_id);
            }
        });
    }

    private ComponentItem test02() {

        return new ComponentItem("getQueryParameter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri parse = Uri.parse(url02);
                String canzhang_channel = parse.getQueryParameter("canzhang_channel");
                String event_id = parse.getQueryParameter("event_id");
                log("canzhang_channel:" + canzhang_channel + " event_id:" + event_id);
            }
        });
    }


    private ComponentItem test03() {

        return new ComponentItem("getQueryParameter03", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> urlParams = UrlParse.getUrlParams(url);
                String canzhang_channel = urlParams.get("canzhang_channel");
                String event_id = urlParams.get("event_id");
                log("canzhang_channel:" + canzhang_channel + " event_id:" + event_id +" urlParams:"+urlParams.toString());
            }
        });
    }


}
