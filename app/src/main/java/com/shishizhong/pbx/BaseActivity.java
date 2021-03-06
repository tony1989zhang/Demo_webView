package com.shishizhong.pbx;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class BaseActivity extends Activity{
    protected final static String LAST_ACTIVITY_NAME = "LAST_ACTIVITY_NAME";
    private CProDlg mProgress;
    private View toastView;
    private Toast toast;
    public WeakReference<Activity> WriActivity = new WeakReference<Activity>(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().aliveActivitys.add(WriActivity);
        mProgress = CProDlg.createDialog(this);
        toastView = LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        toast = Toast.makeText(this,null,Toast.LENGTH_LONG);
        toast.setView(toastView);
    }

    public void showProgressDialog() {
        showProgressDialog("���Ժ�");
    }

    public void showProgressDialog(String strMessage) {
        showProgressDialog(strMessage, false);
    }

    public void showProgressDialog(String strMessage, boolean isCancelAble) {
        if (null == mProgress) {
            return;
        }
        if (isCancelAble) {
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgress.dismiss();
                }
            });
        }
        mProgress.setMessage(strMessage);
        mProgress.show();
    }
    public void dismissProgressDialog() {
        if (null != mProgress && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    public void showToast(String hint) {
        showToast(hint, Toast.LENGTH_LONG);
    }

    public void showToast(String hint, int duration) {
        if (null == toast || null == toastView)
            return;
        TextView tv = (TextView) toastView.findViewById(R.id.toastMessage);
        tv.setText(hint);
        toast.setDuration(duration);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        mProgress = null;
        App.getInstance().aliveActivitys.remove(WriActivity);
    }



}
