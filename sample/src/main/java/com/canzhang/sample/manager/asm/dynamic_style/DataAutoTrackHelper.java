package com.canzhang.sample.manager.asm.dynamic_style;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.Keep;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.ToggleButton;


import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;

import org.json.JSONObject;

/**
 * bury_point_sdk 插桩对应的实现方法
 * 注意：此名称如果要改动，需要同步改动bury_point_sdk中对应的代码
 *
 * @author canzhang
 */
@SuppressWarnings("unused")
public class DataAutoTrackHelper {
    /**
     * 常规dialog点击事件
     *
     * @param dialogInterface
     * @param whichButton
     */
    @Keep
    public static void trackViewOnClick(DialogInterface dialogInterface, int whichButton) {

        LogUtils.logD("current method:trackViewOnClick(DialogInterface dialogInterface, int whichButton)");
        try {
            Dialog dialog = null;
            if (dialogInterface instanceof Dialog) {
                dialog = (Dialog) dialogInterface;
            }

            if (dialog == null) {
                return;
            }
            Button button = null;
            if (dialog instanceof android.app.AlertDialog) {
                button = ((android.app.AlertDialog) dialog).getButton(whichButton);
            } else if (dialog instanceof androidx.appcompat.app.AlertDialog) {
                button = ((androidx.appcompat.app.AlertDialog) dialog).getButton(whichButton);
            }

            String viewTag = ViewDataUtils.getViewTag(button);
            if (!TextUtils.isEmpty(viewTag)) {
                reportClickEvent(viewTag, null, true);
                return;
            }

            StringBuilder path = new StringBuilder();
            StringBuilder content = new StringBuilder();

            Context context = dialog.getContext();
            //将Context转成Activity
            Activity activity = ViewDataUtils.getActivityFromContext(context);

            if (activity == null) {
                activity = dialog.getOwnerActivity();
            }
            if (activity != null) {
                path.append(activity.getClass().getCanonicalName()).append("_").append("Dialog").append("_");
            }
            if (button != null) {
                content.append(button.getText());

                try {
                    String idString = context.getResources().getResourceEntryName(button.getId());
                    if (!TextUtils.isEmpty(idString)) {
                        path.append(idString).append("_");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            reportClickEvent(path.toString(), content.toString(), false);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void trackViewOnClick(CompoundButton view, boolean isChecked) {

        LogUtils.logD("current method:trackViewOnClick(CompoundButton view, boolean isChecked)");
        try {
            String viewTag = ViewDataUtils.getViewTag(view);
            if (!TextUtils.isEmpty(viewTag)) {
                reportClickEvent(viewTag, null, true);
                return;
            }
            StringBuilder content = new StringBuilder();
            String viewText = null;
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (!TextUtils.isEmpty(checkBox.getText())) {
                    viewText = checkBox.getText().toString();
                }
            } else if (view instanceof SwitchCompat) {
                SwitchCompat switchCompat = (SwitchCompat) view;
                if (isChecked) {
                    if (!TextUtils.isEmpty(switchCompat.getTextOn())) {
                        viewText = switchCompat.getTextOn().toString();
                    }
                } else {
                    if (!TextUtils.isEmpty(switchCompat.getTextOff())) {
                        viewText = switchCompat.getTextOff().toString();
                    }
                }
            } else if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                if (isChecked) {
                    if (!TextUtils.isEmpty(toggleButton.getTextOn())) {
                        viewText = toggleButton.getTextOn().toString();
                    }
                } else {
                    if (!TextUtils.isEmpty(toggleButton.getTextOff())) {
                        viewText = toggleButton.getTextOff().toString();
                    }
                }
            } else if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (!TextUtils.isEmpty(radioButton.getText())) {
                    viewText = radioButton.getText().toString();
                }
            }
            content.append(viewText).append("_").append(isChecked);

            reportClickEvent(ViewDataUtils.getCustomViewPath(view), content.toString(), false);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 无法预埋tag类型
     */
    @Keep
    public static void trackViewOnClick(DialogInterface dialogInterface, int whichButton, boolean isChecked) {

        LogUtils.logD("current method:trackViewOnClick(DialogInterface dialogInterface, int whichButton, boolean isChecked)");
        try {
            Dialog dialog = null;
            if (dialogInterface instanceof Dialog) {
                dialog = (Dialog) dialogInterface;
            }

            if (dialog == null) {
                return;
            }
            StringBuilder path = new StringBuilder();
            StringBuilder content = new StringBuilder();
            Context context = dialog.getContext();
            //将Context转成Activity
            Activity activity = ViewDataUtils.getActivityFromContext(context);

            if (activity == null) {
                activity = dialog.getOwnerActivity();
            }
            if (activity != null) {
                path.append(activity.getClass().getCanonicalName()).append("_");
            }

            ListView listView = null;
            if (dialog instanceof android.app.AlertDialog) {
                listView = ((android.app.AlertDialog) dialog).getListView();
            } else if (dialog instanceof androidx.appcompat.app.AlertDialog) {
                listView = ((androidx.appcompat.app.AlertDialog) dialog).getListView();
            }

            if (listView != null) {
                ListAdapter listAdapter = listView.getAdapter();
                Object object = listAdapter.getItem(whichButton);
                if (object != null) {
                    if (object instanceof String) {
                        content.append(object).append("_");
                    }
                }
            }
            path.append("Dialog").append("_");
            content.append(isChecked);
            reportClickEvent(path.toString(), content.toString(), false);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    /**
     * 列表类型
     *
     * @param adapterView
     * @param view
     * @param position
     */
    @Keep
    public static void trackViewOnClick(android.widget.AdapterView adapterView, View view, int position) {

        LogUtils.logD("current method:trackViewOnClick(android.widget.AdapterView adapterView, android.view.View view, int position)");
        try {
            String viewTag = ViewDataUtils.getViewTag(view);
            if (!TextUtils.isEmpty(viewTag)) {
                reportClickEvent(viewTag, null, true);
                return;
            }
            Context context = adapterView.getContext();
            if (context == null) {
                return;
            }
            StringBuilder path = new StringBuilder();
            StringBuilder content = new StringBuilder();


            Activity activity = ViewDataUtils.getActivityFromContext(context);
            if (activity != null) {
                path.append(activity.getClass().getCanonicalName()).append("_");
            }
            String idString = ViewDataUtils.getViewId(adapterView);
            if (!TextUtils.isEmpty(idString)) {
                path.append(idString).append("_");
            }

            String element_type = null;
            if (adapterView instanceof Spinner) {
                element_type = "Spinner";
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    if (item instanceof String) {
                        content.append(item).append("_");
                    }
                }
            } else {
                if (adapterView instanceof ListView) {
                    element_type = "ListView";
                } else if (adapterView instanceof GridView) {
                    element_type = "GridView";
                }


                String viewText = null;
                try {
                    viewText = ViewDataUtils.traverseViewContent(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //$element_content
                if (!TextUtils.isEmpty(viewText)) {
                    content.append(viewText);
                }
            }
            path.append(element_type).append("_").append(position);
            reportClickEvent(path.toString(), content.toString(), true);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 常规view 被点击，自动埋点
     *
     * @param view View
     */
    @Keep
    public static void trackViewOnClick(View view) {

        LogUtils.logD("current method:trackViewOnClick(View view)");
        try {
            String viewTag = ViewDataUtils.getViewTag(view);
            if (!TextUtils.isEmpty(viewTag)) {
                reportClickEvent(viewTag, null, true);
                return;
            }
            reportClickEvent(ViewDataUtils.getCustomViewPath(view), ViewDataUtils.traverseViewContent(view), false);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


//    @Keep
//    public static void trackTabOnSelect(TabLayout.Tab tab) {
//
//        LogUtils.logD("current method:trackTabOnSelect(TabLayout.Tab tab)");
//        if (tab == null) {
//            return;
//        }
//        try {
//            View customView = tab.getCustomView();
//            if (customView != null) {
//                String viewTag = ViewDataUtils.getViewTag(customView);
//                if (!TextUtils.isEmpty(viewTag)) {
//                    reportClickEvent(viewTag, null, true);
//                    return;
//                }
//            }
//            StringBuilder path = new StringBuilder();
//            StringBuilder content = new StringBuilder();
//            if (tab.getText() != null) {
//                content.append(tab.getText());
//            }
//
//            View mView = customView;
//            if (mView == null) {
//                mView = getValueByPropName(tab, "view");
//            }
//            if (mView == null) {
//                mView = getValueByPropName(tab, "mView");
//            }
//            if (mView != null) {
//                Activity activity = ViewDataUtils.getActivityFromView(mView);
//                if (activity != null) {
//                    path.append(activity.getClass().getCanonicalName()).append("_");
//                }
//            }
//            path.append("TabLayout.Tab").append("_");
//            path.append(tab.getPosition());
//
//            reportClickEvent(path.toString(), content.toString(), false);
//
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }


//    private static View getValueByPropName(TabLayout.Tab tab, String propName) {
//        View value = null;
//        try {
//            // 通过属性获取对象的属性
//            Field field = tab.getClass().getDeclaredField(propName);
//            // 对象的属性的访问权限设置为可访问
//            field.setAccessible(true);
//            // 获取属性的对应的值
//            value = (View) field.get(tab);
//        } catch (Exception e) {
//
//            return null;
//        }
//
//        return value;
//    }


    /**
     * 数据上报
     *
     * @param eventId      时间id
     * @param content      携带的附带内容信息
     * @param isViewHasTag view是否有预埋的tag（如果view有预设tag的话，则依据预设tag为eventId）
     */
    private static void reportClickEvent(String eventId, String content, boolean isViewHasTag) {
        try {
            LogUtils.logE("eventId:" + eventId);
            LogUtils.logE("content:" + content);
            JSONObject jsonObject = null;


            jsonObject = new JSONObject();
            jsonObject.put("mcEventIdAuto", eventId);
            jsonObject.put("mcContentAuto", content);




          LogUtils.log(jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
