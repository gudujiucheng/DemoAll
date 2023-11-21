package com.canzhang.sample.manager.android_11;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CommandExecutor {
    private static final int SOCKET_PORT = 52011;
    private Socket socket;

    public void init() {
        Log.d("CommandExecutor","init");
        ThreadUtil.run(new Runnable() {
            @Override
            public void run() {
                getSocket();
            }
        });
    }

    public boolean available() {
        Log.d("CommandExecutor","CommandExecutor.available() " + (socket != null));
        return socket != null;
    }

    public boolean exec(final String cmd) {
        Log.d("CommandExecutor","CommandExecutor.exec()    尝试执行命令：" + cmd);
        final CountDownLatch latch = new CountDownLatch(1);
        ThreadUtil.runQueue(new Runnable() {
            @Override
            public void run() {
                if (latch.getCount() != 0) {
                    socketSend(cmd);
                    latch.countDown();
                }
            }
        });

        try {
            boolean timeout = !latch.await(3, TimeUnit.SECONDS);
            if (!timeout) {
                return true;
            }
            Log.e("CommandExecutor", "执行命令超时：" + cmd);
            // 如果超时则放弃，并将 latch 置 0，以便尽量通知到子线程不要再执行这条命令了
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("CommandExecutor", "执行命令出错：" + e.toString());
        }
        return false;
    }

    private Socket getSocket() {
        synchronized (this) {
            if (socket == null) {
                socket = socketConnect();
            }
            return socket;
        }
    }

    private Socket socketConnect() {
        Log.d("CommandExecutor", "CommandExecutor.socketConnect()");
        try {
            // 创建客户端 Socket
            return new Socket(InetAddress.getLocalHost(), SOCKET_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CommandExecutor", "连接 socket 失败：" + e.toString());
            return null;
        }
    }

    private boolean socketSend(String cmd) {
        Log.d("CommandExecutor", "CommandExecutor.socketSend()    通过 socket 发送命令：" + cmd);
        try {
            Socket socket = getSocket();
            if (socket == null) {
                return false;
            }

            // 获取输出流，向服务器发送数据
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write(cmd);
            pw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CommandExecutor", "通过 socket 发送命令失败：" + e.toString());
        }

        return false;
    }
}
