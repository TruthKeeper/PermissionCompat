package com.tk.persample.action;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.NonNull;

import com.tk.persample.common.PermissionResult;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/04
 *     desc   : 23+校验权限
 * </pre>
 */
public class PermissionManager {
    private static final String TAG = "PermissionFragment";
    private PermissionFragment fragment;

    private PermissionManager(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        android.app.Fragment f = fm.findFragmentByTag(TAG);
        if (f == null) {
            fragment = new PermissionFragment();
            fm.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            //立即执行
            fm.executePendingTransactions();
        } else {
            fragment = (PermissionFragment) f;
        }
    }

    /**
     * 发起权限请求
     *
     * @param callback
     * @param permissions
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void request(@NonNull Callback callback, @NonNull String... permissions) {
        fragment.request(callback, permissions);
    }

    public static PermissionManager with(@NonNull Activity activity) {
        return new PermissionManager(activity);
    }

    public interface Callback {
        void call(PermissionResult result);
    }
}
