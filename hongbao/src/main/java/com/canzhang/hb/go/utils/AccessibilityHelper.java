package com.canzhang.hb.go.utils;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;




public class AccessibilityHelper {

    private static final String CHILD_TAG = AccessibilityHelper.class.getSimpleName();


    public static AccessibilityNodeInfo findHongBaoNode2(AccessibilityService service, AccessibilityEvent event) {
        try {
            //获取当前界面父布局的控件
            AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();

            if (accessibilityNodeInfo != null) {
                String text = "微信红包";
                // 通过内容找到有微信红包的控件
                List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);

                if (nodeInfoList != null && nodeInfoList.size() > 0) {
                    for (int i = 0; i < nodeInfoList.size(); i++) {
                        AccessibilityNodeInfo nodeInfo = nodeInfoList.get(i);

                        Logg.i(CHILD_TAG, "通过内容找到的节点类名  " + nodeInfo.getClassName());
                    }

                    // 获取最后一个节点, 带这个信息的控件一般为红包（打开的 未打开的）
                    AccessibilityNodeInfo lastNode = getLastNode(nodeInfoList);
                    if (lastNode != null) {
                        AccessibilityNodeInfo parent = lastNode.getParent();
                        if (parent != null) {
                            int childCount = parent.getChildCount();
                            Logg.i(CHILD_TAG, "包含内容  " + text + " 的节点父节点下的子节点的个数 " + childCount);
                            if (childCount > 0) {
                                boolean isWeChatHongBao = false;
                                boolean isUnopened = true;
                                for (int i = 0; i < childCount; i++) {
                                    AccessibilityNodeInfo child = parent.getChild(i);
                                    if (child != null) {
                                        String className = child.getClassName().toString();
                                        Logg.i(CHILD_TAG, "包含内容  " + text + " 的节点父节点下的子节点的类名 " + className);

                                        if ("android.widget.TextView".equals(className)) {
                                            String contentText = child.getText().toString();
                                            Logg.i(CHILD_TAG, "包含内容  " + text + " 的节点父节点下的子节点的text为 " + contentText);

                                            if ("微信红包".equals(contentText)) {
                                                isWeChatHongBao = true;
                                            }

                                            if ("已领取".equals(contentText)
                                                    || "已被领完".equals(contentText)
                                                    || "已过期".equals(contentText)) {

                                                isUnopened = false;
                                            }
                                        }
                                    }
                                }

                                if (isWeChatHongBao && isUnopened) {
                                    return parent;
                                }

                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 有可能一个界面上，有多个红包，需要选择最后一个红包
    private static AccessibilityNodeInfo getLastNode(List<AccessibilityNodeInfo> nodeInfoList) {
        if (nodeInfoList == null) {
            return null;
        }
        if (nodeInfoList.size() > 0) {
            AccessibilityNodeInfo accessibilityNodeInfo = nodeInfoList.get(nodeInfoList.size() - 1);
            return accessibilityNodeInfo;
        }

        return null;
    }

    public static boolean clickHongbao(AccessibilityNodeInfo parent) {
        if (parent != null) {
            boolean clickable = parent.isClickable();
            String result = clickable ? "enable" : "disable";
            Logg.i(CHILD_TAG, "parent is click " + result);
            boolean success = parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            return success;
        }

        return false;
    }

    public static void openPackage(AccessibilityService service) {
        try {
            //获取当前界面父布局的控件
            AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();

            if (accessibilityNodeInfo != null) {
                int childCount = accessibilityNodeInfo.getChildCount();
                Logg.i(CHILD_TAG, "父布局下的子节点个数 " + childCount);

                CharSequence className1 = accessibilityNodeInfo.getClassName();
                Logg.i(CHILD_TAG, "父布局的类名  " + className1);

                CharSequence text = accessibilityNodeInfo.getText();
                Logg.i(CHILD_TAG, "父布局的text  " + text);

                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);

                        String childName = child.getClassName().toString();
                        Logg.i(CHILD_TAG, "父布局的子节点类名  " + childName);
                        boolean clickable = child.isClickable();
                        Logg.i(CHILD_TAG, "父布局的子节点  " + (clickable ? "可点击" : "不可点击"));

                        if ("android.widget.TextView".equals(childName)) {
                            String textContent = child.getText().toString();
                            Logg.i(CHILD_TAG, "text view content " + textContent);
                        }

                        // 这个子节点是那个 X
                        if ("android.widget.ImageView".equals(childName)) {
                            // child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }

                        // 这个就是 开 的那个button
                        if ("android.widget.Button".equals(childName)) {
                            child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
