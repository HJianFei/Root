package com.hjianfei.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;


    public static void showShortToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.mContext, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.BOTTOM, 0, dip2px(BaseApplication.mContext, 64));
        toast.show();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}