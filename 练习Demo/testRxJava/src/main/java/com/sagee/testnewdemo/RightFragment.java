package com.sagee.testnewdemo;

import android.graphics.Color;
import android.icu.util.TimeUnit;
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

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hasee on 2017/7/4.
 */

public class RightFragment extends Fragment implements View.OnClickListener {

    private Button bt2;
    private LinearLayout ll2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_right_fragment, container, false);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);
        bt2 = (Button) view.findViewById(R.id.bt2);
        bt2.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View v) {
        //右边的按钮
//        initRxjava();
//        initRxjava1();
//        initRxjava2();
        initRxjava3();
    }

    /**
     * 事件组合. zip操作符
     */
    private void initRxjava3() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.e("observable1", "1");
                Thread.sleep(1000);

                e.onNext(2);
                Log.e("observable1", "2");
                Thread.sleep(1000);

                e.onNext(3);
                Log.e("observable1", "3");
                Thread.sleep(1000);

                e.onNext(4);
                Log.e("observable1", "4");
                Thread.sleep(1000);

                e.onNext(5);
                Log.e("observable1", "5");
                Thread.sleep(1000);

            }
        }).subscribeOn(Schedulers.newThread());
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                Log.e("observable2", "A");
                Thread.sleep(1000);

                e.onNext("B");
                Log.e("observable2", "B");
//                Thread.sleep(1000);

                e.onNext("C");
                Log.e("observable2", "C");
//                Thread.sleep(1000);

            }
        }).subscribeOn(Schedulers.io());

        /**
         * zip:整合两个事件源发送的事件,返回整合后的结果
         * 参数3的泛型,前两个事件传来的数据类型,最后一个参数,整合后的数据类型
         */
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {

            @Override
            public String apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                return integer+s;
            }

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("onSubscribe: ", Thread.currentThread().getName());

            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e("onNext: ", s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("onError: ", "onError");
            }

            @Override
            public void onComplete() {
                Log.e("onComplete: ", "onComplete");
            }
        });
    }

    /**
     * 事件转化. flatMap,concatMap操作符
     */
    private void initRxjava2() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {

            // FlatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，
            // 然后将它们发射的事件合并后放进一个单独的Observable里.
            // 这里需要注意的是, flatMap并不保证事件的顺序
            // 如果需要严格按照上游发送的顺序来触发,要使用concatMap
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {

                ArrayList<String> stringArrayList = new ArrayList<>();
                for (int i = 1; i < 4; i++) {
                    stringArrayList.add("我是第"+integer+"次事件的第"+i+"条数据");
                }
                return Observable.fromIterable(stringArrayList);
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("事件转化:", s);
            }
        });
    }

    /**
     * 线程操作
     */
    private void initRxjava1() {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("123");
                e.onNext("456");
//                e.onError(new Error("异常1"));
//                e.onError(new Error("异常2"));
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("currentThreadName1: ==="+s+"===", Thread.currentThread().getName());
            }
        }).observeOn(Schedulers.io()).doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("currentThreadName2: ==="+s+"===", Thread.currentThread().getName());
            }
        }).observeOn(Schedulers.newThread()).doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("currentThreadName3: ==="+s+"===", Thread.currentThread().getName());
            }
        }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("currentThreadName4: ==="+s+"===", Thread.currentThread().getName());
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("currentThreadName5: ==="+s+"===", Thread.currentThread().getName());
            }
        });

    }

    /**
     * 基础链式写法
     */
    private void initRxjava() {

        Observable.just("rxjava", "进", "阶", "用", "法")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        //subscribe的此参数重写方法表示只关注onNext事件
                        Log.e("onNext: ", s);
                        TextView textView = new TextView(getContext());
                        textView.setText(s);
                        textView.setTextColor(Color.BLACK);
                        ll2.addView(textView);
                    }
                });

        Observable.create(new ObservableOnSubscribe<String>() {

            /**
             * @param e ObservableEmitter:事件的发射器,可以发送onSubscribe,onNext,onError,onComplete事件
             *
             *          分发事件规则:
             *          上游可以发送无限个onNext, 下游也可以接收无限个onNext.
             *          当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
             *          当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
             *          上游可以不发送onComplete或onError.
             *          最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
             * @throws Exception
             */
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("rxjava");
                e.onNext("进");
                e.onNext("阶");
                e.onNext("用");
                e.onNext("法");
                e.onComplete();
            }

        }).subscribe(new Observer<String>() {
            /**
             * @param d d参数可以用来操作打断
             */
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("onSubscribe: ", "订阅事件");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e("onNext: ", "触发事件");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // onError事件和onComplete事件的区别:
                // onError后如果再调用onError会崩溃,onComplete则不一定会崩溃
                // onComplete后再跟onError也会崩溃,onError后跟onComplete则不一定
                Log.e("onError: ", "触发错误事件");
            }

            @Override
            public void onComplete() {
                Log.e("onError: ", "触发完成事件");
            }
        });

    }
}
