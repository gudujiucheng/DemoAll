package com.canzhang.sample.manager.thread.demo.fqlreport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.LongSparseArray;

import com.canzhang.sample.manager.thread.demo.fqlreport.bean.ReportWrapper;
import com.canzhang.sample.manager.thread.demo.fqlreport.pool.ImmedTemPool;
import com.canzhang.sample.manager.thread.demo.fqlreport.pool.ReportPool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 分发上报数据的线程
 * Created by liveeili on 2017/7/26.
 */
@SuppressLint("Java  Def")
public class DispatchThread {

    private static final int NO_VALUE = -1;


    private static int MAX_HOLD = 10;


    private final Context mContext;
    private final ExecutorService mDispatchExecutor;
    private final ReportPool mReportPool;
    private final ImmedTemPool mImmedTemPool;
    private final ReportId mReportId;


    public DispatchThread(Context context) {
        mContext = context;
        mReportId = ReportId.instance(context);
        mReportPool = new ReportPool();
        mImmedTemPool = new ImmedTemPool();
        mDispatchExecutor = Executors.newSingleThreadExecutor();
    }


    public void offer(final ReportWrapper... wrappers) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatch(wrappers);
            }
        });
    }


    private void dispatch(ReportWrapper... wrappers) {
        LogUtils.log("上报第三步 dispatch");
        ReportWrapper lastWrapper = null;
        long lastId = NO_VALUE;
        for (int i = 0; i < wrappers.length; i++) {
            ReportWrapper wrapper = wrappers[i];
            if (wrapper == null || wrapper.getRecordList() == null || wrapper.getRecordList().length() == 0) {
                continue;
            }
            lastWrapper = wrapper;
            lastId = mReportId.getAndIncrement();
            lastWrapper.setId(lastId);
            mReportPool.put(lastId, lastWrapper);
            LogUtils.log("添加到mReportPool size=" + mReportPool.size());
        }
        if (lastWrapper == null) {
            return;
        }

        // 超过最大持有量，立即上报
        if (mReportPool.size() >= MAX_HOLD) {
            // 清空池子并上报
            LogUtils.log("达到最大持有量 maxHold=" + MAX_HOLD + " 清空池子上报 size=" + mReportPool.size());
            report(mReportPool.cloneAndClear());

            return;
        }
    }


    @SuppressLint("UseSparseArrays")
    private JSONArray merge(final LongSparseArray<ReportWrapper> dataArr) {
        HashMap<Integer, LinkedList<Object>> map = new HashMap<>();
        for (int i = 0; i < dataArr.size(); i++) {
            ReportWrapper wrapper = dataArr.valueAt(i);
            if (wrapper == null || wrapper.getRecordList() == null) {
                continue;
            }
            int type = wrapper.getDataType();
            LinkedList<Object> objects = map.get(type);
            boolean isInMap;
            if (objects == null) {
                isInMap = false;
                objects = new LinkedList<>();
            } else {
                isInMap = true;
            }
            JSONArray jsonArray = wrapper.getRecordList();
            for (int j = 0; j < jsonArray.length(); j++) {
                try {
                    Object obj = jsonArray.get(j);
                    if (obj != null) {
                        objects.add(obj);
                    }
                } catch (JSONException e) {

                }
            }
            if (!isInMap) {
                map.put(type, objects);
            }
        }

        JSONArray dataList = new JSONArray();
        Set<Integer> keySet = map.keySet();
        for (int type : keySet) {
            JSONObject dataObject = new JSONObject();
            try {
                JSONArray records = new JSONArray();
                LinkedList<Object> objects = map.get(type);
                for (Object object : objects) {
                    records.put(object);
                }
                dataObject.put("record_list", records);
                dataObject.put("data_type", type);
            } catch (JSONException e) {

                continue;
            }
            dataList.put(dataObject);
        }

        return dataList;
    }

    /**
     * 上传成功则删除数据库中对应的数据,上报失败则增量备份到数据库
     */
    private void report(final LongSparseArray<ReportWrapper> dataArr) {
        if (dataArr == null || dataArr.size() == 0) {
            return;
        }


        // 网络已连接 合入历史因网络因素未上传的实时数据
        if (mImmedTemPool.size() > 0) {
            // 只在dispatch线程操作缓存数据
            LongSparseArray<ReportWrapper> array = mImmedTemPool.cloneAndClear();
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    ReportWrapper wrapper = array.valueAt(i);
                    dataArr.put(wrapper.getId(), wrapper);
                }
            }
        }

        // 合并同一数据类型到一个数组
        JSONArray dataList = merge(dataArr);
        if (dataList == null || dataList.length() == 0) {
            return;
        }

        LogUtils.log("上报数据：" + dataList.toString());
    }


    public void execute(Runnable command) {
        mDispatchExecutor.execute(command);
    }


}
