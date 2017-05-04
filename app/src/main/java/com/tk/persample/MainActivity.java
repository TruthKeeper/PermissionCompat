package com.tk.persample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.tk.persample.common.PermissionResult;
import com.tk.persample.rx.PermissionManager;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private CheckBox cbx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cbx = (CheckBox) findViewById(R.id.cbx);
    }

    public void btn1(View v) {
        if (cbx.isChecked()) {
            PermissionManager.with(this)
                    .request(Manifest.permission.INTERNET)
                    .subscribe(new Consumer<PermissionResult>() {
                        @Override
                        public void accept(@NonNull PermissionResult permissionResult) throws Exception {
                            process(permissionResult);
                        }
                    });
        } else {
            com.tk.persample.action.PermissionManager.with(this)
                    .request(new com.tk.persample.action.PermissionManager.Callback() {
                        @Override
                        public void call(PermissionResult result) {
                            process(result);
                        }
                    }, Manifest.permission.INTERNET);
        }
    }

    public void btn2(View v) {
        if (cbx.isChecked()) {
            PermissionManager.with(this)
                    .request(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<PermissionResult>() {
                        @Override
                        public void accept(@NonNull PermissionResult permissionResult) throws Exception {
                            process(permissionResult);
                        }
                    });
        } else {
            com.tk.persample.action.PermissionManager.with(this)
                    .request(new com.tk.persample.action.PermissionManager.Callback() {
                        @Override
                        public void call(PermissionResult result) {
                            process(result);
                        }
                    }, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public void btn3(View v) {
        if (cbx.isChecked()) {
            PermissionManager.with(this)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe(new Consumer<PermissionResult>() {
                        @Override
                        public void accept(@NonNull PermissionResult permissionResult) throws Exception {
                            process(permissionResult);
                        }
                    });
        } else {
            com.tk.persample.action.PermissionManager.with(this)
                    .request(new com.tk.persample.action.PermissionManager.Callback() {
                        @Override
                        public void call(PermissionResult result) {
                            process(result);
                        }
                    }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        }
    }

    private void process(PermissionResult permissionResult) {
        if (permissionResult.isSuccessful()) {
            Toast.makeText(getApplicationContext(), "成功GET√", Toast.LENGTH_SHORT).show();
            return;
        }
        if (permissionResult.isDisable()) {
            Toast.makeText(getApplicationContext(), "权限被禁用啦，请手动开启权限", Toast.LENGTH_SHORT).show();
            PermissionManager.toSetting(MainActivity.this);
        } else {
            Toast.makeText(getApplicationContext(), "宝宝摔倒了，要开启权限才能爬起来~~~", Toast.LENGTH_SHORT).show();
        }

    }
}
