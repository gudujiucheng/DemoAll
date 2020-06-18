/**
 * 描述
 *
 * @name AESUtil.java
 * @copyright Copyright by fenqile.com
 * @author xuejinshi
 * @version 2016年9月20日
 **/

package com.example.administrator.demoall;


import android.text.TextUtils;
import android.util.Log;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES工具类.
 *
 * @author xuejinshi
 * @version 2016年9月20日
 * AESUtil
 * @since
 */
public class AESUtil {

    private static final String TAG = "AESUtil";

    /**
     * AES加密
     *
     * @param content  加密内容
     * @param password 密钥
     * @return byte[]
     * encrypt
     */
    public static byte[] encrypt(String content, String password) {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(password)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(password.getBytes());
                kgen.init(128, random);

                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                byte[] byteContent = content.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
                byte[] result = cipher.doFinal(byteContent);
                return result; // 加密
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "AES加密失败[NoSuchAlgorithmException]，异常信息:", e);
            } catch (NoSuchPaddingException e) {
                Log.e(TAG, "AES加密失败[NoSuchPaddingException]，异常信息:", e);
            } catch (InvalidKeyException e) {
                Log.e(TAG, "AES加密失败[InvalidKeyException]，异常信息:", e);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "AES加密失败[UnsupportedEncodingException]，异常信息:", e);
            } catch (IllegalBlockSizeException e) {
                Log.e(TAG, "AES加密失败[IllegalBlockSizeException]，异常信息:", e);
            } catch (BadPaddingException e) {
                Log.e(TAG, "AES加密失败[BadPaddingException]，异常信息:", e);
            }
        }
        return new byte[0];
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 密钥
     * @return byte[]
     * decrypt
     */
    public static byte[] decrypt(byte[] content, String password) {
        if (content != null && content.length > 0 && !TextUtils.isEmpty(password)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");

                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(password.getBytes());
                kgen.init(128, random);

                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
                byte[] result = cipher.doFinal(content);
                return result; // 加密
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "AES解密失败[NoSuchAlgorithmException]，异常信息:", e);
            } catch (NoSuchPaddingException e) {
                Log.e(TAG, "AES解密失败[NoSuchPaddingException]，异常信息:", e);
            } catch (InvalidKeyException e) {
                Log.e(TAG, "AES解密失败[InvalidKeyException]，异常信息:", e);
            } catch (IllegalBlockSizeException e) {
                Log.e(TAG, "AES解密失败[IllegalBlockSizeException]，异常信息:", e);
            } catch (BadPaddingException e) {
                Log.e(TAG, "AES解密失败[BadPaddingException]，异常信息:", e);
            }
        }
        return new byte[0];
    }

    /**
     * 加密，返回字符串
     *
     * @param content  加密内容
     * @param password 密钥
     * @return String
     * encrypt2Str
     */
    public static String encrypt2Str(String password,String content) {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(password)) {
            return parseByte2HexStr(encrypt(content, password));
        }
        return null;
    }

    /**
     * 解密，返回字符串
     *
     * @param content  待解密内容
     * @param password 密钥
     * @return String
     * @throws UnsupportedEncodingException decrypt2Str
     */
    public static String decrypt2Str(String password, String content)
            throws UnsupportedEncodingException {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(password)) {
            return new String(decrypt(parseHexStr2Byte(content), password), "utf-8");
        }
        return null;
    }

    /**
     * 将byte数组转为16进制字符串.
     *
     * @param buf byte数组
     * @return String
     * parseByte2HexStr
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串转为byte数组
     *
     * @param hexStr 16进制字符串
     * @return byte[]
     * parseHexStr2Byte
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (TextUtils.isEmpty(hexStr)) {
            return new byte[0];
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
