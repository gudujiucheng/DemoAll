package com.canzhang.oterTest.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 *
 * messenger ipc 服务端
 *
 *
 *
 * 作者：鸿洋_
 * 来源：CSDN
 * 原文：https://blog.csdn.net/lmj623565791/article/details/47017485
 * 版权声明：本文为博主原创文章，转载请附上博文链接！
 */
public class MessengerService extends Service {

    private static final int MSG_SUM = 0x110;

    //TODO 第一步： 只需要去声明一个Messenger对象
    //最好换成HandlerThread的形式
    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgfromClient) {
            //TODO 第三步：坐等客户端将消息发送这里，根据message.what去判断进行什么操作，然后做对应的操作
            Message msgToClient = Message.obtain(msgfromClient);//返回给客户端的消息
            switch (msgfromClient.what) {
                //msg 客户端传来的消息
                case MSG_SUM:
                    msgToClient.what = MSG_SUM;
                    try {
                        //模拟耗时
                        Thread.sleep(2000);
                        msgToClient.arg2 = msgfromClient.arg1 + msgfromClient.arg2;
                        //TODO 第四步：最终将结果通过 msgfromClient.replyTo.send(msgToClient);返回。
                        msgfromClient.replyTo.send(msgToClient);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            super.handleMessage(msgfromClient);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        //TODO 第二步：然后onBind方法返回mMessenger.getBinder()；
        return mMessenger.getBinder();
    }
}

