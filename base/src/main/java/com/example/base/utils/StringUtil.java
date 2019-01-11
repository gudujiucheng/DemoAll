package com.example.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Candice on 2015/10/22.
 */
public class StringUtil {
    public static final int MASK_4BIT = 0x0F;
    public static final int MASK_8BIT = 0xFF;
    public static final int BIT_OF_KB = 10;
    public static final int BIT_OF_MB = 20;
    public static final int BYTE_OF_KB = 1 << BIT_OF_KB;
    public static final int BYTE_OF_MB = 1 << BIT_OF_MB;
    private static final String TAG = "StringUtil";
    // unicode ranges and valid/invalid characters
    private static final char LOWER_RANGE = 0x20;
    private static final char UPPER_RANGE = 0x7f;
    private static final char[] VALID_CHARS = {0x9, 0xA, 0xD};
    private static final char[] INVALID = {'<', '>', '"', '\'', '&', ' '};
    private static final String[] VALID = {"&lt;", "&gt;", "&quot;", "&apos;", "&amp;", "+"};


    private StringUtil() {

    }

    public static boolean isEmpty(final String object) {
        return (object == null) || (object.length() <= 0);
    }

    public static boolean isEmpty(final byte[] object) {
        return (object == null) || (object.length <= 0);
    }

