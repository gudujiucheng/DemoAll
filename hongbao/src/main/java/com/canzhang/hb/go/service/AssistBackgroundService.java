package com.canzhang.hb.go.service;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.canzhang.hb.go.config.ConfigManger;
import com.canzhang.hb.go.utils.AccessibilityHelper;
import com.canzhang.hb.go.utils.Logg;


/**
 * @author xj.luo
 * @email xj_luo@foxmail.com
 * @date Created on 2020/11/13
 */

public class AssistBackgroundService extends AccessibilityService {

    private static final String CHILD_TAG = AssistBackgroundService.class.getSimpleName();

    /**
     * 红包弹出的class的名字
     */
    private static final String ACTIVITY_DIALOG_LUCKYMONEY = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";

    /**
     * 红包详情页
     */
    private static String LUCKY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

    /**
     * 聊天列表页 class name
     */
    private static String CHAT_LIST_PAGE = " com.tencent.mm.ui.LauncherUI";


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    private boolean isHongBaoOpen = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 每当界面改变的时候就会回调这个方法，通过event我们就可以获取到界面的信息包括界面上的控件

        //当前类名
        String className = event.getClassName().toString();
        Logg.i(CHILD_TAG, "当前类名为  " + className);

        //当前为红包弹出窗
        if (className.equals(ACTIVITY_DIALOG_LUCKYMONEY)) {
            Logg.i(CHILD_TAG, "当前为红包弹出框页面");
            int delay = ConfigManger.getInstance().getOpenDelayTime();
            if (delay != 0) {
                Logg.i(CHILD_TAG, "当前为红包弹出框页面,延时 " + delay + "毫秒点击 开 ");
                SystemClock.sleep(delay);
            }
            AccessibilityHelper.openPackage(this);
            isHongBaoOpen = true;
            return;
        }

        //红包领取后的详情页面，自动返回
        if (className.equals(LUCKY_MONEY_DETAIL)) {
            //返回聊天界面
            if (isHongBaoOpen) {
                SystemClock.sleep(1000);
                performGlobalAction(GLOBAL_ACTION_BACK);
                isHongBaoOpen = false;
            }
            return;
        }

        AccessibilityNodeInfo hongBaoParent = AccessibilityHelper.findHongBaoNode2(this, event);
        if (hongBaoParent != null) {
            Logg.i(CHILD_TAG, "找到红包，点击红包");
            int delay2 = ConfigManger.getInstance().getClickDelayTime();
            SystemClock.sleep(delay2);
            boolean success = AccessibilityHelper.clickHongbao(hongBaoParent);
            Logg.i(CHILD_TAG, success ? "红包被点击了 " : "红包没有被点击 ");
        }
    }

    @Override
    public void onInterrupt() {

    }

}
