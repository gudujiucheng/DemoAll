package com.canzhang.sample.manager.block.tencent_use;

import android.content.Context;
import android.util.Log;

import com.tencent.matrix.plugin.DefaultPluginListener;
import com.tencent.matrix.report.Issue;
import com.tencent.matrix.util.MatrixLog;

/**
 * @Description:实现 PluginListener，接收 Matrix 处理后的数据
 * @Author: canzhang
 * @CreateDate: 2019/9/29 17:23
 */
public class TestPluginListener extends DefaultPluginListener {
    public static final String TAG = "Matrix.TestPluginListener";
    public TestPluginListener(Context context) {
        super(context);

    }

    @Override
    public void onReportIssue(Issue issue) {
        super.onReportIssue(issue);
        MatrixLog.e(TAG, issue.toString());
        Log.e("Test", "异常信息信息----------------------》》》》："+issue.toString());

        //add your code to process data
    }
}