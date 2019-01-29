package com.lexinfintech.component.debugdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * 通过WindowManager实现的队列式弹框
 */

@SuppressLint("Java  Def")
public class DebugDialog {

    @SuppressLint("StaticFieldLeak")
    private static DebugDialog sInstance;

    private View mViewRoot;
    private LinearLayout mLlContainer;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvClick;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private LinkedList<Pair<String, String>> mMsgList;
    private BroadcastReceiver mHomeListenerReceiver;
    private IntentFilter mHomeFilter;
    private int mMargin;
    private boolean isInit;
    private static Context sContext;
    private static boolean mIsDebug;
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static DebugDialog getInstance() {
        if (sInstance == null) {
            synchronized (DebugDialog.class) {
                if (sInstance == null) {
                    sInstance = new DebugDialog();
                }
            }
        }
        return sInstance;
    }


    private DebugDialog() {
        isInit = false;
    }

    public static void setIsDebug(boolean isDebug) {
        mIsDebug = isDebug;
    }

    public static boolean isDebug() {
        return mIsDebug;
    }

    /**
     * 使用之前请先进行初始化
     *
     * @param context
     */
    public void init(Context context) {
        if (sContext != null && isInit) {
            return;
        }
        mMsgList = new LinkedList<>();


        sContext = context.getApplicationContext();
        mMargin = (int) dip2px(sContext, 30);
        mViewRoot = LayoutInflater.from(sContext).inflate(R.layout.debug_alert_debug_dialog, null, false);
        mViewRoot.setClickable(true);
        mViewRoot.setFocusable(true);
        mViewRoot.setFocusableInTouchMode(true);

        mLlContainer = mViewRoot.findViewById(R.id.mLlContainer);
        mTvTitle = mViewRoot.findViewById(R.id.mTvTitle);
        mTvContent = mViewRoot.findViewById(R.id.mTvContent);
        mTvClick = mViewRoot.findViewById(R.id.mTvClick);
        mTvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
            }
        });
        mTvClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dismiss();
                return true;
            }
        });

        setKeyListener();

        // 设置可拖动
        setTouchMove2();

        // 窗口管理对象
        mWindowManager = (WindowManager) sContext.getSystemService(Application.WINDOW_SERVICE);
        int type;
        if (Build.VERSION.SDK_INT >= 26) {//target 8.0新特性
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(type);
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.6f;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.CENTER;
        params.windowAnimations = android.R.style.Animation_Dialog;
        mWindowParams = params;
        isInit = true;
    }


    private void setKeyListener() {
        mViewRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    showNext();
                    return true;
                }
                return false;
            }
        });
    }


 /*   private void setTouchMove() {
        mLlContainer.setOnTouchListener(new View.OnTouchListener() {
            // 触屏监听
            float lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                float x = event.getX();
                float y = event.getY();

                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    // 减小偏移量,防止过度抖动
                    mWindowParams.x += (int) (x - lastX) / 2;
                    mWindowParams.y += (int) (y - lastY) / 2;
                    if (isShowing()) {
                        mWindowManager.updateViewLayout(mViewRoot, mWindowParams);
                    }
                }
                return true;
            }
        });
    }*/


    @SuppressLint("ClickableViewAccessibility")
    private void setTouchMove2() {
        mLlContainer.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY, left, top, right, bottom, screenWidth, screenHeight;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        screenWidth = mViewRoot.getWidth();
                        screenHeight = mViewRoot.getHeight();
                        left = v.getLeft() + dx;
                        top = v.getTop() + dy;
                        right = v.getRight() + dx;
                        bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        params.leftMargin = left;
                        params.topMargin = top;
                        params.rightMargin = screenWidth - right;
                        params.bottomMargin = screenHeight - bottom;
                        params.width = v.getWidth();
                        params.height = v.getHeight();
                        v.requestLayout();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    private void registerHomeKeyListener() {
        // 注册Receiver
        if (mHomeListenerReceiver == null) {
            mHomeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mHomeListenerReceiver = new BroadcastReceiver() {
                final String SYSTEM_DIALOG_REASON_KEY = "reason";
                final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (TextUtils.equals(action, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                        String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                        if (reason != null && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            dismiss();
                        }
                    }
                }
            };
        }
        if (sContext != null) {
            sContext.registerReceiver(mHomeListenerReceiver, mHomeFilter);
        }
    }

    private void unregisterHomeKeyListener() {
        //反注册Receiver
        if (mHomeListenerReceiver != null && sContext != null) {
            sContext.unregisterReceiver(mHomeListenerReceiver);
        }
    }


    private void showNext() {
        boolean isShow = isShowing();
        if (isShow) {
            removeView();
        }
        if (mMsgList.size() == 0) {
            return;
        }
        Pair<String, String> msg = mMsgList.removeFirst();
        if (!TextUtils.isEmpty(msg.first)) {
            mTvTitle.setText(msg.first);
        } else {
            mTvTitle.setText("调试信息");
        }
        mTvContent.setText(msg.second);
        // 刷新按钮文案
        refreshClick();
        // 恢复到中央显示
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = mMargin;
        params.rightMargin = mMargin;
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.CENTER_IN_PARENT);
        mLlContainer.setLayoutParams(params);

        addView();
    }

    private void addView() {
        mWindowManager.addView(mViewRoot, mWindowParams);
        registerHomeKeyListener();
    }

    private void removeView() {
        mWindowManager.removeView(mViewRoot);
        unregisterHomeKeyListener();
    }

    private void refreshClick() {
        int size = mMsgList.size();
        mTvClick.setText(size > 0 ? "下一条(" + size + ")" : "确定");
    }


    public void show(final String title, String content) {
        if (sContext == null || !mIsDebug) {
            return;
        }
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (content.length() > 500) {
            content = content.substring(0, 500);
        }
        final String finalContent = content;
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                boolean hasPermission = hasAlertWindowPermission(sContext);
                if (!hasPermission) {
                    Toast.makeText(sContext, "您当前处于调试模式\n查看调试信息，需要开启APP的悬浮窗权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isInit) {
                    init(sContext);
                }
                mMsgList.addLast(new Pair<>(title, finalContent));
                if (!isShowing()) {
                    showNext();
                } else {
                    refreshClick();
                }
            }
        });
    }


    /**
     * 获取悬浮窗权限
     *
     * @param activity
     */
    public static void getAlertPermission(final Activity activity) {
        if (activity == null) {
            return;
        }
        if (hasAlertWindowPermission(activity)) {
            Toast.makeText(activity, "您已经开启APP的悬浮窗权限", Toast.LENGTH_SHORT).show();
            return;
        }

        (new AlertDialog.Builder(activity)).setMessage("调试模式需要开启悬浮窗权限").setPositiveButton("去开启", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                goSetAlertWindowPermission(activity, 1);
            }
        }).setNegativeButton("取消", (DialogInterface.OnClickListener) null).setCancelable(false).create().show();
    }


    private boolean dismiss() {
        if (!isInit) {
            return false;
        }
        if (isShowing()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mMsgList.clear();
                removeView();
            } else {
                if (mMainHandler == null) {
                    return false;
                }
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isShowing()) {
                            mMsgList.clear();
                            removeView();
                        }
                    }
                });
            }
            return true;
        }
        return false;
    }


    /**
     * 点击home键后,该种判断会失灵,所以需要注册home键监听,及时移除
     */
    private boolean isShowing() {
        return mViewRoot != null && mViewRoot.getParent() != null;
    }


    private static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    /**
     * 判断悬浮窗权限
     */
    public static boolean hasAlertWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= 19) {
            // OP_SYSTEM_ALERT_WINDOW = 24;
            return checkOp(context, 24);
        }
        return true;
    }

    @SuppressLint({"FQL_METHOD_CALL_LINT"})
    private static boolean checkOp(Context context, int op) {
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                Log.e("checkOp", Log.getStackTraceString(e));
            }
        } else {
            Log.i("checkOp", "Below API 19 cannot invoke!");
        }
        return false;
    }


    private static void goSetAlertWindowPermission(Activity activity, int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        } else {
            gotoSystemSetting(activity);
        }
    }


    private static void gotoSystemSetting(Context context) {
        if (context != null) {
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return localIntent;
    }
}
