package com.hjianfei.root;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hjianfei.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
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

            private TextView lvFile;

            @Override
            public void onClick(View view) {

                lvFile = (TextView) findViewById(R.id.listFile);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] strings = new String[1];
                        strings[0] = "cp /storage/emulated/0/国际服/libUE4.so /data/data/com.tencent.ig/lib/libUE4.so";
                        final Boolean content = Utils.execCmdsforResult(strings);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lvFile.setText(content + "==");
                            }
                        });

                    }
                }).start();
            }
        });
    }


}
