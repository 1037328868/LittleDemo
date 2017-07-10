package com.example.smssave;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //准备一个集合保存javabean
    private ArrayList<SmsBean> smss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void save(View view) {
        //获取一个内容解析者,只需读取权限
        ContentResolver resolver = getContentResolver();
        //读取系统数据库中短信数据
        Uri uri = Uri.parse("content://sms");
        //参数1:uri地址 参数2:想要查询的列名 返回值:cursor游标用来解析数据
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "body"}, null, null, null);
        if(cursor!=null){
            //创建集合保存javabean
            smss = new ArrayList<>();
            while(cursor.moveToNext()){
                //将读取出的数据存入javabean并封装至集合,方便使用
                String address = cursor.getString(0);
                String date = cursor.getString(1);
                String body = cursor.getString(2);
                Log.e("获取数据:","发送人:"+address+"发送时间:"+date+"发送内容:"+body);
                smss.add(new SmsBean(address,date,body));
            }
            //将数据写入至xml文件
            XMLTool.xmlOut(getApplicationContext(),"sms.xml",smss);

            Toast.makeText(this, "备份成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "获取短信失败", Toast.LENGTH_SHORT).show();
        }
    }
}
