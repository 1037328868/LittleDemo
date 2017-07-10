package com.example.smssave;

/**
 * Created by hasee on 2017/5/5.
 */

import android.content.Context;
import android.util.Xml;


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
    public static void xmlOut(Context context,String fileName, ArrayList<SmsBean> javaBeanList){
        //获取xml序列化器
        XmlSerializer serializer = Xml.newSerializer();
        try {
            //设置保存路径,编码
            serializer.setOutput(context.openFileOutput(fileName,context.MODE_PRIVATE),"utf-8");
            //设置xml开头,根节点
            serializer.startDocument("utf-8",true);
            serializer.startTag(null,"SMSList");
            //遍历集合获取所有元素
            for(SmsBean sms:javaBeanList){
                serializer.startTag(null,"SMS");

                //发件人
                serializer.startTag(null,"address");
                serializer.text(sms.address);
                serializer.endTag(null,"address");
                //收件时间
                serializer.startTag(null,"date");
                serializer.text(sms.date);
                serializer.endTag(null,"date");
                //短信内容
                serializer.startTag(null,"body");
                serializer.text(sms.body);
                serializer.endTag(null,"body");

                serializer.endTag(null,"SMS");
            }
            serializer.endTag(null,"SMSList");
            serializer.endDocument();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
