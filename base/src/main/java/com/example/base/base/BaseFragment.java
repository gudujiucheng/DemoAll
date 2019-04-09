package com.example.base.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BaseFragment extends Fragment {
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        logD("onAttach context");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        logD("onAttach activity");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logD("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logE("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        logD("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        logD("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        logD("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        logD("onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        logD("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logE("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logD("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logD("onDetach");
    }

    public void log(String msg) {
        Log.e(this.getClass().getSimpleName(), "tag:" + this.getTag() + " " + msg);
    }

    public void logE(String msg) {
        Log.e("BaseFragment", this.getClass().getSimpleName() + " tag:" + this.getTag() + " " + msg);
    }

    public void logD(String msg) {
        Log.d("BaseFragment", this.getClass().getSimpleName() + " tag:" + this.getTag() + " " + msg);
    }


    public void showToast(String tips) {
        Toast.makeText(mContext, tips, Toast.LENGTH_SHORT).show();
    }
}
