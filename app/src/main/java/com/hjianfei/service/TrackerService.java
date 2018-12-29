package com.hjianfei.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.hjianfei.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * <pre>
 *     author : HJFei
 *     e-mail : 190766172@qq.com
 *     time   : 2018/12/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TrackerService extends AccessibilityService {

    public static final int BACK = 1;
    public static final int HOME = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName().toString().equals("bin.mt.plus")) {
                Utils.moveFileToSystem("/storage/emulated/0/国际服/temp/libUE4.so", "/data/data/libUE4.so");
            }


        }

    }

    @Override
    public void onInterrupt() {

    }

    @Subscribe
    public void onReceive(Integer action) {
        switch (action) {
            case BACK:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                break;
            case HOME:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                break;
        }
    }
}
