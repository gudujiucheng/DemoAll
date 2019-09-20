package com.canzhang.sample.manager.thread.fqlthreadpool;

/**
 * Created by owenli on 2018/4/9.
 */
public class DefaultTask extends AbstractTask {

    Runnable runnable;

    public DefaultTask(Runnable runnable) {
        super(NORMAL);
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if (runnable != null) {
            runnable.run();
        }
    }
}
