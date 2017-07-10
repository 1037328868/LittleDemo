package com.example.rwcontact;

/**
 * Created by hasee on 2017/5/5.
 */
public class ContactBean {

    public String name;
    public String phone;
    public String email;
    public String address;

    @Override
    public String toString() {
        return "ContactBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
