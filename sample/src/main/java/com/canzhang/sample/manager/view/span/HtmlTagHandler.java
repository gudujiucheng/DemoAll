package com.canzhang.sample.manager.view.span;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

public class HtmlTagHandler implements Html.TagHandler {
    private String tagName;
    private int startIndex = 0;
    private int endIndex = 0;
    final HashMap<String, String> attributes = new HashMap<>();
    private Context context;

    public HtmlTagHandler(Context context, String tagName) {
        this.tagName = tagName;
        this.context = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // 判断是否是当前需要的tag
        if (tag.equalsIgnoreCase(tagName)) {
            // 解析所有属性值
            parseAttributes(xmlReader);
            if (opening) {
                startHandleTag(output);
            } else {
                endEndHandleTag(output);
            }
        }
    }

    public void startHandleTag(Editable output) {
        startIndex = output.length();
    }

    public void endEndHandleTag( Editable output) {
        endIndex = output.length();
        // 获取属性值
        String color = attributes.get("color");
        String size = attributes.get("size");
        int formatSize = getFormatSize(size);
        // 设置字体大小
        if (formatSize>0) {
            output.setSpan(new AbsoluteSizeSpan(formatSize), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // 设置颜色
        if (!TextUtils.isEmpty(color)) {
            output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    /**
     * 解析所有属性值
     *
     * @param xmlReader
     */
    private void parseAttributes(final XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);
            for (int i = 0; i < len; i++) {
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            }
        } catch (Exception e) {
        }
    }

    private int getFormatSize(String size) {
        if (TextUtils.isEmpty(size)) {
            return -1;
        }
        int spValue = -1;
        try {
            spValue = Integer.parseInt(size);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (spValue == -1) {
            return -1;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}