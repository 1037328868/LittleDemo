package com.example.rwcontact;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 需求:读取联系人信息,插入联系人信息
 */
public class MainActivity extends AppCompatActivity {

    private ContentResolver resolver;
    private ContactBean contactBean;
    ArrayList<ContactBean> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //获取联系人信息,需要contactread权限
    public void readContact(View view) {
        //获取内容解析者
        resolver = getContentResolver();
        //由于需要读取两个库所以需要两个uri
        Uri raw_contacts = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data = Uri.parse("content://com.android.contacts/data");
        //1,读取raw contacts
        Cursor query1 = resolver.query(raw_contacts, new String[]{"contact_id"}, null, null, null);
        if(query1!=null){
            //准备集合存javabean
            contactList = new ArrayList<>();
            while (query1.moveToNext()){
                // 1.1获取id
                String id = query1.getString(0);
                //id被删除会被置为null
                if (id==null){
                    continue;
                }
                Log.e("id==>",id);
                //如果id不为null则说明是一个新联系人,创建一个新ContactBean准备存入数据
                contactBean = new ContactBean();
                //根据Id获取data表中联系人的信息
                Cursor query2 = resolver.query(data, new String[]{"data1", "mimetype"}, "raw_contact_id = ?", new String[]{id}, null);
                if(query2!=null){
                    while (query2.moveToNext()){
                        String data1 = query2.getString(0);
                        String mimetype = query2.getString(1);
                        //说明data是email
                        if("vnd.android.cursor.item/email_v2".equals(mimetype)){
                            contactBean.email = data1;
                            //说明是电话
                        }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                            contactBean.phone = data1;
                            //说明是姓名
                        }else if("vnd.android.cursor.item/name".equals(mimetype)){
                            contactBean.name = data1;
                            //说明是地址
                        }else if("vnd.android.cursor.item/postal-address_v2".equals(mimetype)){
                            contactBean.address = data1;
                        }
                    }
                }
                //内循环结束说明一个联a系人信息已全部存入javabean,将javabean封装进集合
                contactList.add(contactBean);
                query2.close();
            }
            //外循环也结束说明联系人信息已全部获取成功,遍历集合并打印
           if(contactList.size()>0){
               for (ContactBean cb:contactList){
                   Log.e("联系人信息",cb.toString());
               }
               Toast.makeText(this, "获取联系人信息成功", Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(this, "获取联系人信息失败", Toast.LENGTH_SHORT).show();
           }
            query1.close();
        }


    }

    //备份联系人信息
    public void saveContact(View view) {
        //遍历集合将读取到的信息备份进xml文件
        if(contactList!=null&&contactList.size()>0){
            XMLTool.xmlOut(getApplicationContext(),"contactInfo.xml",contactList);
            Toast.makeText(this, "备份成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "备份失败", Toast.LENGTH_SHORT).show();
        }
    }

    //插入联系人信息,需要contactwrite权限
    public void insertContact(View view) {
        Intent intent = new Intent(getApplicationContext(),insertContactActivity.class);
        startActivity(intent);
    }
}
