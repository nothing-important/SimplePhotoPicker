package com.example.photopickerlirary.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class CommonUtils {

    public static File createTempFile(){
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/screen_shots/photo_picker";
        File file = new File(rootPath);
        if (!file.exists()){
            file.mkdirs();
        }
        File tempFile = new File(file , System.currentTimeMillis()+".jpg");
        if (tempFile.exists()){
            tempFile.delete();
        }else {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tempFile;
    }

    public static void toSystemPhotoCapturer(Activity activity , int requestCode , File photoFile){
        try {
            Uri imageUri = null;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(activity, "com.example.photopickerlirary.fileprovider", photoFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                imageUri = Uri.fromFile(photoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 这样就将文件的存储方式和uri指定到了Camera应用中
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
