package com.tk.persample.common;

import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/04
 *     desc   : 回调结果
 * </pre>
 */
public class PermissionResult {
    private final boolean successful;

    private final boolean disable;
    private final List<Permission> permissions;

    public PermissionResult(boolean successful, boolean disable, List<Permission> permissions) {
        this.successful = successful;
        this.disable = disable;
        this.permissions = permissions;
    }

    /**
     * 是否获取权限成功
     *
     * @return
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * 是否有被用户勾选拒绝权限，且不再显示
     *
     * @return
     */
    public boolean isDisable() {
        return disable;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

}
