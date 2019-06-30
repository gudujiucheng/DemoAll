package com.canzhang.sample.manager.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.aidl.Book;
import com.canzhang.sample.manager.aidl.IBookManager;
import com.example.base.base.BaseFragment;

import java.util.List;

/**
 * aidl 客户端代码
 * <p>
 * https://blog.csdn.net/luoyanglizi/article/details/51980630
 * <p>
 * https://www.cnblogs.com/huangjialin/p/7738104.html
 */
public class MessengerClientFragment extends BaseFragment {


    private static final String TAG = "MainActivity";
    private static final int MSG_SUM = 0x110;

    private Button mBtnAdd;
    private LinearLayout mLyContainer;
    //显示连接状态
    private TextView mTvState;

    private Messenger mService;
    private boolean isConn;


    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgFromServer) {
            switch (msgFromServer.what) {
                case MSG_SUM:
                    TextView tv = (TextView) mLyContainer.findViewById(msgFromServer.arg1);
                    tv.setText(tv.getText() + "=>" + msgFromServer.arg2);
                    break;
            }
            super.handleMessage(msgFromServer);
        }
    });


    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //TODO 第二步：在onServiceConnected中拿到回调的service（IBinder）对象，通过service对象去构造一个mService =new Messenger(service);
            mService = new Messenger(service);
            isConn = true;
            mTvState.setText("connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            isConn = false;
            mTvState.setText("disconnected!");
        }
    };

    private int mA;


    public static Fragment newInstance() {
        return new MessengerClientFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_messenger_client, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        //开始绑定服务
        bindServiceInvoked();

        mTvState = view.findViewById(R.id.id_tv_callback);
        mBtnAdd = view.findViewById(R.id.id_btn_add);
        mLyContainer = view.findViewById(R.id.id_ll_container);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int a = mA++;
                    int b = (int) (Math.random() * 100);

                    //创建一个tv,添加到LinearLayout中
                    TextView tv = new TextView(mContext);
                    tv.setText(a + " + " + b + " = caculating ...");
                    tv.setId(a);
                    mLyContainer.addView(tv);

                    Message msgFromClient = Message.obtain(null, MSG_SUM, a, b);
                    msgFromClient.replyTo = mMessenger;
                    if (isConn) {
                        //TODO 第三步：然后就可以使用mService.send(msg)给服务端了。
                        //往服务端发送消息
                        mService.send(msgFromClient);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    private void bindServiceInvoked() {
        //TODO 第一步 ：首先bindService
        Intent intent = new Intent();
        intent.setAction("com.zhy.aidl.calc");
        mContext.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        log("bindService invoked !");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        mContext.unbindService(mConn);
    }
}
