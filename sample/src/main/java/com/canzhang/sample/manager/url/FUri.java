package com.canzhang.sample.manager.url;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import org.w3c.dom.Text;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by denny on 2016/4/20.
 */
public class FUri {

    private Uri uri;
    private static final String s = "08d383f559c568de92f5cbbf849bd2b2";

    private FUri(Uri uri) {
        this.uri = uri;
    }

    public static FUri parse(String uriString) {
        return new FUri(Uri.parse(uriString));
    }

    public static FUri parseWithCheck(String uriString) {
        if (!TextUtils.isEmpty(uriString)) {
            if (uriString.contains("#")) {
                uriString = uriString.replaceAll("#", s);
            }

        }
        return new FUri(Uri.parse(uriString));
    }

    public Set<String> getQueryParameterNames() {
        try {
            return uri.getQueryParameterNames();
        } catch (Throwable ignore) {
            return Collections.emptySet();
        }
    }

    public String getQuery() {
        return uri.getQuery();
    }

    public String getQueryParameter(String key) {
        try {
            return uri.getQueryParameter(key);
        } catch (Throwable ignore) {
            return null;
        }
    }

    public String newGetQueryParameter(String key) {
        try {
            String parameter = uri.getQueryParameter(key);
            if(parameter==null){
                return  null;
            }
            return parameter.replaceAll(s,"#");
        } catch (Throwable ignore) {
            return null;
        }
    }

    public String toString() {
        return uri.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Uri)) {
            return false;
        }

        Uri other = (Uri) o;

        return toString().equals(other.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean isHierarchical() {
        return uri.isHierarchical();
    }

    public boolean isAbsolute() {
        return !isRelative();
    }

    public String getScheme() {
        return uri.getScheme();
    }

    public boolean isOpaque() {
        return !isHierarchical();
    }

    public boolean isRelative() {
        return uri.isRelative();
    }

    public void addParameter(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        addParameter(map);
    }

    public void addParameter(Map<String, String> map) {
        String url = toString();
        if (!isHierarchical()) {
            Log.e("FUri", "Uri's value " + url + " cannot addParameter,is not hierarchical");
            return;
        }

        boolean hasFragment = url.contains("#");
        String fragment = "";
        if (hasFragment) {
            fragment = getHashValue();
        }
        int fragmentStartIndex;
        if (hasFragment) {
            fragmentStartIndex = url.indexOf("#");
        } else {
            fragmentStartIndex = url.length();
        }

        String strWithoutFragment;
        if (fragmentStartIndex == -1) {
            return;
        } else {
            strWithoutFragment = url.substring(0, fragmentStartIndex);
        }
        int queryStartIndex = strWithoutFragment.indexOf("?");

        boolean hasQuery = queryStartIndex != -1;
        String query = "";
        String strWithoutParamAndFragment;
        StringBuilder needParam = new StringBuilder();
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        StringBuilder resultParam = new StringBuilder();
        if (hasQuery) {
            query = strWithoutFragment.substring(queryStartIndex);
        }
        boolean andFlag = false;
        String key, value;
        for (Map.Entry<String, String> entry : entrySet) {
            key = entry.getKey();
            value = entry.getValue();
            //如果已经有key了
            if (hasQuery && uri.getQueryParameterNames().contains(key)) {
                String queryValue = uri.getQueryParameter(key);
                if (queryValue == null) {
                    queryValue = "";
                }
                query = query.replace(key + "=" + queryValue, key + "=" + value);
                continue;
            }
            if (andFlag) {
                andFlag = !andFlag;
                needParam.append("&");
            }
            needParam.append(key).append("=").append(value);
        }
        if (hasQuery) {
            strWithoutParamAndFragment = url.substring(0, queryStartIndex);
            resultParam.append(query);
            if (!query.equals("?") && !query.endsWith("&") && !TextUtils.isEmpty(needParam)) {
                resultParam.append("&");
            }
            resultParam.append(needParam);
        } else {
            resultParam.append("?").append(needParam);
            strWithoutParamAndFragment = url.substring(0, fragmentStartIndex);
        }

        StringBuilder resultUrl = new StringBuilder(strWithoutParamAndFragment);
        resultUrl.append(resultParam.toString());
        if (hasFragment) {
            resultUrl.append("#");
            resultUrl.append(fragment);
        }
        uri = Uri.parse(resultUrl.toString());
    }

    public String getSchemeSpecificPart() {
        return uri.getSchemeSpecificPart();
    }

    public String getEncodedSchemeSpecificPart() {
        return uri.getEncodedSchemeSpecificPart();
    }

    public String getAuthority() {
        return uri.getAuthority();
    }

    public String getEncodedAuthority() {
        return uri.getEncodedAuthority();
    }

    public String getUserInfo() {
        return uri.getUserInfo();
    }

    public String getEncodedUserInfo() {
        return uri.getEncodedUserInfo();
    }

    public String getHost() {
        return uri.getHost();
    }

    public int getPort() {
        return uri.getPort();
    }

    public String getPath() {
        return uri.getPath();
    }

    public String getEncodedPath() {
        return uri.getEncodedPath();
    }

    public String getEncodedQuery() {
        return uri.getEncodedQuery();
    }

    public String getHashValue() {
        String url = uri.toString();
        if (url.contains("#")) {
            return url.substring(url.indexOf("#") + 1);
        }
        return "";
    }

    /**
     * Gets the encoded fragment part of this URI, everything after the '#'.
     *
     * @return the encoded fragment or null if there isn't one
     */
    public String getEncodedFragment() {
        return uri.getEncodedFragment();
    }

    /**
     * Gets the decoded path segments.
     *
     * @return decoded path segments, each without a leading or trailing '/'
     */
    public List<String> getPathSegments() {
        return uri.getPathSegments();
    }

    /**
     * Gets the decoded last segment in the path.
     *
     * @return the decoded last segment or null if the path is empty
     */
    public String getLastPathSegment() {
        return uri.getLastPathSegment();
    }

}
