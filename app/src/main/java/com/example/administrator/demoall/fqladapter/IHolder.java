package com.example.administrator.demoall.fqladapter;

import android.content.Context;

import java.util.List;

public interface IHolder<T> {
    void setDataOnUI(Context context, T bean, int position, List<T> data);
}
