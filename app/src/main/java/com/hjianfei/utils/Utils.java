package com.hjianfei.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * <pre>
 *     author : HJFei
 *     e-mail : 190766172@qq.com
 *     time   : 2018/12/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Utils {

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static boolean isRoot() {
        boolean bool = false;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
            Log.d("onResponse", "bool = " + bool);
        } catch (Exception e) {

        }
        return bool;
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static Boolean execCmdsforResult(String[] cmds) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            process.getErrorStream();
            int i = cmds.length;
            for (int j = 0; j < i; j++) {
                String str = cmds[j];
                os.write((str + "\n").getBytes());
            }
            os.write("exit\n".getBytes());
            os.flush();
            os.close();
            process.waitFor();
            process.destroy();
            return true;
        } catch (Exception localException) {
            return false;
        }
    }

    /**
     * 修改文件权限
     *
     * @param fromFilePath
     * @param terminal
     * @param type
     */
    public static void removeFile(final String fromFilePath, final String terminal, final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] strings = new String[2];
                    strings[0] = "chmod -R " + terminal + " " + fromFilePath;
                    strings[1] = "chmod -R " + terminal + " /data/data/com.tencent.ig/lib/libxguardian.so";
                    boolean isSuccess = Utils.execCmdsforResult(strings);
                    if (isSuccess) {
                        EventBus.getDefault().post(new EventBean(1, type));
                    } else {
                        EventBus.getDefault().post(new EventBean(2, type));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d("onResponse", e.toString());
                }

            }
        }).start();
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static final String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint({"HardwareIds", "MissingPermission"})
            String imei = telephonyManager.getDeviceId();
            if (imei == null) {
                imei = "";
            }
            LogUtil.d("onResponse", imei);
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
