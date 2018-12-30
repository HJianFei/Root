package com.hjianfei.root;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hjianfei.service.TrackerService;
import com.hjianfei.utils.BaseApplication;
import com.hjianfei.utils.EventBean;
import com.hjianfei.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_is_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isRoot()) {
                    Toast.makeText(MainActivity.this, "你的手机已获取root权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "你的手机没有root权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.tv_get_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = Utils.upgradeRootPermission(getPackageCodePath());
                if (b) {
                    Toast.makeText(MainActivity.this, "已获取root权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "获取root权限失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.tv_get_assemble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
        findViewById(R.id.tv_start_service).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Settings.canDrawOverlays(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, TrackerService.class);
                    startService(intent);
                    showNormalDialog();
                } else {
                    //若没有权限，提示获取.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    Toast.makeText(MainActivity.this, "需要使用悬浮窗权限，监测游戏封号行为", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.tv_end_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.removeFile(BaseApplication.tempPath, BaseApplication.gamePath, 2);
            }
        });
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("重要提示");
        normalDialog.setMessage("必须保持专用防封在后台运行,游戏结束后返回专用防封软件关闭防封，发送正常数据到服务器以防止游戏追封");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "防封开启成功，请保持专用防封在后台运行", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Toast.makeText(MainActivity.this, "防封开启成功，请保持专用防封在后台运行", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMessage(EventBean eventBean) {
        if (eventBean.eventType == 1) {
            if (eventBean.type == 1) {
                Toast.makeText(this, "专用防封已开启检测游戏封号行为，请保持软件在后台运行", Toast.LENGTH_SHORT).show();
            } else if (eventBean.type == 2) {
                Toast.makeText(this, "专用防封数据发送成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "数据出错，请联系管理员", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
