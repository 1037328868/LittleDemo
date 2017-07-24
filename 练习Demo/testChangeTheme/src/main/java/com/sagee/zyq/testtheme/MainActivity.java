package com.sagee.zyq.testtheme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyApp myApp;
    boolean isClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApp) getApplication();
        myApp.initTheme(this);
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null){
            isClick = savedInstanceState.getBoolean("isClick",false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isClick",!isClick);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            boolean isClick = savedInstanceState.getBoolean("isClick");
            Log.e("onRestore", isClick+"");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    public void changeTheme(View view) {
        if (!isClick){
            myApp.changeTheme(R.style.MyBlueTheme);
            recreate();
        }else {
            myApp.changeTheme(R.style.AppTheme);
            recreate();
        }
    }
}
