package com.canzhang.sample.manager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.statusbar.FullscreenActivity;
import com.canzhang.sample.utils.AppUtils;
import com.example.simple_test_annotations.MarkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 小测试
 */
@MarkManager(value = "其他测试")
public class OtherTestDemoManager extends BaseManager {

    private List<String> mList = new ArrayList<>();

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        super.getSampleItem(activity);
        List<ComponentItem> list = new ArrayList<>();
        list.add(testJson());
        list.add(testStatusBar());
        list.add(testAddUrlParams());
        list.add(testScheme());
        list.add(testJiXing());
        list.add(testAppStatus());
        list.add(testGenerate());
        list.add(testLauncher());
        list.add(testTimeMillis());
        list.add(clearAppData(activity));
        list.add(methodTest());
        list.add(jsonTest());
        list.add(mainTest());
        return list;
    }


    private ComponentItem testJson() {
        return new ComponentItem("json解析（测试老的仓库、百科解析代码异常）", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 全标准json （原来没有针对这个名字做特殊处理，会被当成 object对待，解析到第二级的时候会报错）
                String str = "{\"weapon_category\":{\"2\":\"步枪\",\"3\":\"狙击枪\",\"4\":\"机枪\",\"5\":\"冲锋枪\",\"6\":\"霰弹枪\",\"7\":\"副武器\",\"8\":\"近战武器\",\"9\":\"投掷武器\"},\"weaponSubCategorys\":{\"4\":{\"545730837\":\"RPK机枪\",\"569944754\":\"ULTIMAX-100\",\"636632646\":\"M60\",\"627144989\":\"MG3\",\"594617926\":\"加特林机枪\",\"537432322\":\"刘易斯轻机枪\",\"647881692\":\"M249 MINIMI\"},\"5\":{\"632181826\":\"P90\",\"541814031\":\"UMP45\",\"585040663\":\"UZI\",\"537836702\":\"Vector\",\"639802501\":\"汤姆逊冲锋枪\"}},\"last_timestamp\":1670396864,\"path\":\"https:\\/\\/mcdn.gtimg.com\\/bbcdn\\/cfhd\\/serial\\/\",\"list\":[{\"iWeaponId\":\"644249529\",\"szModel\":\"1\",\"szCate\":\"8\",\"szName\":\"蝴蝶刀\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"暂时未知\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"6442495291_small.png\",\"szBigPic\":\"6442495291_small.png\",\"szScreenPic\":\"6442495293.png\",\"iArmPower\":\"1\",\"iArmDegree\":\"1\",\"iArmFireRate\":\"1\",\"iArmStable\":\"1\",\"iArmHandMove\":\"1\",\"iArmThrough\":\"1\",\"iArmPortable\":\"1\",\"iArmChangeBomb\":\"1\",\"iArmBomb\":\"1\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"1\",\"szAccBomb\":\"1\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"10\",\"weaponSubCategory\":\"\",\"notShow\":0},{\"iWeaponId\":\"588363690\",\"szModel\":\"1\",\"szCate\":\"3\",\"szName\":\"SCAR-SSR\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"5000\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"588363690_small.png\",\"szBigPic\":\"588363690.png\",\"szScreenPic\":\"\",\"iArmPower\":\"0\",\"iArmDegree\":\"0\",\"iArmFireRate\":\"0\",\"iArmStable\":\"0\",\"iArmHandMove\":\"0\",\"iArmThrough\":\"0\",\"iArmPortable\":\"0\",\"iArmChangeBomb\":\"0\",\"iArmBomb\":\"0\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"\",\"szAccBomb\":\"\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"\",\"weaponSubCategory\":\"\",\"notShow\":0}]}";

                //weaponSubCategorys 整个是个字符串 （这个也不行，因为是没有针对这个名字处理，所以默认会当成object，也就是weaponSubCategorys 必须是个object ）
                str = "{\"weapon_category\":{\"2\":\"步枪\",\"3\":\"狙击枪\",\"4\":\"机枪\",\"5\":\"冲锋枪\",\"6\":\"霰弹枪\",\"7\":\"副武器\",\"8\":\"近战武器\",\"9\":\"投掷武器\"},\"weaponSubCategorys\":\"{\\\"4\\\":{\\\"545730837\\\":\\\"RPK机枪\\\",\\\"569944754\\\":\\\"ULTIMAX-100\\\",\\\"636632646\\\":\\\"M60\\\",\\\"627144989\\\":\\\"MG3\\\",\\\"594617926\\\":\\\"加特林机枪\\\",\\\"537432322\\\":\\\"刘易斯轻机枪\\\",\\\"647881692\\\":\\\"M249 MINIMI\\\"},\\\"5\\\":{\\\"632181826\\\":\\\"P90\\\",\\\"541814031\\\":\\\"UMP45\\\",\\\"585040663\\\":\\\"UZI\\\",\\\"537836702\\\":\\\"Vector\\\",\\\"639802501\\\":\\\"汤姆逊冲锋枪\\\"}}\",\"last_timestamp\":1670396864,\"path\":\"https:\\/\\/mcdn.gtimg.com\\/bbcdn\\/cfhd\\/serial\\/\",\"list\":[{\"iWeaponId\":\"644249529\",\"szModel\":\"1\",\"szCate\":\"8\",\"szName\":\"蝴蝶刀\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"暂时未知\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"6442495291_small.png\",\"szBigPic\":\"6442495291_small.png\",\"szScreenPic\":\"6442495293.png\",\"iArmPower\":\"1\",\"iArmDegree\":\"1\",\"iArmFireRate\":\"1\",\"iArmStable\":\"1\",\"iArmHandMove\":\"1\",\"iArmThrough\":\"1\",\"iArmPortable\":\"1\",\"iArmChangeBomb\":\"1\",\"iArmBomb\":\"1\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"1\",\"szAccBomb\":\"1\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"10\",\"weaponSubCategory\":\"\",\"notShow\":0},{\"iWeaponId\":\"588363690\",\"szModel\":\"1\",\"szCate\":\"3\",\"szName\":\"SCAR-SSR\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"5000\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"588363690_small.png\",\"szBigPic\":\"588363690.png\",\"szScreenPic\":\"\",\"iArmPower\":\"0\",\"iArmDegree\":\"0\",\"iArmFireRate\":\"0\",\"iArmStable\":\"0\",\"iArmHandMove\":\"0\",\"iArmThrough\":\"0\",\"iArmPortable\":\"0\",\"iArmChangeBomb\":\"0\",\"iArmBomb\":\"0\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"\",\"szAccBomb\":\"\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"\",\"weaponSubCategory\":\"\",\"notShow\":0}]}";

                // weaponSubCategorys：{"name":"xxxx 这里改成字符串"}
                str ="{\"weapon_category\":{\"2\":\"步枪\",\"3\":\"狙击枪\",\"4\":\"机枪\",\"5\":\"冲锋枪\",\"6\":\"霰弹枪\",\"7\":\"副武器\",\"8\":\"近战武器\",\"9\":\"投掷武器\"},\"weaponSubCategorys\":{\"4\":\"{\\\"545730837\\\":\\\"RPK机枪\\\",\\\"569944754\\\":\\\"ULTIMAX-100\\\",\\\"636632646\\\":\\\"M60\\\",\\\"627144989\\\":\\\"MG3\\\",\\\"594617926\\\":\\\"加特林机枪\\\",\\\"537432322\\\":\\\"刘易斯轻机枪\\\",\\\"647881692\\\":\\\"M249 MINIMI\\\"}\",\"5\":\"{\\\"632181826\\\":\\\"P90\\\",\\\"541814031\\\":\\\"UMP45\\\",\\\"585040663\\\":\\\"UZI\\\",\\\"537836702\\\":\\\"Vector\\\",\\\"639802501\\\":\\\"汤姆逊冲锋枪\\\"}\"},\"last_timestamp\":1670396864,\"path\":\"https:\\/\\/mcdn.gtimg.com\\/bbcdn\\/cfhd\\/serial\\/\",\"list\":[{\"iWeaponId\":\"644249529\",\"szModel\":\"1\",\"szCate\":\"8\",\"szName\":\"蝴蝶刀\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"暂时未知\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"6442495291_small.png\",\"szBigPic\":\"6442495291_small.png\",\"szScreenPic\":\"6442495293.png\",\"iArmPower\":\"1\",\"iArmDegree\":\"1\",\"iArmFireRate\":\"1\",\"iArmStable\":\"1\",\"iArmHandMove\":\"1\",\"iArmThrough\":\"1\",\"iArmPortable\":\"1\",\"iArmChangeBomb\":\"1\",\"iArmBomb\":\"1\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"1\",\"szAccBomb\":\"1\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"10\",\"weaponSubCategory\":\"\",\"notShow\":0},{\"iWeaponId\":\"588363690\",\"szModel\":\"1\",\"szCate\":\"3\",\"szName\":\"SCAR-SSR\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"5000\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"588363690_small.png\",\"szBigPic\":\"588363690.png\",\"szScreenPic\":\"\",\"iArmPower\":\"0\",\"iArmDegree\":\"0\",\"iArmFireRate\":\"0\",\"iArmStable\":\"0\",\"iArmHandMove\":\"0\",\"iArmThrough\":\"0\",\"iArmPortable\":\"0\",\"iArmChangeBomb\":\"0\",\"iArmBomb\":\"0\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"\",\"szAccBomb\":\"\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"\",\"weaponSubCategory\":\"\",\"notShow\":0}]}";

                //带上品级
                str = "{\"weapon_category\":{\"2\":\"步枪\",\"3\":\"狙击枪\",\"4\":\"机枪\",\"5\":\"冲锋枪\",\"6\":\"霰弹枪\",\"7\":\"副武器\",\"8\":\"近战武器\",\"9\":\"投掷武器\"},\"propLevels\":{\"1\":\"传说级\",\"2\":\"王者级\",\"3\":\"炫金级\",\"4\":\"英雄级\",\"5\":\"源级\",\"6\":\"挑战级\",\"7\":\"精英级\",\"8\":\"通用级\",\"9\":\"强化级\",\"10\":\"普通\"},\"weaponSubCategorys\":{\"4\":\"{\\\"545730837\\\":\\\"RPK机枪\\\",\\\"569944754\\\":\\\"ULTIMAX-100\\\",\\\"636632646\\\":\\\"M60\\\",\\\"627144989\\\":\\\"MG3\\\",\\\"594617926\\\":\\\"加特林机枪\\\",\\\"537432322\\\":\\\"刘易斯轻机枪\\\",\\\"647881692\\\":\\\"M249 MINIMI\\\"}\",\"5\":\"{\\\"632181826\\\":\\\"P90\\\",\\\"541814031\\\":\\\"UMP45\\\",\\\"585040663\\\":\\\"UZI\\\",\\\"537836702\\\":\\\"Vector\\\",\\\"639802501\\\":\\\"汤姆逊冲锋枪\\\"}\"},\"last_timestamp\":1670396864,\"path\":\"https:\\/\\/mcdn.gtimg.com\\/bbcdn\\/cfhd\\/serial\\/\",\"list\":[{\"iWeaponId\":\"644249529\",\"szModel\":\"1\",\"szCate\":\"8\",\"szName\":\"蝴蝶刀\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"暂时未知\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"6442495291_small.png\",\"szBigPic\":\"6442495291_small.png\",\"szScreenPic\":\"6442495293.png\",\"iArmPower\":\"1\",\"iArmDegree\":\"1\",\"iArmFireRate\":\"1\",\"iArmStable\":\"1\",\"iArmHandMove\":\"1\",\"iArmThrough\":\"1\",\"iArmPortable\":\"1\",\"iArmChangeBomb\":\"1\",\"iArmBomb\":\"1\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"1\",\"szAccBomb\":\"1\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"10\",\"weaponSubCategory\":\"\",\"notShow\":0},{\"iWeaponId\":\"588363690\",\"szModel\":\"1\",\"szCate\":\"3\",\"szName\":\"SCAR-SSR\",\"iRaity\":\"1\",\"iSaleCate\":\"1\",\"iSalePrice\":\"5000\",\"szShelfTime\":\"1\",\"szDesc\":\"1\",\"szSeries\":\"1\",\"szSmallPic\":\"588363690_small.png\",\"szBigPic\":\"588363690.png\",\"szScreenPic\":\"\",\"iArmPower\":\"0\",\"iArmDegree\":\"0\",\"iArmFireRate\":\"0\",\"iArmStable\":\"0\",\"iArmHandMove\":\"0\",\"iArmThrough\":\"0\",\"iArmPortable\":\"0\",\"iArmChangeBomb\":\"0\",\"iArmBomb\":\"0\",\"iArmRange\":\"1\",\"iArmLoadBomb\":\"1\",\"szAccPower\":\"1\",\"szAccDegree\":\"1\",\"szAccFireRate\":\"1\",\"szAccStable\":\"1\",\"szAccHandMove\":\"1\",\"szAccThrough\":\"1\",\"szAccPortable\":\"1\",\"szAccChangeBomb\":\"\",\"szAccBomb\":\"\",\"szCovHeader\":\"1\",\"szCovArm\":\"1\",\"szCovAbdomen\":\"1\",\"szCovLeg\":\"1\",\"szStore\":\"1\",\"propLevel\":\"\",\"weaponSubCategory\":\"\",\"notShow\":0}]}";

                BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes())));
                JsonReader reader = new JsonReader(in);
                try {
                    reader.beginObject();
                    while (reader.hasNext()) {

                        String name = reader.nextName();

                        Log.e("can_test", "name:" + name);

                        if ("last_timestamp".equals(name)) {
                            long lastModify = reader.nextLong() * 1000l;
                            Log.d("can_test", " last_timestamp:" + lastModify);
                        } else if ("path".equals(name)) {
                            String path = reader.nextString();
                            Log.d("can_test", "name:" + name + " path:" + path);
                        } else if ("list".equals(name)) {
                            reader.beginArray();
                            while (reader.hasNext()) {
                                Map<String, Object> map = readObject(reader);
                            }
                            reader.endArray();
                        } else {
                            reader.beginObject();
                            JSONObject tmp = new JSONObject();
                            while (reader.hasNext()) {
                                String k = reader.nextName();
                                String v = reader.nextString();
                                Log.d("can_test", "k:" + k + " v:" + v);
                                tmp.put(k, v);
                            }
                            reader.endObject();
                        }

                    }
                    reader.endObject();

                    reader.close();
                    in.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static Map<String, Object> readObject(JsonReader reader) {

        Map<String, Object> valuesMap = new HashMap<>();
        try {
            reader.beginObject();


            while (reader.hasNext()) {
                String k = reader.nextName();

                JsonToken jsonToken = reader.peek();
                if (jsonToken == JsonToken.BEGIN_ARRAY) {

                    reader.beginArray();
                    JSONArray array = new JSONArray();

                    while (reader.hasNext()) {
                        String item = reader.nextString();
                        array.put(item);
                    }

                    valuesMap.put(k, array.toString());

                    reader.endArray();

                } else if (jsonToken == JsonToken.BEGIN_OBJECT) {

                    Map<String, Object> map = readObject(reader);
                    valuesMap.put(k, map);

                } else if (jsonToken == JsonToken.BOOLEAN) {

                    boolean v = reader.nextBoolean();
                    valuesMap.put(k, v);

                } else if (jsonToken == JsonToken.NUMBER) {

                    String strNum = reader.nextString();
                    if (strNum.contains(".")) {
                        double v = parseDouble(strNum, 0d);
                        valuesMap.put(k, v);
                    } else {
                        long v = parseLong(strNum, 0L);
                        valuesMap.put(k, v);
                    }

                } else if (jsonToken == JsonToken.NULL) {
                    //do nothing
                    reader.nextNull();
                } else {
                    try {
                        String v = reader.nextString();
                        valuesMap.put(k, v);
                    } catch (Exception e) {
                        Log.e("CAN_TEST", e.getMessage(), e);
                    }

                }
            }
            reader.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return valuesMap;
    }


    public static long parseLong(String value, long def) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException var4) {
            return def;
        }
    }

    public static double parseDouble(String value, double def) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException var4) {
            return def;
        }
    }

    int index = 0;

    private ComponentItem testStatusBar() {
        return new ComponentItem("状态栏测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, FullscreenActivity.class));
            }
        });
    }

    private ComponentItem testAddUrlParams() {
        return new ComponentItem("测试uri拼接参数", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String url = "";
//                switch (index % 3) {
//                    case 0:
//                        url = "https://www.baidu.com/s?";//无参数
//                        break;
//                    case 1:
//                        url = "https://www.baidu.com/s?wd=haha";//有参数
//                        break;
//                    case 2:
//                        url = "https://www.baidu.com/s?key=haha";//有同名参数 (测试结果：不会去重)
//                        break;
//                }
//                Uri.Builder builder = Uri.parse(url).buildUpon();
//                builder.appendQueryParameter("key", "{\"result\":0,\"returnCode\":0,\"returnMsg\":\"\"}");
//                index++;
//                Log.e("CAN_TEST", builder.toString());


                String url = "";
                switch (index % 3) {
                    case 0:
                        url = "test?";
                        break;
                    case 1:
                        url = "/test";
                        break;
                    case 2:
                        url = "https://www.baidu.com/s?y=#777&key=haha&x=8";//有同名参数 (测试结果：不会去重)
                        break;
                }
                index++;
                HashMap<String, Object> map = new HashMap<>();
                map.put("key", "haha");
                url = appendParamsToUrl(url, map);
                Log.e("CAN_TEST", url);


            }
        });
    }


    private static String appendParamsToUrl(String url, Map<String, Object> params) {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return url;
        }

        Uri uri = Uri.parse(url);
        if (uri.isOpaque()) {
            return url;
        }

        if (!url.contains("?")) {
            url = url + "?";
        }
        String urlStart = url.split("\\?")[0];
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        for (String name : queryParameterNames) {
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            //去重
            params.put(name, uri.getQueryParameter(name));
        }

        String paramsStr = parseUrlQuery(params, true);

        return urlStart + "?" + paramsStr;

    }


    public static String parseUrlQuery(Map<String, Object> params, boolean encodeKV) {
        if (params == null || params.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, Object>> entrySet = params.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object object = entry.getValue();
            String value = (object == null ? "" : object.toString());
            if (encodeKV) {
                key = getUrlEncodeValue(key);
                value = getUrlEncodeValue(value);
            }
            sb.append(key)
                    .append("=")
                    .append(value)
                    .append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String getUrlEncodeValue(String origValue) {
        if (origValue == null) {
            origValue = "";
        }
        try {
            return URLEncoder.encode(origValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(origValue);
        }
    }

    public static String getURLDecodeValue(String origValue) {
        if (origValue == null) {
            origValue = "";
        }
        try {
            return URLDecoder.decode(origValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLDecoder.decode(origValue);
        }
    }

    private ComponentItem testScheme() {
        return new ComponentItem("通过scheme打开页面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) {
                    return;
                }
//                http://tapd.woa.com/gamehelper_client_doc/markdown_wikis/show/#1220383062001980329

                //cf 打开url 的方法   其中要求 三个参数都必须有值
//                String url = "cfpage://webopenapi?action=20002&gameId=1&url=https://bb.img.qq.com/clsq/protocol/cf/protocol.html";
//                String url = "cfpage://webopenapi?action=20002&gameId=10011&url=https://cf.qq.com/act/5326/a20210323ld/index.html";
//                String url = "cfpage://webopenapi/startApp";//可以打开 但是应用在后台的时候 似乎唤起不到前台
//                String url = "cfpage://webopenapi";//可以
//                String url = "cfpage://";//不可以

//                ConfigManager.getInstance().putStringConfig(GlobalData.ArgumentsKey.KEY_START_APP_FROM_BROWSWER_CONFIG, uri.toString());

                JSONObject button = new JSONObject();
                try {
                    button.put("type", 10005);
//                    button.put("belongGameId", 10011); //可以不要了

//                    button.put("uri","com.tencent.gamehelper.ui.moment.TopicMomentActivity");
//                    button.put("uri","momentTopic");
//                    JSONObject param =  new JSONObject();
//                    param.put("id",482);
//                    param.put("name","#Qqqqqqq#");
//                    button.put("param",param);


//                    button.put("uri","moment_detail?feedId=35262&type=0");


//                    button.put("uri","infodetail?iInfoId=1065106881");


//                    button.put("uri","homepage?userId=497257227");


//                    JSONObject param = new JSONObject();
////                    param.put("userId", 497257227);
//                    param.put("TO_USER_ID", 497257227);
//                    button.put("param", param);


                    button.put("uri", "make_team_page");


                    //dnf改版后，这样传递会导致乱码，需要使用兼容方案
//                    button.put("uri", "submitMoment?&topicGameId=20003&topic="+URLEncoder.encode("#什么道具值得买#", "utf-8"));

                    //兼容方案
//                    button.put("uri", "submitMoment");
//                    JSONObject param = new JSONObject();
//                    param.put("topic", "#什么道具值得买#");
//                    param.put("topicGameId", 20003);
////                    param.put("fromPage", "topic");
//                    button.put("param", param);


                    //跳转到个人主页=作品tab
//                    button.put("uri", "com.tencent.gamehelper.ui.main.MainActivity");
//                    JSONObject param = new JSONObject();
//                    param.put("type", "10015");//这个主要是用来定位跳转什么大页面的  比如主页、情报站等
//                    param.put("subTabName", "works");
//                    button.put("param", param);

                    //跳转到作品发布
//                    button.put("uri", "submit_work");
//                    JSONObject param = new JSONObject();
//                    //1是图文  2 是视频
//                    param.put("submit_work_type", 2 );
//                    param.put("submit_work_game_id", 20003 );
//                    button.put("param", param);


                    //跳转精选下的某个子tab
//                    button.put("uri", "com.tencent.gamehelper.ui.main.MainActivity");
//                    JSONObject param = new JSONObject();
//                    param.put("subTabName", "视频");
//                    button.put("param", param);//由于默认第一个就是精选，所以这里的type 可以不填写


                    //跳转到动态 精选tab
//                    button.put("uri", "com.tencent.gamehelper.ui.main.MainActivity");
//                    JSONObject param = new JSONObject();
//                    param.put("type", "10027");//这个主要是用来定位跳转什么大页面的（底栏类型）  比如主页、情报站等
//                    param.put("subTabName", "精选");
//                    button.put("param", param);


                    //顶部带角色切换打开web页面
//                    button.put("type", 10032);
//                    button.put("uri", "https://bb.img.qq.com/clsq/protocol/cf/protocol.html");


                    //不带角色 打开web页面，这个不用type类型
//                    String  uri = "https://bb.img.qq.com/clsq/protocol/cf/protocol.html";
//                    String url = "cfpage://webopenapi?action=20002&url=" + uri;

                    Log.e("TEST", button.toString());
                    String url = "cfpage://webopenapi?action=20003&button=" + URLEncoder.encode(button.toString(), "utf-8")
                            /* +"&reportParam="+URLEncoder.encode("{\"modId\":550,\"xxx\":true,\"source\":\"shouyou\"}", "utf-8")*/;


                    Log.e("TEST", url);
                    Log.e("TEST", URLEncoder.encode(url, "utf-8"));
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mActivity.startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private ComponentItem testJiXing() {
        return new ComponentItem("获取桌面应用包名", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) {
                    return;
                }
                isSupport();


            }
        });
    }

    private boolean isSupport() {
        Intent launchIntent = mActivity.getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());
        if (launchIntent == null) {
            Log.e("CAN_TEST", "Unable to find launch intent for package " + mActivity.getPackageName());
            return false;
        }
        ComponentName sComponentName = launchIntent.getComponent();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = mActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String currentHomePackage = resolveInfo.activityInfo.packageName;
            Log.d("CAN_TEST", "currentHomePackage:" + currentHomePackage);
        }
        //Turns out framework does not guarantee to put DEFAULT Activity on top of the list.
        ResolveInfo resolveInfoDefault = mActivity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        validateInfoList(resolveInfoDefault, resolveInfos);

        for (ResolveInfo resolveInfo : resolveInfos) {
            String currentHomePackage = resolveInfo.activityInfo.packageName;
            Log.e("CAN_TEST", "currentHomePackage:" + currentHomePackage);
        }
        return true;
    }


    /**
     * Making sure the default Home activity is on top of the returned list
     *
     * @param defaultActivity default Home activity
     * @param resolveInfos    list of all Home activities in the system
     */
    private static void validateInfoList(ResolveInfo defaultActivity, List<ResolveInfo> resolveInfos) {
        int indexToSwapWith = 0;
        int resolveInfosSize = 0;
        if (resolveInfos != null) {
            resolveInfosSize = resolveInfos.size();
            for (int i = 0; i < resolveInfosSize; i++) {
                ResolveInfo resolveInfo = resolveInfos.get(i);
                String currentActivityName = resolveInfo.activityInfo.packageName;
                if (currentActivityName.equals(defaultActivity.activityInfo.packageName)) {
                    indexToSwapWith = i;
                }
            }
        }
        if (resolveInfosSize > 1 && resolveInfosSize > indexToSwapWith) {
            Collections.swap(resolveInfos, 0, indexToSwapWith);
        }
    }

    private ComponentItem testAppStatus() {
        return new ComponentItem("测试app当前运行状态", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) {
                    return;
                }
                Log.e("CAN_TEST", "isRunBackground:" + AppUtils.isRunBackground(mActivity) + " isRunForeground:" + AppUtils.isRunForeground(mActivity));
            }
        });
    }

    private ComponentItem testLauncher() {
        return new ComponentItem("启动别的activity 页面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1604226916},\"gameId\":10011,\"notify\":1}"));//过期的消息
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1604914721},\"gameId\":20003,\"notify\":1}"));//该游戏id下没有角色
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1604916160},\"gameId\":10011,\"notify\":1}"));//
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1605151191},\"gameId\":10011,\"notify\":1}"));//
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1606110600},\"gameId\":1001,\"notify\":1}"));//
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1606893900},\"gameId\":10011,\"notify\":1}"));//
                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1612348200},\"gameId\":1001,\"notify\":1}"));//
                mActivity.startActivity(intent);
                String a = null;
                String b = "1" + a;
                Log.e("CAN_TEST", "b:" + b);
            }
        });
    }

    private ComponentItem testGenerate() {
        return new ComponentItem("另外一种生成参数的方法", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//具体参见                https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/push-basic-capability#h2-1576658726104
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // Scheme协议（pushscheme://com.huawei.codelabpush/deeplink?）需要开发者自定义
                intent.setData(Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?"));
                // 往intent中添加参数，用户可以根据自己的需求进行添加参数
                intent.putExtra("name", "{\"action\":\"sysNotification\",\"param\":{\"id\":1603792829},\"gameId\":10011,\"notify\":1}");
                intent.putExtra("age", 180);
                // 必须带上该Flag
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
                // 打印出的intentUri值就是设置到推送消息中intent字段的值
                Log.d("TEST", "intentUri:" + intentUri);
            }
        });
    }


    private ComponentItem testTimeMillis() {
        /**
         * https://github.com/instacart/truetime-android/issues
         */
        return new ComponentItem("修改系统时间、测试时间戳", "时间戳会随着更改系统的时间而更改，如果需要精准，可获取服务器时间，或者使用第三方获取库", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                showToast("当前时间:" + timeStr);
            }
        });
    }

    private ComponentItem clearAppData(Activity activity) {
        return new ComponentItem("清除 app数据，等价重装", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.clearAppUserData(activity);

            }
        });
    }


    private ComponentItem methodTest() {
        return new ComponentItem("入参修改测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentItem testItem = new ComponentItem("测试", (View.OnClickListener) null);
                print(testItem);
                Log.e("Test", "原来的值：" + testItem.name);
            }
        });
    }

    private void print(ComponentItem item) {
        Log.e("Test", "变化前：" + item.name);
        item = new ComponentItem("xxxxx", (View.OnClickListener) null);
        Log.e("Test", "变化后：" + item.name);


    }

    private ComponentItem jsonTest() {
        return new ComponentItem("jsonObject 并发修改异常", "1、不同线程操作jsonObject可能会出现并发修改异常（这里需要多开几个线程，更好复现，狂点击也行）" +
                "\n2、这是因为jsonObject本身是维护了一个LinkedHashMap，同时读写就可能发生并发修改异常", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final JSONObject jsonObject = new JSONObject();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        modifyJson(jsonObject);
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject.put(Thread.currentThread().getName(), "线程2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                log("当前json串：" + jsonObject.toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject.put(Thread.currentThread().getName(), "线程3");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                log("当前json串2：" + jsonObject.toString());
            }
        });
    }

    private void modifyJson(JSONObject jsonObject) {

        try {
            jsonObject.put("1", "张灿");
            jsonObject.put("2", "zhangke");
            jsonObject.put("3", "zdk77777");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private ComponentItem mainTest() {
        return new ComponentItem("回调到主线程的测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                for (int i = 0; i < 100; i++) {
                    final int j = i;
                    int time = rand.nextInt(100) + 1;//1-100随机取值
                    log("随机time： " + time);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mList.add(j + "");
                        }
                    }, time);

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(mList, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                if (Integer.valueOf(o1) > Integer.valueOf(o2)) {
                                    return 1;
                                } else {
                                    return -1;
                                }

                            }
                        });
                        for (int i = 0; i < mList.size(); i++) {
                            log("当前值： " + mList.get(i) + " ");
                        }
                    }
                }, 1000);


            }
        });
    }


}
