package com.example.administrator.demoall.floatView;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.example.administrator.demoall.R;

public class LXCustomView extends FloatingMagnetView {

    public LXCustomView(Context context) {
        this(context, R.layout.lx_floating_view);
    }

    public LXCustomView(@NonNull Context context, @LayoutRes int resource) {
        super(context, null);
        inflate(context, resource, this);

    }


}
