package com.example.administrator.demoall.webview.cache.disklru;

import android.content.ComponentCallbacks2;
import android.util.LruCache;


import com.example.administrator.demoall.BaseApp;
import com.example.administrator.demoall.webview.cache.bean.MemoryObject;
import com.example.administrator.demoall.webview.cache.config.WebCacheConfig;

import java.io.File;
import java.io.IOException;

public class LruCacheHelper {

    private static final int CACHE_SIZE = 50 * 1024 * 1024;
    private static volatile LruCacheHelper mInstance;
    private LruCache<String, MemoryObject> mLruCache;
    private DiskLruCache mDiskLruCache;

    private LruCacheHelper() {
        if (mDiskLruCache == null) {
            try {
                mDiskLruCache = DiskLruCache.open(
                        new File(WebCacheConfig.getPageCachePath()),
                        1,
                        1, CACHE_SIZE);

            } catch (IOException e) {

            }

            mLruCache = new LruCache<String, MemoryObject>(CACHE_SIZE / 5) {
                @Override
                protected int sizeOf(String key, MemoryObject value) {
                    return value.getSize();
                }
            };
        }
    }

    public static LruCacheHelper getInstance() {
        if (mInstance == null) {
            synchronized (LruCacheHelper.class) {
                if (mInstance == null) {
                    mInstance = new LruCacheHelper();
                }
            }
        }
        return mInstance;
    }

    public LruCache<String, MemoryObject> getMemoryLruCache() {
        return mLruCache;
    }

    public DiskLruCache getDiskLruCache() {
        return mDiskLruCache;
    }

    public DiskLruCache.Editor getEditor(String key) {
        try {
            if (mDiskLruCache.isClosed()) {
                return null;
            }
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            return editor;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DiskLruCache.Snapshot get(String key) {
        try {
            return mDiskLruCache.get(key);
        } catch (IOException e) {
            return null;
        }
    }

    public void onTrimMemory(int level) {
        if(level != ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN ) {
            mLruCache.evictAll();
        }
    }
}
