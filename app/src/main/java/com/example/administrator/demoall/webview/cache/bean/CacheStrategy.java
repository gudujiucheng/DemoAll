package com.example.administrator.demoall.webview.cache.bean;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Owenli on 2018/12/05.
 */
public class CacheStrategy {
    private boolean enableMemory = false;
    private boolean enableDisk = false;
    private ArrayList<Pattern> cachePagesRegular;
    private ArrayList<String> diskCacheExt;
    private ArrayList<String> menCacheExt;

    public boolean isMemoryEnabled() {
        return enableMemory;
    }

    public void setMemoryEnabled(boolean enabled) {
        enableMemory = enabled;
    }

    public boolean isDiskEnabled() {
        return enableDisk;
    }

    public void setDiskEnabled(boolean enabled) {
        enableDisk = enabled;
    }

    public ArrayList<String> getDiskCacheExt() {
        return diskCacheExt;
    }

    public void setDiskCacheExt(ArrayList<String> diskCacheExt) {
        this.diskCacheExt = diskCacheExt;
    }

    public ArrayList<String> getMenCacheExt() {
        return menCacheExt;
    }

    public void setMenCacheExt(ArrayList<String> menCacheExt) {
        this.menCacheExt = menCacheExt;
    }

    public ArrayList<Pattern> getCachePagesRegular() {
        return cachePagesRegular;
    }

    public void setCachePagesRegular(ArrayList<Pattern> cachePagesRegular) {
        this.cachePagesRegular = cachePagesRegular;
    }
}
