package com.hjianfei.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageButton;

import com.hjianfei.root.R;
import com.hjianfei.utils.BaseApplication;
import com.hjianfei.utils.Utils;

public class TrackerService extends AccessibilityService {

    private static final String TAG = "TrackerService";

    ConstraintLayout toucherLayout;
    WindowManager.LayoutParams params;
    WindowManager windowManager;

    ImageButton imageButton1;

    //状态栏高度.
    int statusBarHeight = -1;
    private boolean isFirstSetting = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName().toString().equals("com.tencent.tmgp.pubgmhd") && event.getClassName().toString().equals("com.epicgames.ue4.GameActivity")) {
                if (!isFirstSetting) {
                    Utils.removeFile(BaseApplication.gamePath, "220", 1);
                    isFirstSetting = true;
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        createToucher();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createToucher() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        params.width = dp2px(this, 45.0f);
        params.height = dp2px(this, 45.0f);

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (ConstraintLayout) inflater.inflate(R.layout.toucherlayout, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout, params);

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        imageButton1 = (ImageButton) toucherLayout.findViewById(R.id.imageButton1);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            long[] hints = new long[2];

            @Override
            public void onClick(View v) {
//                Log.i(TAG, "点击了");
//                System.arraycopy(hints, 1, hints, 0, hints.length - 1);
//                hints[hints.length - 1] = SystemClock.uptimeMillis();
//                if (SystemClock.uptimeMillis() - hints[0] >= 700) {
//                    Log.i(TAG, "要执行");
//                    Toast.makeText(TrackerService.this, "连续点击两次以退出", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.i(TAG, "即将关闭");
//                    stopSelf();
//                }
            }
        });

        imageButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                params.x = (int) event.getRawX() - 150;
                params.y = (int) event.getRawY() - 150 - statusBarHeight;
                windowManager.updateViewLayout(toucherLayout, params);
                return false;
            }
        });
    }

    /**
     * dp转换成px
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    private int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public void onDestroy() {
        if (imageButton1 != null) {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }
}
