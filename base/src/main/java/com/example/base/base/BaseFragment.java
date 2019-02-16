package com.example.base.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class BaseFragment extends Fragment {
    protected Context mContext;

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Test", this.getClass().getSimpleName());
    }

    public void log(String msg) {
        Log.e(this.getClass().getSimpleName(), msg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void showToast(String tips) {
        Toast.makeText(mContext, tips, Toast.LENGTH_SHORT).show();
    }
}
