package com.zsg.eventbus;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册事件总线 让当前对象成为观察者
        EventBus.getDefault().register(this);

        initView();
    }

    private void initView() {
        view = (TextView) findViewById(R.id.TextView);

    }

    public void doPost(View v) {
        //网络操作
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        //发布事件  类型字符串
           /*             OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(3, TimeUnit.MINUTES)
                                .build();
                        final Request request = new Request.Builder()
                                .url("http://www.baidu.com")
                                .build();

                        client.newCall(request).enqueue(
                                new Callback() {
                                    @Override
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String s = request.body().toString();
                                        EventBus.getDefault().post(s);
                                    }
                                }
                        );*/


                        Log.d(TAG, "发布    " + Thread.currentThread().getName());
                        EventBus.getDefault().post("info");
                    }
                }
        ).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销
        EventBus.getDefault().unregister(this);
    }

    /**
     * 观察对象数据改变  触发
     * 不加 threadMode 默认在哪发的事件  就从哪个线程执行 ThreadMode.POSTING
     * ThreadMode.MAIN：在创建方法中的线程中执行
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showInfo(String s) {
        Log.d(TAG, "MAIN   " + Thread.currentThread().getName() + "    " + s);
    }

    /**
     * ThreadMode.POSTING：在发送线程中执行
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void showInfo2(String s) {
        Log.d(TAG, "POSTING   " + Thread.currentThread().getName() + "    " + s);
    }

    /**
     * ThreadMode.BACKGROUND：后台执行
     * 若是主线程发布事件，会启动子线程执行该方法
     * 若在子线程发布事件 就在当前子线程执行该方法
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void showInfo3(String s) {
        Log.d(TAG, "BACKGROUND   " + Thread.currentThread().getName() + "    " + s);
    }

    /**
     * ThreadMode.ASYNC：在新线程中执行  不管在哪创建和发布  都会新启动一个执行
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void showInfo4(String s) {
        Log.d(TAG, "ASYNC   " + Thread.currentThread().getName() + "    " + s);
    }


    /**
     * 根据参数来决定事件的类型   post（类型）
     *
     * @param t
     */
    @Subscribe
    public void showHello(int t) {

    }
}
