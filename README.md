# PermissionCompat
6.0+权限获取工具

采用[Glide](https://github.com/bumptech/glide)的Fragment依附思想，无需继承Activity，傻瓜式调用，传入权限的String参数为可变长度型，支持2种模式

- 普通的Callback回调
- 通过RxJava2生成事件流

Callback用法：
```
//PermissionManager在action包下
PermissionManager.with(this)
                 .request(new PermissionManager.Callback() {
                        @Override
                        public void call(PermissionResult result) {
                            process(result);
                        }
                    }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
```
RxJava2用法
```
//PermissionManager在rx包下
PermissionManager.with(this)
        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(new Consumer<PermissionResult>() {
            @Override
            public void accept(@NonNull PermissionResult permissionResult) throws Exception {
                process(permissionResult);
            }
        });
```
返回的权限结果处理实例：
```
private void process(PermissionResult permissionResult) {
    if (permissionResult.isSuccessful()) {
        Toast.makeText(getApplicationContext(), "成功GET√", Toast.LENGTH_SHORT).show();
        return;
    }
    if (permissionResult.isDisable()) {
        //用户傲娇的勾选了不再提示，并且拒绝了权限
        Toast.makeText(getApplicationContext(), "权限被禁用啦，请手动开启权限", Toast.LENGTH_SHORT).show();
        PermissionManager.toSetting(MainActivity.this);
    } else {
        //用户拒绝了权限
        Toast.makeText(getApplicationContext(), "宝宝摔倒了，要开启权限才能爬起来~~~", Toast.LENGTH_SHORT).show();
    }

}
```
Ps：``request``方法用``@TargetApi``修饰，API23以下不执行

![效果](https://github.com/TruthKeeper/PermissionCompat/blob/master/screenshot/demo.gif)
