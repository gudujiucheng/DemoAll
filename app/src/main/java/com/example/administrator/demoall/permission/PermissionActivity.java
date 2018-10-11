package com.example.administrator.demoall.permission;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

public class PermissionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CONTACT = 0x2000;

    private static final int REQUEST_CODE_PERMISSION_CONTACT = 0x2010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkContactPermission();
                test();

            }
        });
    }


    private void test01(){

    }
    private void test(){
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_CONTACTS)
                .onGranted(permissions -> {
                    openContact();
                })
                .onDenied(permissions -> {
                    Log.e("Test","XXXXXXX");
                })
                .start();
    }

    private void checkContactPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PERMISSION_CONTACT);
        } else {
            openContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openContact();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openContact() {
        Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, REQUEST_CODE_CONTACT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_CONTACT && resultCode == RESULT_OK) {
            if (intent == null) {
                return;
            }
            //处理返回的data,获取选择的联系人信息
            Uri uri = intent.getData();
            String[] contacts = getPhoneContacts(this, uri);
            Log.e("Test",contacts[0]);

            return;

        }
    }


    /**
     * 选择了通讯录联系人
     */
    public static String[] getPhoneContacts(Context context, Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex =
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null,
                    null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(
                        phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

}
