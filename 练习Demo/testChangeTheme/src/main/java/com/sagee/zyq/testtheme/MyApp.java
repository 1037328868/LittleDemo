package com.sagee.zyq.testtheme;

import android.app.Application;
import android.content.Context;

/**
 * Created by hasee on 2017/7/20.
 */

public class MyApp extends Application {
    private int currentTheme = R.style.AppTheme;
    public void changeTheme(int theme){
        this.currentTheme = theme;
    }
    public void initTheme(Context context){
        context.setTheme(currentTheme);
    }

}
