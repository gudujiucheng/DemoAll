package com.canzhang.sample.manager.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

import java.util.List;

/**
 * aidl 客户端代码
 * <p>
 * https://blog.csdn.net/luoyanglizi/article/details/51980630
 * <p>
 * https://www.cnblogs.com/huangjialin/p/7738104.html
 */
public class AidlClientFragment extends BaseFragment {

    //由AIDL文件生成的Java类
    private IBookManager mBookManager = null;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    //包含Book对象的list
    private List<Book> mBooks;


    public static Fragment newInstance() {
        return new AidlClientFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_aidl_client, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        view.findViewById(R.id.bt_add_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        view.findViewById(R.id.bt_connect_aidl_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBound) {
                    attemptToBindService();
                }
            }
        });

        view.findViewById(R.id.bt_disconnect_aidl_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    mContext.unbindService(mServiceConnection);
                    mBound = false;
                }
            }
        });

    }


    private static int position = 0;
    /**
     * 按钮的点击事件，点击之后调用服务端的addBookIn方法
     */
    private void addBook() {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            showToast("当前与服务端处于未连接状态，正在尝试重连，请稍后再试");
            return;
        }
        if (mBookManager == null) return;

        Book book = new Book();
        book.setName("来自客户端的书 "+position);
        book.setPrice(30);
        try {
            mBookManager.addBook(book);
            log(book.toString());
            position++;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.canzhang.aidl");
        //从 Android 5.0开始 隐式Intent绑定服务的方式已不能使用,所以这里需要设置Service所在服务端的包名
        intent.setPackage("com.canzhang.aidlservice");
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            mContext.unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            log("log from client :service connected");
            mBookManager = IBookManager.Stub.asInterface(service);
            mBound = true;

            if (mBookManager != null) {
                try {
                    mBooks = mBookManager.getBookList();
                    log("log from client :"+mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            log("log from client :service disconnected");
            mBound = false;
        }
    };


}
