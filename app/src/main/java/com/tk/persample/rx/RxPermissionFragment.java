package com.tk.persample.rx;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.tk.persample.common.Permission;
import com.tk.persample.common.PermissionResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/04
 *     desc   : 权限不可见视图，取自Glide思想
 * </pre>
 */
public class RxPermissionFragment extends Fragment {
    public static final int REQUEST = 233;
    private Subject<PermissionResult> subject;
    private List<Permission> permissionList = new ArrayList<>();

    public void request(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            subject = ReplaySubject.create(2);
            permissionList.clear();
            List<String> preRequest = new ArrayList<>();
            List<String> refuse = new ArrayList<>();
            Permission permission;

            for (String s : permissions) {
                permission = new Permission(s,
                        ContextCompat.checkSelfPermission(getContext(), s) == PackageManager.PERMISSION_GRANTED,
                        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), s));
                permissionList.add(permission);

                if (!permission.isGranted()) {
                    //权限未开启
                    preRequest.add(s);
                    if (!permission.isShouldShowRequestPermissionRationale()) {
                        //被用户勾选总是拒绝
                        refuse.add(s);
                    }
                }
            }
            if (preRequest.isEmpty()) {
                //不需要请求
                if (refuse.isEmpty()) {
                    subject.onNext(new PermissionResult(true, false, permissionList));
                } else {
                    subject.onNext(new PermissionResult(false, true, permissionList));
                }
                subject.onComplete();
                return;
            }
            //发起请求
            requestPermissions(toArray(preRequest), REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean successful = true;
        boolean disable = false;
        Permission p;
        for (int i = 0; i < permissions.length; i++) {
            for (int j = 0, length = permissionList.size(); j < length; j++) {
                p = permissionList.get(j);
                if (p.getName().equals(permissions[i])) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        successful = false;
                        p.setGranted(false);
                    } else {
                        p.setGranted(true);
                    }
                    boolean e = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), p.getName());
                    p.setShouldShowRequestPermissionRationale(e);
                    if (!e) {
                        disable = true;
                    }
                }
            }
        }
        subject.onNext(new PermissionResult(successful, disable, permissionList));
        subject.onComplete();
    }

    public Subject<PermissionResult> getSubject() {
        return subject;
    }

    private static String[] toArray(@NonNull List<String> list) {
        String[] str = new String[list.size()];
        list.toArray(str);
        return str;
    }
}
