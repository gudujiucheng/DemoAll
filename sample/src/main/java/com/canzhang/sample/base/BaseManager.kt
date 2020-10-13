package com.canzhang.sample.base

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.canzhang.sample.base.bean.ComponentItem
import com.component.debugdialog.DebugDialog
import com.example.base.base.AppProxy

abstract class BaseManager : IManager {
    @JvmField
    var mActivity: Activity? = null
    fun showTipsDialog(msg: String?) {
        DebugDialog.getInstance().show("", msg)
    }

    fun showTipsDialog(title: String?, msg: String?) {
        DebugDialog.getInstance().show(title, msg)
    }

    fun log(msg: String) {
        Log.e("Test", javaClass.simpleName + ":" + msg)
    }

    fun showToast(msg: String?) {
        val application = AppProxy.getInstance().application
        if (application != null) {
            Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getPriority(): Int {
        return 0
    }

    override fun getSampleItem(activity: Activity?): List<ComponentItem?>? {
        mActivity = activity
        return null
    }
}