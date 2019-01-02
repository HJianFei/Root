package com.hjianfei.root;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.hjianfei.service.GTrackerService;
import com.hjianfei.service.ITrackerService;
import com.hjianfei.utils.BaseApplication;
import com.hjianfei.utils.EventBean;
import com.hjianfei.utils.ToastUtil;
import com.hjianfei.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private LoadingDailog dialog;

    private static String IMEI = "355457087264381";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        initDta();
        findViewById(R.id.tv_is_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.getIMEI(MainActivity.this).equals(IMEI)) {
                    if (Utils.isRoot()) {
                        ToastUtil.showShortToast("你的手机已获取root权限");
                    } else {
                        ToastUtil.showShortToast("你的手机没有root权限");
                    }
                } else {
                    ToastUtil.showShortToast("你没有授权使用该软件，请与管理员联系");
                }
            }
        });
        findViewById(R.id.tv_get_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.getIMEI(MainActivity.this).equals(IMEI)) {
                    boolean b = Utils.upgradeRootPermission(getPackageCodePath());
                    if (b) {
                        ToastUtil.showShortToast("已获取root权限");
                    } else {
                        ToastUtil.showShortToast("获取root权限失败");
                    }
                } else {
                    ToastUtil.showShortToast("你没有授权使用该软件，请与管理员联系");
                }
            }
        });
        findViewById(R.id.tv_get_assemble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.getIMEI(MainActivity.this).equals(IMEI)) {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                } else {
                    ToastUtil.showShortToast("你没有授权使用该软件，请与管理员联系");
                }
            }
        });
        findViewById(R.id.tv_start_china_service).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Utils.getIMEI(MainActivity.this).equals(IMEI)) {
                    if (Settings.canDrawOverlays(MainActivity.this)) {
                        showNormalDialog(1);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        ToastUtil.showShortToast("需要使用悬浮窗权限，监测游戏封号行为");
                        startActivity(intent);
                    }
                } else {
                    ToastUtil.showShortToast("你没有授权使用该软件，请与管理员联系");
                }
            }
        });
        findViewById(R.id.tv_start_internet_service).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Utils.getIMEI(MainActivity.this).equals(IMEI)) {
                    if (Settings.canDrawOverlays(MainActivity.this)) {
                        showNormalDialog(2);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        ToastUtil.showShortToast("需要使用悬浮窗权限，监测游戏封号行为");
                        startActivity(intent);
                    }
                } else {
                    ToastUtil.showShortToast("你没有授权使用该软件，请与管理员联系");
                }
            }
        });
        findViewById(R.id.tv_end_china_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.getIMEI(MainActivity.this).equals(IMEI)) {
                    Utils.removeChinaFile(BaseApplication.ChinaGamePath, "664", 2);
                } else {
                    ToastUtil.showShortToast("你没有授权使用该软件，请与管理员联系");
                }
            }
        });
        findViewById(R.id.tv_end_internet_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.getIMEI(MainActivity.this).equals(IMEI)) {
                    Utils.removeChinaFile(BaseApplication.ChinaGamePath, "664", 2);
                } else {
                    ToastUtil.showShortToast("你没有授权使用该软件，请与管理员联系");
                }
            }
        });
    }

    private void initDta() {


        //所要申请的权限
        String[] perms = {Manifest.permission.READ_PHONE_STATE};

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            showLoadingDialog();

        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perms);
        }

    }

    private void showLoadingDialog() {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("数据校验中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.show();
        if (Utils.getIMEI(this).equals(IMEI)) {

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    ToastUtil.showShortToast("数据校验成功");
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    ToastUtil.showShortToast("数据校验失败");
                }
            }, 2000);
        }
    }

    private void showNormalDialog(final int type) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("重要提示");
        normalDialog.setMessage("必须保持专用防封在后台运行,游戏结束后返回专用防封软件关闭防封，发送正常数据到服务器以防止游戏追封");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 1) {
                            Intent intent = new Intent(MainActivity.this, GTrackerService.class);
                            startService(intent);
                        } else if (type == 2) {
                            Intent intent = new Intent(MainActivity.this, ITrackerService.class);
                            startService(intent);
                        }
                        ToastUtil.showShortToast("防封开启成功，请保持专用防封在后台运行");
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
                Toast.makeText(this, "专用防封已开启检测游戏封号行为，请保持软件在后台运行", Toast.LENGTH_SHORT).show();
            } else if (eventBean.type == 2) {
                ToastUtil.showShortToast("专用防封数据发送成功");
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
        showLoadingDialog();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        ToastUtil.showShortToast("专用拦截没有获取必要权限");
    }
}
