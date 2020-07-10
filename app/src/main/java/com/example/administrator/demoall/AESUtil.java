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

import com.example.administrator.Base64Util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
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
     * 初始向量
     */
    private static final String IV_STRING = "fdba88a93a2aafca";

    /**
     * utf-8
     */
    private static final String charset = "UTF-8";

    /**
     * AES加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String aesEncryptString(String content, String key) {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(key)) {
            try {
                byte[] contentBytes = content.getBytes(charset);
                byte[] keyBytes = key.getBytes(charset);
                byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
//                Base64.Encoder encoder = Base64.getEncoder();
                return Base64Util.encode(encryptedBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * AES解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String aesDecryptString(String content, String key) {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(key)) {
            try {
//                Base64.Decoder decoder = Base64.getDecoder();
                byte[] encryptedBytes = Base64Util.decode(content);
                byte[] keyBytes = new byte[0];
                keyBytes = key.getBytes(charset);
                byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
                return new String(decryptedBytes, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
    }

    private static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }
}
