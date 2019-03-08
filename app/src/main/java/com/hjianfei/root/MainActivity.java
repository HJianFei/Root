package com.hjianfei.root;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.hjianfei.service.TrackerService;
import com.hjianfei.utils.BaseApplication;
import com.hjianfei.utils.EventBean;
import com.hjianfei.utils.FileUtils;
import com.hjianfei.utils.ToastUtil;
import com.hjianfei.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private LoadingDailog dialog;
    ConstraintLayout toucherLayout;
    WindowManager.LayoutParams params;
    WindowManager windowManager;

    ImageButton imageButton1;

    int statusBarHeight = -1;
    private boolean isIFirstSetting = false;
    private int type;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_text);
        textView.setText("软件完全免费，如果你用钱买了,说明你被骗了.\n" +
                "欢迎加入gg大佬茶餐厅1，群聊：550425199.\n" +
                "欢迎加入gg大佬茶餐厅2，群聊：347489706.\n" +
                "反馈QQ:3029359596");
        initDta();
        findViewById(R.id.tv_is_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isRoot()) {
                    ToastUtil.showShortToast("你的手机已获取root权限");
                } else {
                    ToastUtil.showShortToast("你的手机没有root权限");
                }
            }
        });
        findViewById(R.id.tv_get_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = Utils.upgradeRootPermission(getPackageCodePath());
                if (b) {
                    ToastUtil.showShortToast("已获取root权限");
                } else {
                    ToastUtil.showShortToast("获取root权限失败");
                }
            }
        });
        findViewById(R.id.tv_get_assemble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });

        findViewById(R.id.tv_start_internet_service).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Settings.canDrawOverlays(MainActivity.this)) {
                    showNormalDialog(2);
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    ToastUtil.showShortToast("需要使用悬浮窗权限");
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.tv_start_internet_service_1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Utils.removeFile(BaseApplication.InternetGamePath, "200", 1);
            }
        });
        findViewById(R.id.tv_end_internet_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.removeFile(BaseApplication.InternetGamePath, "664", 2);
            }
        });
        findViewById(R.id.tv_all_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setIcon(R.mipmap.ic_launcher);
                normalDialog.setTitle("重要提示");
                normalDialog.setMessage("第一步：清除游戏缓存数据;\n" +
                        "第二步：点击无限游客登录按钮;\n" +
                        "第三步：正常打开游戏;");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogd, int which) {
                                dialogd.dismiss();
                                LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(MainActivity.this)
                                        .setMessage("处理中...")
                                        .setCancelable(false)
                                        .setCancelOutside(false);
                                dialog = loadBuilder.create();
                                dialog.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
//                                            Utils.cpFile("/data/data/com.vng.pubgmobile/app_Bugtrace/bugtrace_info.txt", FileUtils.getDiskCacheDir(MainActivity.this) + "/bugtrace_info.txt", 4);
//                                            Thread.sleep(500);
//                                            FileUtils.modifyFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/bugtrace_info.txt", FileUtils.getString(FileUtils.getDiskCacheDir(MainActivity.this) + "/", "bugtrace_info.txt"), false);
//                                            Thread.sleep(500);
//                                            Utils.cpFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/bugtrace_info.txt", "/data/data/com.tencent.ig/app_Bugtrace/bugtrace_info.txt", 4);
//                                            Thread.sleep(500);
                                            Utils.cpFile("/data/data/com.vng.pubgmobile/shared_prefs/device_id.xml", FileUtils.getDiskCacheDir(MainActivity.this) + "/device_id.xml", 4);
                                            Thread.sleep(500);
                                            FileUtils.modifyFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/device_id.xml", FileUtils.getString(FileUtils.getDiskCacheDir(MainActivity.this) + "/", "device_id.xml"), false);
                                            Thread.sleep(500);
                                            Utils.cpFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/device_id.xml", "/data/data/com.vng.pubgmobile/shared_prefs/device_id.xml", 3);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        });
                normalDialog.setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogd, int which) {
                                dialogd.dismiss();
                            }
                        });
                normalDialog.show();
            }
        });
    }

    private void initDta() {


        //所要申请的权限
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限

        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perms);
        }

    }

    private void showNormalDialog(final int type) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("重要提示");
        normalDialog.setMessage("出现第一个界面的时候点击悬浮窗（切记），出现防封开启成功提示说明成功，如果没有提示重新尝试,游戏结束后返回专用防封软件结束游戏");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 1) {
                            Intent intent = new Intent(MainActivity.this, TrackerService.class);
                            intent.putExtra("type", 1);
                            startService(intent);
                        } else if (type == 2) {
//                            Intent intent = new Intent(MainActivity.this, TrackerService.class);
//                            intent.putExtra("type", 2);
//                            startService(intent);
                            createToucher();
                        }
//                        ToastUtil.showShortToast("请保持专用防封在后台运行");
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showShortToast("防封没有开启");
                        dialog.dismiss();
                    }
                });
        normalDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMessage(EventBean eventBean) {
        if (eventBean.eventType == 1) {
            if (eventBean.type == 1) {
                Toast.makeText(this, "专用防封开启成功", Toast.LENGTH_SHORT).show();
            } else if (eventBean.type == 2) {
                ToastUtil.showShortToast("游戏结束");
            } else if (eventBean.type == 3) {
                ToastUtil.showShortToast("游客账号已刷新");
                dialog.dismiss();
            }
        } else {
            ToastUtil.showShortToast("数据出错，请联系管理员");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        ToastUtil.showShortToast("专用拦截没有获取必要权限");
    }

    private void createToucher() {
        if (imageButton1 != null) {
            return;
        } else {
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

            //浮动窗口按钮.
            imageButton1 = (ImageButton) toucherLayout.findViewById(R.id.imageButton1);
            imageButton1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    params.x = (int) event.getRawX() - 150;
                    params.y = (int) event.getRawY() - 150 - statusBarHeight;
                    windowManager.updateViewLayout(toucherLayout, params);
                    return false;
                }
            });
            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isIFirstSetting) {
                        isIFirstSetting = !isIFirstSetting;
                        Utils.removeFile(BaseApplication.InternetGamePath, "200", 1);
                    }
                }
            });
        }
    }

    /**
     * dp转换成px
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onDestroy() {
        if (imageButton1 != null) {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }
}
