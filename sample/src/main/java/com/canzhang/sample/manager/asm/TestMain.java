package com.canzhang.sample.manager.asm;

import com.canzhang.sample.manager.asm.slow_method.SlowMethodHelper;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/10/9 15:05
 */
public class TestMain {
    public void test() {
        SlowMethodHelper.setStartTime("Test", System.nanoTime());
        SlowMethodHelper.setEndTime("Test2", System.nanoTime());
    }
}
