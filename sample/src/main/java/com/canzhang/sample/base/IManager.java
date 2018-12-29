package com.canzhang.sample.base;

import android.app.Activity;


import com.canzhang.sample.bean.ComponentItem;

import java.util.List;

public interface IManager {
    List<ComponentItem> getSampleItem(Activity activity);
}
