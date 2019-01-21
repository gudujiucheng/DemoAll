package com.example.administrator.demoall.webview.cache.dynamic;



import android.util.Log;

import com.example.administrator.demoall.webview.cache.bean.Cache;
import com.example.administrator.demoall.webview.cache.bean.MemoryObject;
import com.example.administrator.demoall.webview.cache.config.WebCacheConfig;
import com.example.administrator.demoall.webview.cache.disklru.DiskLruCache;
import com.example.administrator.demoall.webview.cache.disklru.LruCacheHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author owenli
 * @date 2018/11/21
 */
class ResourceInputStream extends InputStream {

    private OutputStream mOutputStream;
    private InputStream mInnerInputStream;
    private ByteArrayOutputStream mRamArray;
    private int mCurrentReadLength;
    private DiskLruCache.Editor mEditor;
    private boolean isReadOver = false;
    private long mFileSize;
    private String mFileName;
    private String mUrl;
    private Cache mCache;

    public ResourceInputStream(String url, InputStream inputStream, String fileName, long fileSize, Cache cache) {
        mUrl = url;
        mInnerInputStream = inputStream;
        mFileSize = fileSize;
        mFileName = fileName;
        mCache = cache;
        if (mCache != null) {
            if (mCache.enableDisk) {
                mEditor = LruCacheHelper.getInstance().getEditor(fileName);
                if (mEditor != null) {
                    try {
                        mOutputStream = mEditor.newOutputStream(0);
                    } catch (IOException e) {

                    }
                }
            }
            if (mCache.enableMemory) {
                mRamArray = new ByteArrayOutputStream();
            }
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    private void writeStream(byte[] b, int off, int len) {
        if (!mCache.enableMemory && !mCache.enableDisk) {
            return;
        }

        if (len > 0) {
            mCurrentReadLength += len;
            if (mOutputStream != null) {
                try {
                    mOutputStream.write(b, off, len);//TODO
                } catch (IOException e) {

                }
            }
            if (mRamArray != null) {
                mRamArray.write(b, off, len);//TODO 这里写了两次  性能内存 不太有利
            }
        }


    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = mInnerInputStream.read(b, off, len);
        if (count == -1) {
            isReadOver = true;
        }
        writeStream(b, off, count);
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        return mInnerInputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return mInnerInputStream.available();
    }

    private void streamClose() throws Exception {
        mInnerInputStream.close();

        if (!mCache.enableMemory && !mCache.enableDisk) {
            return;
        }

        if (!isReadOver || (mFileSize > 0 && mCurrentReadLength != mFileSize)) {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (mEditor != null) {
                mEditor.abort();
            }
            Log.d("WebCache", "abort1 = " + mFileName + ",url = " + mUrl
                    + ",isReadOver = " + isReadOver + ",mFileSize = " + mFileSize
                    + ",mCurrentReadLength = " + mCurrentReadLength + ",mFileSize = " + mFileSize);
            WebCacheConfig.sBlacklist.add(mFileName);
            return;
        }

        if (mRamArray != null) {
            try {
                MemoryObject memory = new MemoryObject();
                byte[] buffer = mRamArray.toByteArray();
                memory.setData(buffer);
                memory.setSize(buffer.length);
                LruCacheHelper.getInstance().getMemoryLruCache().put(mFileName, memory);
                Log.d("WebCache", "memory success = " + mFileName + ",url = " + mUrl);
            } catch (Exception e) {
            }
        }

        if (mOutputStream != null) {
            mOutputStream.flush();
            mOutputStream.close();
            mEditor.commit();
            Log.d("WebCache", "disk success = " + mFileName);
        } else {
            if (mEditor != null) {
                mEditor.abort();
                Log.d("WebCache", "disk abort2 = " + mFileName + ",url = " + mUrl);
            }
        }
    }

    @Override
    public void close() {
        try {
            streamClose();
        } catch (Exception e) {

        }
    }

    @Override
    public synchronized void mark(int readLimit) {
        mInnerInputStream.mark(readLimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        mInnerInputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return mInnerInputStream.markSupported();
    }

    @Override
    public int read() throws IOException {
        return mInnerInputStream.read();
    }
}
