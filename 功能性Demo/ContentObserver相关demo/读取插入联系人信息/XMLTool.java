package com.example.rwcontact;

/**
 * Created by hasee on 2017/5/5.
 */

import android.content.Context;
import android.util.Xml;


import com.example.rwcontact.ContactBean;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;


public class XMLTool {
    /**
     * xml解析工具
     * 参数1:上下文
     * 参数2: 文件名
     * 参数3: javaBean集合
     */
    public static void xmlOut(Context context,String fileName, ArrayList<ContactBean> javaBeanList){
        //获取xml序列化器
        XmlSerializer serializer = Xml.newSerializer();
        try {
            //设置保存路径,编码
            serializer.setOutput(context.openFileOutput(fileName,context.MODE_PRIVATE),"utf-8");
            //设置xml开头,根节点
            serializer.startDocument("utf-8",true);
            serializer.startTag(null,"ContactList");
            //遍历集合获取所有元素
            for(ContactBean contactBean:javaBeanList){
                serializer.startTag(null,"CONTACT");

                //姓名
                serializer.startTag(null,"name");
                serializer.text(contactBean.name);
                serializer.endTag(null,"name");
                //电话
                serializer.startTag(null,"phone");
                serializer.text(contactBean.phone);
                serializer.endTag(null,"phone");
                //email
                serializer.startTag(null,"email");
                serializer.text(contactBean.email);
                serializer.endTag(null,"email");
                //地址
                serializer.startTag(null,"address");
                serializer.text(contactBean.address);
                serializer.endTag(null,"address");

                serializer.endTag(null,"CONTACT");
            }
            serializer.endTag(null,"ContactList");
            serializer.endDocument();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
