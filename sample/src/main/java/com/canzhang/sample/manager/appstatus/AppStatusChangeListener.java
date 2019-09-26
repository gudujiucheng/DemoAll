package com.canzhang.sample.manager.appstatus;

/**
 * Created by owenli on 2018/2/24.
 */

public interface AppStatusChangeListener {

    /**
     * App 退到后台
     */
    void onAppToBackground();

    /**
     * App 从后台切回前台
     */
    void onAppToFront();

    /**
     * App 首次打开
     */
    void onAppFirstCreate();
}
