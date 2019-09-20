//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.canzhang.sample.manager.thread.fqlthreadpool;

import android.os.StrictMode;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ThreadPoolUtils {

    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 5;

    private static FqlPriorityBlockingQueue<Runnable> workQueue = new FqlPriorityBlockingQueue<>();
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadPool thread:" + this.integer.getAndIncrement());
        }
    };
    public static ThreadPoolExecutor threadPool;

    static {
        createThreadPool();
    }

    private static void createThreadPool(){
        int threadCount = calculateBestThreadCount();
        threadPool = new FqlThreadPoolExecutor(threadCount, threadCount,
                10 * 60, TimeUnit.SECONDS, workQueue, threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    private ThreadPoolUtils() {
    }

    public static void execute(Runnable runnable) {

        if (threadPool == null || threadPool.isShutdown()) {
            synchronized (ThreadPoolUtils.class) {
                if (threadPool == null || threadPool.isShutdown()) {
                    createThreadPool();
                }
            }
        }

        threadPool.execute(runnable);
    }

    public static int calculateBestThreadCount() {
        StrictMode.ThreadPolicy originalPolicy = StrictMode.allowThreadDiskReads();
        File[] cpus = null;
        try {
            File cpuInfo = new File(CPU_LOCATION);
            final Pattern cpuNamePattern = Pattern.compile(CPU_NAME_REGEX);
            cpus = cpuInfo.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return cpuNamePattern.matcher(s).matches();
                }
            });
        } catch (Throwable t) {
        } finally {
            StrictMode.setThreadPolicy(originalPolicy);
        }

        int cpuCount = cpus != null ? cpus.length : 0;
        int availableProcessors = Math.max(2, Runtime.getRuntime().availableProcessors());
        return Math.min(MAXIMUM_AUTOMATIC_THREAD_COUNT, Math.max(availableProcessors, cpuCount));
    }

    public static void setDebug(boolean debug){
        FqlPriorityBlockingQueue.DEBUG = debug;
    }

    public static int getMaxQueueSize(){
        return workQueue.getMaxSize();
    }
}
