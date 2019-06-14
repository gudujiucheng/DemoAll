package com.example.administrator.demoall.webview.cache.dynamic;



import android.util.Log;

import com.example.administrator.demoall.webview.cache.bean.Cache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author owenli
 * @date 2018/12/05
 */
public class HttpInputStream extends InputStream {

    private ResourceInputStream mResourceInputStream;
    private String mFileName;
    private String mUrl;
    private Cache mCache;

    public HttpInputStream(String url, String fileName, Cache cache) {
        mUrl = url;
        mFileName = fileName;
        mCache = cache;
    }

    @Override
    public int read() throws IOException {
        if (mResourceInputStream == null) {//TODO 204的时候 会陷入死循环    尝试在fq上实验下看看
            mResourceInputStream = createInputStream();
        }

        if (mResourceInputStream != null) {
            return mResourceInputStream.read();
        } else {
            return -1;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (mResourceInputStream == null) {
            mResourceInputStream = createInputStream();
        }

        if (mResourceInputStream != null) {
            return mResourceInputStream.read(b, off, len);
        } else {
            return -1;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        if (mResourceInputStream != null) {
            return mResourceInputStream.skip(n);
        }
        return super.skip(n);
    }

    @Override
    public int available() throws IOException {
        if (mResourceInputStream != null) {
            return mResourceInputStream.available();
        }
        return super.available();
    }

    @Override
    public void close() throws IOException {
        if (mResourceInputStream != null) {
            mResourceInputStream.close();
        }
        super.close();
    }


    @Override
    public synchronized void mark(int readLimit) {
        if (mResourceInputStream != null) {
            mResourceInputStream.mark(readLimit);
        }
        super.mark(readLimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        if (mResourceInputStream != null) {
            mResourceInputStream.reset();
        }
        super.reset();
    }

    @Override
    public boolean markSupported() {
        if (mResourceInputStream != null) {
            return mResourceInputStream.markSupported();
        }
        return super.markSupported();
    }

    private ResourceInputStream createInputStream() {
        Log.e("WebCache","owenli thread id stream = " + Thread.currentThread().getId() + ", url = " + mUrl);
        try {
            OkHttpClient client = new OkHttpClient();//TODO 改动的
            Request request = new Request.Builder().url(mUrl).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            int responseCode = response.code();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response.headers();//TODO 没有用到
                InputStream inputStream = response.body().byteStream();//TODO 可能出现空指针异常

                if (inputStream != null) {
                    return new ResourceInputStream(mUrl, inputStream,
                            mFileName, response.body().contentLength(), mCache);
                }
            }
        } catch (Exception e) {

        }
        return null;
    }
}
