package com.tk.persample.rx;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.tk.persample.common.PermissionResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/04
 *     desc   : 23+校验权限，By RxJava2
 * </pre>2
 */
public class PermissionManager {
    private static final String TAG = "RxPermissionFragment";
    private RxPermissionFragment fragment;

    private PermissionManager(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        android.app.Fragment f = fm.findFragmentByTag(TAG);
        if (f == null) {
            fragment = new RxPermissionFragment();
            fm.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            //立即执行
            fm.executePendingTransactions();
        } else {
            fragment = (RxPermissionFragment) f;
        }
    }

    /**
     * 发起权限请求
     *
     * @param permissions
     */
    @TargetApi(Build.VERSION_CODES.M)
    public Observable<PermissionResult> request(@NonNull final String... permissions) {
        return Observable.just(true).compose(new ObservableTransformer<Boolean, PermissionResult>() {
            @Override
            public ObservableSource<PermissionResult> apply(Observable<Boolean> upstream) {
                fragment.request(permissions);
                return fragment.getSubject();
            }
        });
    }

    /**
     * Entry
     *
     * @param activity
     * @return
     */
    public static PermissionManager with(@NonNull Activity activity) {
        return new PermissionManager(activity);
    }

    /**
     * 当用户勾选不再提示且拒绝权限时
     *
     * @param activity
     * @return
     */
    public static void toSetting(@NonNull Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }
}
