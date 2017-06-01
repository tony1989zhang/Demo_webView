package com.shishizhong.pbx;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class App extends Application{
    private static  App mApp = null;
    public List<WeakReference<Activity>> aliveActivitys = new ArrayList<WeakReference<Activity>>();
    
    @Override
    public void onCreate() {
    	// TODO Auto-generated method stub
    	super.onCreate();
        mApp = this;
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
    public static App getInstance(){
        if (mApp == null)
        {
            mApp = new App();
        }
        return mApp;
    }
    public void finishAllActivity(){
        for (int i = 0;i < aliveActivitys.size();i++)
        {
            if (aliveActivitys.get(i) != null)
            {
                aliveActivitys.get(i).get().finish();
            }
        }
    }
    public Activity getTopActivity(){
        return aliveActivitys.get(aliveActivitys.size() - 1).get();
    }

}
