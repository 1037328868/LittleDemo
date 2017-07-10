package com.example.rwcontact;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class insertContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_contact);
    }

    //插入新建联系人信息
    public void insert(View view) {
        //获取用户输入的联系人数据
        EditText et_name = (EditText) findViewById(R.id.et_name);
        EditText et_phone = (EditText) findViewById(R.id.et_phone);
        EditText et_address = (EditText) findViewById(R.id.et_address);
        EditText et_email = (EditText) findViewById(R.id.et_email);
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String address = et_address.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        //判断用户是否输入正确
        if("".equals(name)||"".equals(phone)||"".equals(address)||"".equals(email)){
            Toast.makeText(this, "输入内容错误", Toast.LENGTH_SHORT).show();
        }else{
            //获取内容解析者
            ContentResolver resolver = getContentResolver();
            //由于需要读写两个库所以需要两个uri
            Uri raw_contacts = Uri.parse("content://com.android.contacts/raw_contacts");
            Uri data = Uri.parse("content://com.android.contacts/data");
            //先查询raw_contacts表确定联系人id
            Cursor cursor = resolver.query(raw_contacts, new String[]{"contact_id"}, null, null, null);
            //getcount:获取contacts_id列的最大行数,新建的联系人id就是最大行数+1
            String id = cursor.getCount() + 1+"";
            Log.e("id==>",id+"");

            if(!("".equals(id)||"null".equals(id))){
                try {
                    //将id存入raw_contacts表
                    ContentValues values1 = new ContentValues();
                    values1.put("contact_id",id);
                    resolver.insert(raw_contacts, values1);
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }
                //准备存入data表的数据
                ContentValues values2 = new ContentValues();
                //插入4行,由于数据类型相同,map可以复用
                values2.put("data1",name);
                values2.put("mimetype","vnd.android.cursor.item/name");
                values2.put("raw_contact_id",id);
                resolver.insert(data,values2);

                values2.put("data1",phone);
                values2.put("mimetype","vnd.android.cursor.item/phone_v2");
                resolver.insert(data,values2);

                values2.put("data1",address);
                values2.put("mimetype","vnd.android.cursor.item/postal-address_v2");
                resolver.insert(data,values2);

                values2.put("data1",email);
                values2.put("mimetype","vnd.android.cursor.item/email_v2");
                resolver.insert(data,values2);

                Toast.makeText(this, "新建联系人成功", Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        }
    }
}
