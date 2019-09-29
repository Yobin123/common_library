package com.yobin_he.packagedemo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * @Author: yobin he
 * @Date:2019/8/27 14:34
 * @Email: heyibin@huawenpicture.com
 * @Des : 请求权限帮助类。
 * 使用步骤
 * 1.在使用功能时候需要检查权限（checkPermissions）
 * ①有权限 进行相关操作
 * ②无权限 请求权限（requestPermissions）
 * 2 回调权限结果函数中
 * 例子：
 * if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
 * if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
 * //允许权限，有调起相机拍照。
 * openCamera();
 * } else {
 * //拒绝权限，弹出提示框。
 * showExceptionDialog();
 * }
 * }
 * ①如果同意 进行下一步操作
 * ②如果拒绝 显示设置弹窗（showExceptionDialog）
 */
public class PermissionUtil {

    //检查是否有权限
    public static boolean checkPermissions(Context context, String[] permissions) {
        if (permissions == null || context == null) {
            return false;
        }
        if (context != null) {
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            for (String permission : permissions) {
                int per = manager.checkPermission(permission, packageName);
                if (per == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    //请求权限
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 发生没有权限等异常时，显示一个提示dialog.(放在)
     */
    public static void showExceptionDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("使用该功能需要权限，请到“设置”>“应用”>“权限”中配置权限。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        activity.finish();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startAppSettings(activity);
            }
        }).show();
    }


    /**
     * 启动应用的设置
     */
    private static void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }


}
