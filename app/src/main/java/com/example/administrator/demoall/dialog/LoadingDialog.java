package com.example.administrator.demoall.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.demoall.R;


public class LoadingDialog extends Dialog {


    private TextView mTvContent;

    private LoadingDialog(@NonNull Context context) {
        super(context);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);

    }

    public void setView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_dialog, null, false);
        mTvContent = (TextView) inflate.findViewById(R.id.mTvContent);
        setContentView(inflate);

    }

    public static LoadingDialog get(Context context) {
        LoadingDialog dialog = new LoadingDialog(context, R.style.loading_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setView();
        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        return dialog;
    }

    public LoadingDialog setContent(String content) {
        if (mTvContent != null) {
            mTvContent.setText(content);
        }
        return this;
    }

    public LoadingDialog setLoadingCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    public LoadingDialog setLoadingCancelable(boolean cancel) {
        setCancelable(cancel);
        return this;
    }


}
