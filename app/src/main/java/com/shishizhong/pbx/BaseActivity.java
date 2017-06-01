package com.shishizhong.pbx;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class BaseActivity extends Activity{

    public WeakReference<Activity> WriActivity = new WeakReference<Activity>(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().aliveActivitys.add(WriActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        App.getInstance().aliveActivitys.remove(WriActivity);
    }



}
