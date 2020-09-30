package com.example.administrator.demoall;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.administrator.SafeLog;
import com.example.administrator.demoall.filemanager.FqlFileManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.administrator.demoall", appContext.getPackageName());
    }


    @Test
    public void test() {
        //测试File.separator 是否会自动去除
        FqlFileManager.getInstance().getDownloadRootPath();
        String s =  FqlFileManager.getInstance().getWeexLoadPath();
        String s2 =  FqlFileManager.getInstance().getWeexLoadPath2();
        String path = new File(s,"xxx").getAbsolutePath();
        SafeLog.log("s:"+s);
        String path2 = new File(s2,"xxx").getAbsolutePath();
        SafeLog.log("s2:"+s2);
        SafeLog.log("path:"+path);
        SafeLog.log("path2:"+path2);
    }


}
