package com.canzhang.sample.manager.sockettest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketTestActivity extends BaseActivity {


    //IP地址和端口号
    public static String IP_ADDRESS = "";
    public static int PORT = 9998;
    //三个控件
    EditText et_message = null;  //需要发送的内容
    Button bt_getAdress = null;   //获取本机IP地址
    Button bt_connect = null;   //连接并发送
    Button bt_startServer = null;   //启动服务端
    TextView tv_adress = null;   //ip地址
    TextView tv_reply = null;   //服务器回复的消息
    //handler
    Handler handler = null;
    Socket soc = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    String messageRecv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_socket_test);
        et_message = findViewById(R.id.et_message);
        bt_getAdress = findViewById(R.id.bt_getAdress);
        bt_connect = findViewById(R.id.bt_connect);
        bt_startServer = findViewById(R.id.bt_startServer);

        tv_adress = findViewById(R.id.tv_adress);
        tv_reply = findViewById(R.id.tv_reply);
        bt_getAdress.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    InetAddress addr = InetAddress.getLocalHost();
                    Log.e("CAN_TEST","local host:"+addr);
                    runOnUiThread(() -> tv_adress.setText(addr.toString().split("/")[1]));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        bt_startServer.setOnClickListener(v -> {
            //启动服务
            new Thread(() -> new Server().startService()).start();
            Toast.makeText(SocketTestActivity.this,"服务已启动",Toast.LENGTH_SHORT).show();
        });
        bt_connect.setOnClickListener(v -> {
            IP_ADDRESS = tv_adress.getText().toString();
            new ConnectionThread(et_message.getText().toString()).start();
        });
        handler = new Handler(msg -> {
            Bundle b = msg.getData();  //获取消息中的Bundle对象
            String str = b.getString("data");  //获取键为data的字符串的值
            tv_reply.setText(str);
            return false;
        });
    }

    //新建一个子线程，实现socket通信
    class ConnectionThread extends Thread {
        String message = null;

        public ConnectionThread(String msg) {
            message = msg;
        }

        @Override
        public void run() {
            if (soc == null) {
                try {
                    //Log.d("socket","new socket");
                    if ("".equals(IP_ADDRESS)) {
                        return;
                    }
                    soc = new Socket(IP_ADDRESS, PORT);
                    //获取socket的输入输出流
                    dis = new DataInputStream(soc.getInputStream());
                    dos = new DataOutputStream(soc.getOutputStream());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                dos.writeUTF(message);//客户端发送
                dos.flush();
                messageRecv = dis.readUTF();//客户端接收 如果没有收到数据，会阻塞
                Log.e("CAN_TEST","msg from server:"+messageRecv);
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("data", messageRecv);
                msg.setData(b);
                handler.sendMessage(msg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}