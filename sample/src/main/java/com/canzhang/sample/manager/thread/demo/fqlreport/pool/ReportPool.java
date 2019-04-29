package com.canzhang.sample.manager.thread.demo.fqlreport.pool;

import android.util.LongSparseArray;

import com.canzhang.sample.manager.thread.demo.fqlreport.bean.ReportWrapper;

/**
 * 上报数据的缓存池
 * Created by liveeili on 2017/7/25.
 */
public class ReportPool {

    private volatile LongSparseArray<ReportWrapper> mPool;

    public ReportPool() {
        mPool = new LongSparseArray<>(10);
    }

    public void put(long key, ReportWrapper value) {
        mPool.put(key, value);
    }

    public void remove(long key) {
        mPool.remove(key);
    }

    public ReportWrapper get(long key) {
        return mPool.get(key);
    }

    public LongSparseArray<ReportWrapper> getAll() {
        return mPool;
    }

    public int size() {
        return mPool.size();
    }

    public void clear() {
        mPool.clear();
    }

    public LongSparseArray<ReportWrapper> cloneOnly() {
        return mPool.clone();
    }

    public LongSparseArray<ReportWrapper> cloneAndClear() {
        LongSparseArray<ReportWrapper> clone = mPool;
        mPool = new LongSparseArray<>(10);
        return clone;
    }


}
