package com.example.administrator.demoall.design;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.demoall.R;

public class BuilderDesignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder_design);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog();

            }
        });
    }

    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("title")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("message")
                .setPositiveButton("bt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(BuilderDesignActivity.this,"xxxxxxx",Toast.LENGTH_LONG).show();
                    }
                });


        builder.create()//返回dialog对象
                .show();//show
    }
}
