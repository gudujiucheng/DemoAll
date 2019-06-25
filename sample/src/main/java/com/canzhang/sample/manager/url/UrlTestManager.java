package com.canzhang.sample.manager.url;

import android.app.Activity;
import android.net.Uri;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        list.add(test04());
        return list;
    }


    /**
     * 带有这种 # 号 解析会有异常,导致参数值，以及后面的参数取不出来。
     */
    private String url = "https://cc.sale.canzhang.com/1902181929/index.html?canzhang_channel=AM.NADD2019050700028761.NADP2017121500001001#NADS2017121500001003#AI&event_id=AM.NADD2019050700028761";

    private String url02 = "https://cc.sale.canzhang.com/1902181929/index.html?canzhang_channel=AM.NADD2019050700028761.NADP2017121500001001&event_id=AM.NADD2019050700028761";

    private String url03 = "https://mall.m.fenqile.com/jump.html#/activity?url=https://pay.m.fenqile.com/index.html#/activity/temp-credit?_OSC=aaabbbccc0001&test=xxx";

    private ComponentItem test04() {

        return new ComponentItem("getQueryParameter（替换特殊字符#）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomUri uri = CustomUri.parse(url03);
                String url = uri.getQueryParameter("url");
                String _OSC = uri.getQueryParameter("_OSC");
                String test = uri.getQueryParameter("test");

                log("url:" + url + " _OSC:" + _OSC + " test:" + test);

                //测试fql中截取部分，防止异常出现
                Set<String> names;
                names = new HashSet<>();
                String tempUrl = uri.toString();
                log("tempUrl:"+tempUrl);
                String vars = tempUrl.substring(tempUrl.indexOf("?") + 1, tempUrl.length());
                log("vars:"+vars);
                String[] keys = vars.split("&");

                if (keys.length == 0) {
                    return;
                }
                for (String key : keys) {
                    log("key:" + key);
                    names.add(key.split("=")[0]);
                }

                log("names:" + names.toString());
            }
        });
    }

    private ComponentItem test() {

        return new ComponentItem("fql getQueryParameter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FUri parse = FUri.parseWithCheck(url);
                String canzhang_channel = parse.newGetQueryParameter("canzhang_channel");
                String event_id = parse.newGetQueryParameter("event_id");

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
                log("canzhang_channel:" + canzhang_channel + " event_id:" + event_id + " urlParams:" + urlParams.toString());
            }
        });
    }


}
