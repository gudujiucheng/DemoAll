package com.canzhang.sample.manager.thread.demo.fqlreport.pool;

import android.util.LongSparseArray;

import com.canzhang.sample.manager.thread.demo.fqlreport.bean.ReportWrapper;

/**
 * 实时数据的临时缓存池
 * Created by liveeili on 2017/7/25.
 */
public class ImmedTemPool {

    private final int MAX_SIZE = 30;

    private volatile LongSparseArray<ReportWrapper> mPool;

    public ImmedTemPool() {
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


    public LongSparseArray<ReportWrapper> cloneAndClear() {
        LongSparseArray<ReportWrapper> clone = mPool;
        mPool = new LongSparseArray<>(10);
        return clone;
    }

    public boolean isFull() {
        return mPool.size() >= MAX_SIZE;
    }


}
