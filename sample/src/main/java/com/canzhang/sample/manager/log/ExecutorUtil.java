package com.canzhang.sample.manager.log;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ExecutorUtil {
    private static ExecutorService sExecutorService;

    private ExecutorUtil() {
    }

    public static void execute(Runnable r) {
        if (sExecutorService == null) {
            sExecutorService = new ThreadPoolExecutor(1, 5, 60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    new ThreadPoolExecutor.AbortPolicy());

        }
        sExecutorService.execute(r);
    }
}