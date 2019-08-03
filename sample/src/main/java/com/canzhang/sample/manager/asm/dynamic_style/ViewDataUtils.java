package com.canzhang.sample.manager.asm.dynamic_style;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;


/**
 * view相关信息获取工具类
 *
 * @author canzhang
 */
class ViewDataUtils {


    /**
     * 获取view的预埋标示（fql_auto_bury_point_tag）
     *
     * @param view
     * @return
     */
    public static String getViewTag(View view) {
        if (view == null) {
            return null;
        }
        try {
            Object viewTag = view.getTag(R.id.fql_auto_bury_point_tag);
            if (viewTag != null) {//如果设置有指定tag，则取指定当前容器名+tag 为path

                LogUtils.log("成功获取到tag：" + viewTag);
                return viewTag.toString();
            }
        } catch (Throwable e) {
            LogUtils.log("有异常" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * list 可以暂时不优先
     * 点击来源要记录
     * page访问记录也要接入
     * 文本信息，以及原始id
     *
     * @param view
     * @return
     */
    public static String getCustomViewPath(View view) {
        if (view == null) {
            return null;
        }
        try {

            View targetView = view;
            StringBuilder path = new StringBuilder();
            Activity activity = ViewDataUtils.getActivityFromView(view);
            if (activity != null) {
                path.append(activity.getClass().getCanonicalName()).append("_");
            }
            String viewId = getViewId(targetView);
            if (!TextUtils.isEmpty(viewId)) {
                path.append(viewId).append("_");
            }
            do {
                if (targetView.getId() == android.R.id.content) {//已经到跟布局了,这里主要是为了避免不同ROM,不同版本SDK的差异
                    LogUtils.log("已经到跟布局了，返回");
                    break;
                }


                String name = targetView.getClass().getSimpleName();
                View parent = (View) targetView.getParent();
                if (parent instanceof ViewGroup) {//如果是group类型，当前控件的索引
                    int index = ((ViewGroup) parent).indexOfChild(targetView);
                    path.append(name).append("_").append(index).append("_");
                }
                String parentId = getViewId(parent);
                if (!TextUtils.isEmpty(parentId)) {
                    path.append(parentId).append("_");
                }
                Object nextView = parent.getParent();//然后取父布局的父布局
                if (nextView instanceof View) {//如果依然是view类型，则继续循环
                    targetView = parent;
                } else {//非view 类型，则跳出循环
                    LogUtils.log("非view 跳出循环 " + (nextView != null ? nextView.getClass().getName() : ""));
                    break;
                }

            } while (true);
            LogUtils.log("最终path:" + path.toString());
            return path.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
            return null;
        }

    }


    /**
     * 获取 view 的 anroid:id 对应的字符串
     *
     * @param view View
     * @return String
     */
    public static String getViewId(View view) {//这里如果取不到id，可以考虑继续从子布局里面获取
        String idString = null;
        try {
            if (view.getId() != View.NO_ID) {
                idString = view.getContext().getResources().getResourceEntryName(view.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
        }
        return idString;
    }


    public static String traverseViewContent(View view) {
        return traverseViewContent(new StringBuilder(), view);
    }

    private static String traverseViewContent(StringBuilder stringBuilder, View view) {
        try {
            if (view == null) {
                return stringBuilder.toString();
            }

            if (view instanceof ViewGroup) {
                ViewGroup root = (ViewGroup) view;
                final int childCount = root.getChildCount();
                for (int i = 0; i < childCount; ++i) {
                    final View child = root.getChildAt(i);

                    if (child.getVisibility() != View.VISIBLE) {
                        continue;
                    }

                    if (child instanceof ViewGroup) {
                        traverseViewContent(stringBuilder, child);
                    } else {
                        CharSequence viewText = getViewContent(child);
                        if (!TextUtils.isEmpty(viewText)) {
                            stringBuilder.append(viewText.toString());
                            stringBuilder.append("-");
                        }
                    }
                }

            } else {
                CharSequence viewText = getViewContent(view);
                if (!TextUtils.isEmpty(viewText)) {
                    stringBuilder.append(viewText.toString());
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
            return stringBuilder.toString();
        }
    }

    @Nullable
    private static CharSequence getViewContent(View child) {

        try {
            CharSequence viewText = null;
            if (child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                viewText = checkBox.getText();
            } else if (child instanceof SwitchCompat) {
                SwitchCompat switchCompat = (SwitchCompat) child;
                viewText = switchCompat.getTextOn();
            } else if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                viewText = radioButton.getText();
            } else if (child instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) child;
                boolean isChecked = toggleButton.isChecked();
                if (isChecked) {
                    viewText = toggleButton.getTextOn();
                } else {
                    viewText = toggleButton.getTextOff();
                }
            } else if (child instanceof Button) {
                Button button = (Button) child;
                viewText = button.getText();
            } else if (child instanceof CheckedTextView) {
                CheckedTextView textView = (CheckedTextView) child;
                viewText = textView.getText();
            } else if (child instanceof TextView) {
                TextView textView = (TextView) child;
                viewText = textView.getText();
            } else if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;
                if (!TextUtils.isEmpty(imageView.getContentDescription())) {
                    viewText = imageView.getContentDescription().toString();
                }
            }
            return viewText;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取 View 所属 Activity
     *
     * @param view View
     * @return Activity
     */
    public static Activity getActivityFromView(View view) {
        Activity activity = null;
        if (view == null) {
            return null;
        }

        try {
            Context context = view.getContext();
            if (context != null) {
                if (context instanceof Activity) {
                    activity = (Activity) context;
                } else if (context instanceof ContextWrapper) {
                    while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    }
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
        }
        return activity;
    }

    public static Activity getActivityFromContext(Context context) {
        Activity activity = null;
        try {
            if (context != null) {
                if (context instanceof Activity) {
                    activity = (Activity) context;
                } else if (context instanceof ContextWrapper) {
                    while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    }
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
        }
        return activity;
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        try {
            for (int i = 0; i < indent; i++) {
                sb.append('\t');
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
        }
    }

    /**
     * 格式化字符串，方便查看
     *
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        try {
            if (null == jsonStr || "".equals(jsonStr)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            char last;
            char current = '\0';
            int indent = 0;
            boolean isInQuotationMarks = false;
            for (int i = 0; i < jsonStr.length(); i++) {
                last = current;
                current = jsonStr.charAt(i);
                switch (current) {
                    case '"':
                        if (last != '\\') {
                            isInQuotationMarks = !isInQuotationMarks;
                        }
                        sb.append(current);
                        break;
                    case '{':
                    case '[':
                        sb.append(current);
                        if (!isInQuotationMarks) {
                            sb.append('\n');
                            indent++;
                            addIndentBlank(sb, indent);
                        }
                        break;
                    case '}':
                    case ']':
                        if (!isInQuotationMarks) {
                            sb.append('\n');
                            indent--;
                            addIndentBlank(sb, indent);
                        }
                        sb.append(current);
                        break;
                    case ',':
                        sb.append(current);
                        if (last != '\\' && !isInQuotationMarks) {
                            sb.append('\n');
                            addIndentBlank(sb, indent);
                        }
                        break;
                    default:
                        sb.append(current);
                }
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("有异常" + e.getMessage());
            return "";
        }
    }
}