    /**
     * 功能：判断字符串是否为数字
     */
    private static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    // 密码6~20位且不含特殊字符
    public static boolean isPwd(String pwd) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_]{6,20}$",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    public static boolean isEmail(String str) {
        Pattern p = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    // 字母，数字，下划线，6—�?18�?用户名和密码都采用此验证方法
    public static boolean isUsername(String name) {

        Pattern p = Pattern.compile("^[a-zA-Z0-9_]{6,18}$",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public static boolean isName(String name) {
        Pattern num = Pattern.compile("^[0-9]*$", Pattern.CASE_INSENSITIVE);
        if (num.matcher(name).matches()) {
            return false;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9_\u4e00-\u9fa5]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * will drop some value
     *
     * @param value
     * @return
     */

    public static String escapeSqlValue(String value) {
        if (value != null) {
            value = value.replace("\\[", "[[]");
            value = value.replace("%", "");
            value = value.replace("\\^", "");
            value = value.replace("'", "");
            value = value.replace("\\{", "");
            value = value.replace("\\}", "");
            value = value.replace("\"", "");
        }
        return value;
    }





    /**
     * 英文符号代替中文符号，半角转全角
     */
    public static String DBCtoSBC(String str) {
        str = ToDBC(StringFilterq(str));
        return str;
    }

    /**
     * 半角转换为全角
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    // 替换特殊字符，解决TextView排版不整齐的缺陷
    public static String StringFilterq(String str)
            throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static boolean isValidString(int minlength, int maxlength, String input) {
        return (input.length() > maxlength || input.length() < minlength);
    }

    public static String listToString(List<String> srcList, String seperator) {
        if (srcList == null) {
            return "";
        }
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < srcList.size(); i++) {
            if (i == srcList.size() - 1) {
                result.append(srcList.get(i).trim());
            } else {
                result.append(srcList.get(i).trim() + seperator);
            }
        }
        return result.toString();
    }

    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        Collections.addAll(result, src);
        return result;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {


        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }

        return sb.toString();
    }

    public static String getSizeKB(final long bytes) {
        final float cRound = 10;

        // > 1MB
        if ((bytes >> BIT_OF_MB) > 0) {
            return getSizeMB(bytes);
        }

        // > 0.5K
        if ((bytes >> (BIT_OF_KB - 1)) > 0) {
            final float bytesInKB = (Math.round(bytes * cRound / BYTE_OF_KB))
                    / cRound;
            return "" + bytesInKB + "KB";
        }

        return "" + bytes + "B";
    }

    public static String getSizeMB(final long bytes) {
        final float cRound = 10;

        final float bytesInMB = (Math.round(bytes * cRound / BYTE_OF_MB))
                / cRound;
        return "" + bytesInMB + "MB";
    }

    public static String dumpArray(final Object[] stack) {
        final StringBuilder sb = new StringBuilder();
        for (final Object ste : stack) {
            sb.append(ste);
            sb.append(",");
        }
        return sb.toString();
    }

    public static String dumpHex(final byte[] privateKey) {
        if (privateKey == null) {
            return "(null)";
        }

        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        final int j = privateKey.length;
        final int cHexWidth = 3;
        final char[] str = new char[j * cHexWidth + j / 16];
        int k = 0;
        for (int i = 0; i < j; i++) {
            final byte byte0 = privateKey[i];
            str[k++] = ' ';
            str[k++] = hexDigits[byte0 >>> 4 & MASK_4BIT];
            str[k++] = hexDigits[byte0 & MASK_4BIT];

            if (i % 16 == 0 && i > 0) {
                str[k++] = '\n';
            }
        }
        return new String(str);
    }

    public static ArrayList<String> getTellIndex(String str) {
        ArrayList<String> telList = new ArrayList<String>(2);
        int telLength = 0;
        int startLength = -1;
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) >= '0' && str.charAt(i) <= '9')
                    || str.charAt(i) == '-') {
                telLength++;
                // 第一次匹配到号码关键字
                if (startLength < 0) {
                    startLength = i;
                }
                if (i == str.length() - 1) {
                    telList.add(str.substring(startLength, startLength + telLength));
                    telLength = 0;
                    startLength = -1;
                }
            } else {
                // 长度不符合，初始化
                if (telLength < 11 || telLength > 14) {
                    telLength = 0;
                    startLength = -1;
                } else {
                    telList.add(str
                            .substring(startLength, startLength + telLength));
                    telLength = 0;
                    startLength = -1;
                }
            }
        }
        return telList;
    }


    /**
     * ini parser
     */
    public static Map<String, String> parseIni(final String ini) {
        if (ini == null || ini.length() <= 0) {
            return null;
        }

        final Map<String, String> values = new HashMap<String, String>();

        final String[] lines = ini.split("\n");
        for (final String line : lines) {
            if (line == null || line.length() <= 0) {
                continue;
            }

            final String[] kv = line.trim().split("=", 2);
            if (kv == null || kv.length < 2) {
                continue;
            }

            // key
            final String key = kv[0];
            final String value = kv[1];
            if (key == null || key.length() <= 0
                    || !key.matches("^[a-zA-Z0-9_]*")) {
                continue;
            }

            values.put(key, value);
        }

        final Iterator<Map.Entry<String, String>> iter = values.entrySet()
                .iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, String> entry = iter.next();

        }
        return values;
    }

    public static byte[] decodeHexString(final String in) {
        if (in == null || in.length() <= 0) {
            return new byte[0];
        }

        try {
            byte[] buf = new byte[in.length() / 2];
            final int cRadix = 16;
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (Integer.parseInt(
                        in.substring(i * 2, (i * 2 + 2)), cRadix) & MASK_8BIT);
            }
            return buf;

        } catch (NumberFormatException e) {
        }

        return new byte[0];
    }

    /**
     * encodeByteArray �� decodeString �ɶ�ʹ��
     *
     * @param
     * @return
     */
    public static String encodeHexString(final byte[] ba) {
        StringBuilder out = new StringBuilder("");
        if (ba != null) {
            for (int i = 0; i < ba.length; i++) {
                out.append(String.format("%02x", ba[i] & MASK_8BIT));
            }
        }

        return out.toString();
    }

    public static String unescape(String s) {
        if (StringUtil.isEmpty(s))
            return "";
        while (true) {
            int n = s.indexOf("&#");
            if (n < 0)
                break;
            int m = s.indexOf(";", n + 2);
            if (m < 0)
                break;
            try {
                s = s.substring(0, n)
                        + (char) (Integer.parseInt(s.substring(n + 2, m)))
                        + s.substring(m + 1);
            } catch (Exception e) {
                return s;
            }
        }
        s = s.replace("&quot;", "\"");
        s = s.replace("&lt;", "<");
        s = s.replace("&gt;", ">");
        s = s.replace("&amp;", "&");
        return s;
    }

    public static String reverse2DotFormat(double d) {
        final DecimalFormat df = new DecimalFormat("#,##0.00");
        try {
            return df.format(d);
        } catch (Exception e) {

        }
        return reverse2DotFormat(d + "");
    }

    public static String reverse2DotFormat(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0.00";
        }
        return reverse2DotFormat(Double.valueOf(str));
    }

    /**
     * Escape a string such that it is safe to use in an XML document.
     *
     * @param str the string to escape
     */
    public static String escapeStringForXml(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer out = new StringBuffer();
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);

            if ((c < LOWER_RANGE && c != VALID_CHARS[0] && c != VALID_CHARS[1]
                    && c != VALID_CHARS[2]) || (c > UPPER_RANGE)) {
                // character out of range, escape with character value
                out.append("&#");
                out.append(Integer.toString(c));
                out.append(';');
            } else {
                boolean valid = true;
                // check for invalid characters (e.g., "<", "&", etc)
                for (int j = INVALID.length - 1; j >= 0; --j) {
                    if (INVALID[j] == c) {
                        valid = false;
                        out.append(VALID[j]);
                        break;
                    }
                }
                // if character is valid, don't escape
                if (valid) {
                    out.append(c);
                }
            }
        }
        return out.toString();
    }




    public static boolean isQQNum(String QQ) {
        String regex = "^[1-9][0-9]{4,}$";
        return StringUtil.PatternCheck(QQ, regex);
    }

    public static boolean PatternCheck(String str, String regex) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static double valueDoubleOfString(String str) {
        double result;
        try {
            result = Double.valueOf(str);
        } catch (Exception e) {

            result = 0;
        }
        return result;
    }

    public static int valueIntOfString(String str) {
        int result;
        try {
            result = Integer.valueOf(str);
        } catch (Exception e) {

            result = 0;
        }
        return result;
    }

    public static float valueFloatOfString(String str) {
        float result;
        try {
            result = Float.valueOf(str);
        } catch (Exception e) {

            result = 0;
        }
        return result;
    }

    public static float parseFloatOfString(String str) {
        float result;
        try {
            result = Float.valueOf(str);
        } catch (Exception e) {

            result = 0;
        }
        return result;
    }

    public static int parseIntOfString(String str) {
        int result;
        try {
            result = Integer.valueOf(str);
        } catch (Exception e) {

            result = 0;
        }
        return result;
    }

    public static double parseDoubleOfString(String str) {
        double result;
        try {
            result = Double.valueOf(str);
        } catch (Exception e) {

            result = 0.0;
        }
        return result;
    }

    public static boolean copyToClipboard(Context context, CharSequence chars) {
        if (chars == null || chars.length() == 0) {
            return false;
        }
        String text = chars.toString();
        // 从API11开始android推荐使用android.content.ClipboardManager
        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 通过调用ClipData类的数据方法的相应类型来实例化ClipData对象
        // 如果文本数据在newPlainText方法被调用,必须将数据设置为剪贴板管理器对象的剪辑
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        return true;
    }

    public static byte[] getInputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 用数据装
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
        // 关闭流一定要记得。
        return outstream.toByteArray();
    }

    public static String dividePhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }

        String result = phoneNum.substring(0, 3) + " "
                + phoneNum.substring(3, 7) + " "
                + phoneNum.substring(7, 11);
        return result;
    }

    public static boolean isPhone(String phone) {
        //第三种为了兼容测试帐号
        return phone.matches("1[0-9]{10}|\\+?86 ?1[0-9]{10}") || phone.matches("9[0-9]{10}");
    }

    public static String getDividerValue(double value) {
        DecimalFormat fnum;
        fnum = new DecimalFormat("#,##0.00");
        String result = fnum.format(value);
        return result;
    }

    public static String getIntegerDividerValue(double value) {
        DecimalFormat fnum;
        fnum = new DecimalFormat("#,##0");
        String result = fnum.format(value);
        return result;
    }

    public static Spannable getFenqileMoneySpannableStr(String pay) {
        Spannable ss;
        if (TextUtils.isEmpty(pay)) {
            ss = new SpannableString("");
        } else {
            ss = new SpannableString(pay);
            if (pay.contains(",")) {
                int index = pay.indexOf(",");
                ss.setSpan(new AbsoluteSizeSpan(54), index, index + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            if (pay.contains(".")) {
                int index = pay.indexOf(".");
                ss.setSpan(new AbsoluteSizeSpan(54), index, index + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }

    public static Typeface getMoneyTypeface(Context context) {
        Typeface cmtTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Medium.otf");// 根据路径得到Typeface
        return cmtTypeFace;
    }


    public static Spannable getDefaultMoneyStyle(double value) {
        String resultValue = getDividerValue(value);
        Spannable spannable = getFenqileMoneySpannableStr(resultValue);
        return spannable;
    }

    public static void setPriceTextRMB(String price, TextView tv, int signTextSize) {
        if (TextUtils.isEmpty(price)) {
            return;
        }
        price = "¥" + price;
        SpannableString styledText = new SpannableString(price);
        styledText.setSpan(new AbsoluteSizeSpan(signTextSize, true), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(styledText);
    }


    public static String getRandomFsTag() {
        String cd = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cd.length() * 2; i++) {
            sb.append(cd.charAt((int) (Math.random() * 16)));
        }
        return sb.toString();
    }

    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getApplicationContext().getResources().getAssets().open(fileName);
//获取文件的字节数
            int lenght = in.available();
            InputStreamReader isReader = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isReader);
            String line = null;
            while ((line = br.readLine()) != null) {
                //读txt文件中内容
                Log.e("txt中内容", line);
                result = line;
            }
            br.close();
            isReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
