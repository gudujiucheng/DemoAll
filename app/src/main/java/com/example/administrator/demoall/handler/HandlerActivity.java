package com.example.administrator.demoall.handler;

import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.demoall.R;

public class HandlerActivity extends AppCompatActivity {

  private Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what){
              case 1:
                  Toast.makeText(HandlerActivity.this,"收到消息",Toast.LENGTH_LONG).show();
                  break;
          }
      }
  };

    private Handler handler02 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(HandlerActivity.this,"handler02收到消息",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        handler.sendMessage(Message.obtain());
                        handler.sendEmptyMessage(1);
                        handler02.sendEmptyMessage(1);
                    }
                }.run();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
