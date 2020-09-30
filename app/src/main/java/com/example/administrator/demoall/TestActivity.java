package com.example.administrator.demoall;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.demoall.bean.xuliehua.ParcelableBean;
import com.example.administrator.demoall.bean.xuliehua.SerializeBean;

public class TestActivity extends AppCompatActivity {
    public static final String SERIALIZE = "SERIALIZE";
    public static final String PARCELABLE = "PARCELABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        SerializeBean bean = (SerializeBean) getIntent().getSerializableExtra(SERIALIZE);
        ParcelableBean parcelableBean = (ParcelableBean) getIntent().getParcelableExtra(PARCELABLE);
        Log.e("mm","传递后："+bean.toString());
        Log.e("mm","2222传递后："+parcelableBean.toString());
    }


    public static void startTestActivity(Context context){
        Intent intent =   new Intent(context, TestActivity.class);
        SerializeBean bean = new SerializeBean("haha",11);
        ParcelableBean parcelableBean = new ParcelableBean();
        parcelableBean.age = 17;
        parcelableBean.name = "xxx";
        intent.putExtra(SERIALIZE,bean);
        intent.putExtra(PARCELABLE,parcelableBean);
        Log.e("mm","传递前："+bean.toString());
        context.startActivity(intent);
    }
}
