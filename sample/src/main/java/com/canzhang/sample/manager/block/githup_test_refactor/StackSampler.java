package com.canzhang.sample.manager.block.githup_test_refactor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @Description: 堆栈异常信息采样
 * @Author: canzhang
 * @CreateDate: 2019/9/26 11:37
 */
public class StackSampler extends AbstractSampler {
    public static final String SEPARATOR = "\r\n";
    private static final int DEFAULT_MAX_ENTRY_COUNT = 100;
    private static final LinkedHashMap<Long, String> sStackMap = new LinkedHashMap<>();

    private int mMaxEntryCount = DEFAULT_MAX_ENTRY_COUNT;
    private Thread mCurrentThread;

    public StackSampler(Thread thread, long sampleIntervalMillis) {
        this(thread, DEFAULT_MAX_ENTRY_COUNT, sampleIntervalMillis);
    }

    public StackSampler(Thread thread, int maxEntryCount, long sampleIntervalMillis) {
        super(sampleIntervalMillis);
        mCurrentThread = thread;
        mMaxEntryCount = maxEntryCount;
    }

    public ArrayList<String> getThreadStackEntries(long startTime, long endTime) {
        ArrayList<String> result = new ArrayList<>();
        synchronized (sStackMap) {
            for (Long entryTime : sStackMap.keySet()) {
                if (startTime < entryTime && entryTime < endTime) {
                    result.add(SEPARATOR
                            + sStackMap.get(entryTime));
                }
            }
        }
        return result;
    }

    @Override
    protected void doSample() {
        StringBuilder stringBuilder = new StringBuilder();

        for (StackTraceElement stackTraceElement : mCurrentThread.getStackTrace()) {
            stringBuilder
                    .append(stackTraceElement.toString())
                    .append(SEPARATOR);
        }

        synchronized (sStackMap) {
            if (sStackMap.size() == mMaxEntryCount && mMaxEntryCount > 0) {
                sStackMap.remove(sStackMap.keySet().iterator().next());
            }
            sStackMap.put(System.currentTimeMillis(), stringBuilder.toString());
        }
    }
}
