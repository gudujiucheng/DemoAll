package com.canzhang.sample.manager.jetpack.livedata;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * viewmodlue 基本原理分析 https://blog.csdn.net/xfhy_/article/details/88703853
 *
 *
 */
public class TestViewModel extends ViewModel {//ViewModel只是用来管理UI的数据的,千万不要让它持有View、Activity或者Fragment的引用(小心内存泄露)
    //配合livedata使用，实现观察者效果（activity、fragment为观察者，viewmodel为被观察者）
//    LiveData是一个抽象类，不能直接使用，所以通常我们使用它的直接子类MutableLiveData。
    private MutableLiveData<String> mNameEvent = new MutableLiveData<>();

    public MutableLiveData<String> getNameEvent() {
        return mNameEvent;
    }
}
