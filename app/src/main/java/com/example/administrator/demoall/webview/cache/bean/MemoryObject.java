package com.example.administrator.demoall.webview.cache.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Owenli on 2018/12/04.
 */
public class MemoryObject {
    private int size = 0;
    private byte[] buffer;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public InputStream getStream() {
        return new ByteArrayInputStream(buffer);
    }

    public void setData(byte[] buffer) {
        this.buffer = buffer;
    }

}
