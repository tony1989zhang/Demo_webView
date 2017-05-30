package com.lottotrend.demo_webview;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class CusDlg extends Dialog implements View.OnClickListener {
    OnOperationListener operationListener;

    public CusDlg(Context context) {

        super(context, R.style.AlertDialogStyle);
        setContentView(R.layout.cus_dlg);
        findViewById(R.id.dialogLeftBtn).setOnClickListener(this);
        findViewById(R.id.dialogRightBtn).setOnClickListener(this);
    }

    public CusDlg(Context context, View.OnClickListener listener) {
        super(context, R.style.AlertDialogStyle);
        setContentView(R.layout.cus_dlg);
        findViewById(R.id.dialogLeftBtn).setOnClickListener(this);
        findViewById(R.id.dialogRightBtn).setOnClickListener(listener);
    }

    public void setOperationListener(OnOperationListener operationListener) {
        this.operationListener = operationListener;
    }

    @Override
    public void setTitle(CharSequence title) {

        ((TextView) findViewById(R.id.dialogTitle)).setText(title);
    }

    public void setTag(Object tag) {

        findViewById(R.id.dialogTitle).setTag(tag);
    }

    public Object getTag() {

        return findViewById(R.id.dialogTitle).getTag();
    }

    public void setMessage(CharSequence message) {
        ((TextView) findViewById(R.id.dialogText)).setText(message);
    }

    public void setButtonsText(CharSequence left, CharSequence right) {

        findViewById(R.id.dialogRightBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.dialogLeftBtn)).setText(left);
        ((TextView) findViewById(R.id.dialogRightBtn)).setText(right);
    }

    public void setButtonText(CharSequence text) {
        findViewById(R.id.btnLayout).setVisibility(View.GONE);
        findViewById(R.id.singleBtnLayout).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.singBtn)).setText(text);
        findViewById(R.id.singBtn).setOnClickListener(this);
    }

    public void hideLeftButton(){
        findViewById(R.id.dialogRightBtn).setVisibility(View.GONE);
        findViewById(R.id.left_right_divider).setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogLeftBtn:
                if (operationListener != null)
                    operationListener.onLeftClick();
                else
                    this.cancel();
                break;

            case R.id.dialogRightBtn:
                if (operationListener != null)
                    operationListener.onRightClick();

                break;
            case R.id.singBtn:
                this.cancel();
                break;

        }
    }
}