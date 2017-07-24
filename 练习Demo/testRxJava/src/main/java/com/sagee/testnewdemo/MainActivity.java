package com.sagee.testnewdemo;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fl1;
    private FrameLayout fl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fl1 = (FrameLayout) findViewById(R.id.fl1);
        fl2 = (FrameLayout) findViewById(R.id.fl2);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        //添加两个fragment
        fragmentTransaction.add(R.id.fl1,new LeftFragment());
        fragmentTransaction.add(R.id.fl2,new RightFragment());
        fragmentTransaction.commit();
    }
}
