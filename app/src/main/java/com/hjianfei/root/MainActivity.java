package com.hjianfei.root;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
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
                normalDialog.setMessage("第一步：清除游戏全部数据;\n" +
                        "第二步：正常打开游戏，等待下载3M的文件，下载完成关闭游戏;\n" +
                        "第三步：点击无限游客登录按钮;\n" +
                        "第四步：正常打开游戏;");
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
                                            Utils.cpFile("/data/data/com.tencent.ig/app_Bugtrace/bugtrace_info.txt", FileUtils.getDiskCacheDir(MainActivity.this) + "/bugtrace_info.txt", 4);
                                            Thread.sleep(500);
                                            FileUtils.modifyFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/bugtrace_info.txt", FileUtils.getString(FileUtils.getDiskCacheDir(MainActivity.this) + "/", "bugtrace_info.txt"), false);
                                            Thread.sleep(500);
                                            Utils.cpFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/bugtrace_info.txt", "/data/data/com.tencent.ig/app_Bugtrace/bugtrace_info.txt", 4);
                                            Thread.sleep(500);
                                            Utils.cpFile("/data/data/com.tencent.ig/shared_prefs/device_id.xml", FileUtils.getDiskCacheDir(MainActivity.this) + "/device_id.xml", 4);
                                            Thread.sleep(500);
                                            FileUtils.modifyFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/device_id.xml", FileUtils.getString(FileUtils.getDiskCacheDir(MainActivity.this) + "/", "device_id.xml"), false);
                                            Thread.sleep(500);
                                            Utils.cpFile(FileUtils.getDiskCacheDir(MainActivity.this) + "/device_id.xml", "/data/data/com.tencent.ig/shared_prefs/device_id.xml", 3);
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
                            Intent intent = new Intent(MainActivity.this, TrackerService.class);
                            intent.putExtra("type", 2);
                            startService(intent);
                        }
                        ToastUtil.showShortToast("请保持专用防封在后台运行");
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
}
