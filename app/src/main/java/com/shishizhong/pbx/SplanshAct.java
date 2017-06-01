package com.shishizhong.pbx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/5/16 0016.
 */
public class SplanshAct extends BaseActivity{

    private long splanshTime = 2000;
    private ImageView mIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_spl);
        overridePendingTransition(R.anim.zoomin, 0);//activity 进入动画
        mIv = (ImageView) findViewById(R.id.iv);
        doJump();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIv = null;
        setContentView(R.layout.view_null);
    }

    private void doJump() {
        final boolean isFristStart = (boolean) SPUtil.getInstant(this).get(Constants.IS_FRIST_START_APP, false);

        mIv.postDelayed(new Runnable() {
            @Override
            public void run() {
                Class activity = null;
                if (!isFristStart) {
                    activity = WelAct.class;
                    SPUtil.getInstant(SplanshAct.this).save(Constants.IS_FRIST_START_APP, true);
                } else {
                        //判断是否填写完毕，填写完毕跳转主页
                        activity = WebAct.class;
                        //如果没有填写完毕，则跳转信息填写的页面
                }
                startActivity(new Intent(SplanshAct.this, activity));
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        }, splanshTime);
    }

}
