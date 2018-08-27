package com.example.photopickerlirary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity {

    private PermissionRequestListener permissionRequestListener;
    private int permissionRequestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 请求运行时权限
     * @param permissions 需要请求的权限
     * @param requestCode 请求码
     * @param permissionRequestListener 结果回调
     */
    public void requestSelfPermissions(String [] permissions , int requestCode , PermissionRequestListener permissionRequestListener){
        this.permissionRequestListener = permissionRequestListener;
        List<String> grantedPermissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0 ; i < permissions.length ; i ++){
                String permission = permissions[i];
                int permissionStatus = ContextCompat.checkSelfPermission(this, permission);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED){
                    grantedPermissions.add(permission);
                }else {
                    deniedPermissions.add(permission);
                }
            }
            if (deniedPermissions.size() == 0){//全部通过
                permissionRequestListener.onPermissionGranted(requestCode , grantedPermissions);
            }else {
                this.permissionRequestCode = requestCode;
                requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]) , requestCode);
            }
        }else {
            for (int i = 0 ; i < permissions.length ; i ++){
                grantedPermissions.add(permissions[i]);
            }
            permissionRequestListener.onPermissionGranted(requestCode , grantedPermissions);
        }

    }

    /**
     * 请求运行权限回调接口
     */
    public interface PermissionRequestListener{
        void onPermissionGranted(int requestCode, List<String> allGrantedPermission);
        void onPermissionDenied(int requestCode, List<String> deniedPermissions);
    }

    /**
     *展示权限设置弹框
     */
    public void showSettingDialog(Context context) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("权限相关提示")
                .setMessage("检测到您未允许应用获得相关权限，为了您更好的体验，建议您开启")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goSetting();
                    }
                })
                .setNegativeButton("算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    /**
     * 进入应用设置界面
     */
    private void goSetting(){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionRequestCode){
            List<String> grantedPermissions = new ArrayList<>();
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0 ; i < grantResults.length ; i ++){
                int grantResult = grantResults[i];
                String permission = permissions[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED){
                    grantedPermissions.add(permission);
                }else {
                    deniedPermissions.add(permission);
                }
            }
            if (deniedPermissions.size() == 0){
                permissionRequestListener.onPermissionGranted(requestCode , grantedPermissions);
            }else {
                permissionRequestListener.onPermissionDenied(requestCode , deniedPermissions);
            }
        }
    }

}
