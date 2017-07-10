package com.example.smssave;

/**
 * Created by hasee on 2017/5/5.
 */

public class SmsBean {
    public SmsBean(String address, String date, String body) {
        this.address = address;
        this.date = date;
        this.body = body;
    }

    //发送人
    public String address;
    //发送时间
    public String date;
    //发送内容
    public String body;
}
