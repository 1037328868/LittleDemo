package com.sagee.zyq.timetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // 时间格式
    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;

    private TextView time;
    private Button btStart;
    private Button btStop;
    private Timer timer;

    //假数据,真实情况是请求网络从服务器拿
    private static long timeCount = 1 * DAY;

    private long tempDay;
    private long tempHour;
    private long tempMinute;
    private long tempSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView) findViewById(R.id.time);
        btStart = (Button) findViewById(R.id.bt_start);
        btStop = (Button) findViewById(R.id.bt_stop);

        btStart.setOnClickListener(this);
        btStop.setOnClickListener(this);
    }

    /**
     * 计算时间设置控件
     *
     * @param surplusTime 传入剩余时间(毫秒)
     */
    private void computeTime(long surplusTime) {

        // 剩余时间
        long tempTime = surplusTime;

        tempDay = 0;
        tempHour = 0;
        tempMinute = 0;
        tempSecond = 0;

        if (surplusTime - DAY > 0 && tempTime > 0) {// 有剩余天数
            tempDay = surplusTime / DAY;// 剩余天数
            tempTime = surplusTime % DAY;// 减去天数之后剩余的时间
        }
        if (surplusTime - HOUR > 0 && tempTime > 0) {// 有剩余小时
            tempHour = tempTime / HOUR;// 剩余小时
            tempTime = tempTime % HOUR;// 减去小时之后的剩余时间
        }
        if (surplusTime - MINUTE > 0 && tempTime > 0) {// 有剩余分钟
            tempMinute = tempTime / MINUTE;// 剩余分钟
            tempTime = tempTime % MINUTE;// 减去分钟之后的剩余时间
        }
        if (surplusTime - SECOND > 0 && tempTime > 0) {// 有剩余分钟
            tempSecond = tempTime / SECOND;// 剩余秒
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                time.setText("还剩" + tempDay + "天" + tempHour + "小时" + tempMinute + "分钟" + tempSecond + "秒");
            }
        });
    }

    /**
     * 开启时间任务
     */
    private void timeTask() {
        timer = new Timer();
        // 每次执行任务延迟1秒
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                timeCount -= 1 * SECOND;
                if (timeCount < 0) return;

                computeTime(timeCount);

            }
        }, 1000, 1000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                if (timer == null)
                    timeTask();
                break;
            case R.id.bt_stop:
                if (timer != null)
                    timer.cancel();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }
}
