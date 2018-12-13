package com.example.administrator.demoall.webview.cache.utils;


import java.security.MessageDigest;

/**
 * <div class="en">MD5 digest wrapper</div>
 * <div class="zh_CN">MD5计算封装</div>
 *
 * @author kirozhao
 */
public final class MD5 {

    private MD5() {

    }

    public final static String getMessageDigest(String str) {
        return getMessageDigest(str.getBytes());
    }

    /**
     * get md5 string for input buffer
     *
     * @param buffer data to be calculated
     * @return md5 result in string format
     */
    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }




}
