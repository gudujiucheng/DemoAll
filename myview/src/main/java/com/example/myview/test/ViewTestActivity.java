package com.example.myview.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.myview.R;

public class ViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
       final  MyTestView view =  findViewById(R.id.mv);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                view.requestLayout();
                view.invalidate();
//                view.postInvalidate();
            }
        });
    }
}
