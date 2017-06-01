package com.shishizhong.pbx;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;


public class CProDlg extends Dialog {
    private Context mContext = null;
    private static CProDlg mCustomProgressDialog = null;
    public CProDlg(Context context) {
        super(context);
        this.mContext = context;
    }
    public CProDlg(Context context, int theme) {
        super(context, theme);
    }
    public static CProDlg createDialog(Context context) {
        mCustomProgressDialog = new CProDlg(context,
                R.style.CustomProgressDialog);
        mCustomProgressDialog.setContentView(R.layout.progressdialog_style);
        mCustomProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return mCustomProgressDialog;
    }
    public void onWindowFocusChanged(boolean hasFocus) {

        if (mCustomProgressDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) mCustomProgressDialog
                .findViewById(R.id.image_loading_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();
    }
    public CProDlg setTitile(String strTitle) {
        return mCustomProgressDialog;
    }
    public CProDlg setMessage(String strMessage) {
        TextView tvMsg = (TextView) mCustomProgressDialog
                .findViewById(R.id.text_loading_msg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return mCustomProgressDialog;
    }
}
