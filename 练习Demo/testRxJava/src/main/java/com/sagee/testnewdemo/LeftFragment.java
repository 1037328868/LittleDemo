package com.sagee.testnewdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by hasee on 2017/7/4.
 */

public class LeftFragment extends Fragment implements View.OnClickListener {

    private Button bt1;
    private Observer<String> observer;
    private Observable<String> observable;
    private LinearLayout ll1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_left_fragment, container, false);
        bt1 = (Button) view.findViewById(R.id.bt1);
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        bt1.setOnClickListener(this);

        initObserver();
        initObservable();

        return view;
    }

    /**
     * 设置观察(订阅)者
     */
    private void initObserver() {

        //observer观察者,决定事件触发时做怎样的事情
        //当被注册时调用
        //当发送事件后被调用
        //当事件处理异常时调用
        //当事件队列完成时调用,即不再有新变动
        observer = new Observer<String>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                //当被注册时调用
                Log.e("onSubscribe: ", "注册");
            }

            @Override
            public void onNext(@NonNull String s) {
                //当发送事件后被调用
                Log.e("onSubscribe: ", "事件被触发");
                TextView textView = new TextView(getContext());
                textView.setText(s);
                textView.setTextColor(Color.BLACK);
                ll1.addView(textView);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //当事件处理异常时调用
                Log.e("onSubscribe: ", "事件异常");
            }

            @Override
            public void onComplete() {
                //当事件队列完成时调用,即不再有新变动
                Log.e("onSubscribe: ", "事件完成");
            }
        };
    }

    /**
     * 设置被观察(订阅)者
     */
    private void initObservable() {

        //observable被观察者,决定什么时候触发什么事件
        //此方法用来决定触发事件的规则
        observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                //此方法用来决定触发事件的规则
                e.onNext("rxjava");
                e.onNext("初");
                e.onNext("级");
                e.onNext("用");
                e.onNext("法");
                e.onComplete();
//                e.onError(new Error("异常1"));
//                e.onError(new Error("异常2"));
            }
        });

        //observable的另外创建方法,just(依次传入参数,依次执行)
//        observable = Observable.just("哈", "喽", "卧", "的");
        //from,传入数组或迭代器拆分成具体对象依次执行

    }

    @Override
    public void onClick(View v) {
        //左边的按钮
        observable.subscribe(observer);
    }
}
