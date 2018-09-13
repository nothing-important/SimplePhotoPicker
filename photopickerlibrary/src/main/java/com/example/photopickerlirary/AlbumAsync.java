package com.example.photopickerlirary;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.photopickerlirary.entity.PhotoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumAsync extends AsyncTask<String , String , Map<String , List<PhotoBean>>> {

    private static final String TAG = "AlbumAsync";
    private Context context;
    private Map<String , List<PhotoBean>> map;
    private PhotoLoadListener photoLoadListener;

    public AlbumAsync(Context context) {
        this.context = context;
        map = new HashMap<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, List<PhotoBean>> doInBackground(String... strings) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg" , "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor == null){
            return null;
        }
        while (cursor.moveToNext()){
            //获取路径
            String path = cursor.getString (cursor.getColumnIndex (MediaStore.Images.Media.DATA));
            //父文件名
            String parentPath = new File(path).getParentFile().getName();
            //根据父文件名对图片进行分类
            PhotoBean bean = new PhotoBean();
            bean.setSelected(false);
            if (map.containsKey(parentPath)){//集合中有该父文件  直接添加
                bean.setPath(path);
                map.get(parentPath).add(bean);
            }else {
                List<PhotoBean> list = new ArrayList<>();
                bean.setPath(path);
                list.add(bean);
                map.put(parentPath , list);
            }
        }
        cursor.close();
        return map;
    }

    @Override
    protected void onPostExecute(Map<String, List<PhotoBean>> stringListMap) {
        super.onPostExecute(stringListMap);
        if (photoLoadListener == null){
            Log.e(TAG, "onPostExecute: 相册初始化失败，监听为空");
            return;
        }
        photoLoadListener.onPhotoFinished(map);
    }

    public interface PhotoLoadListener{
        void onPhotoFinished(Map<String, List<PhotoBean>> resultMap);
    }

    public void setOnPhotoLoadListener(PhotoLoadListener photoLoadListener){
        this.photoLoadListener = photoLoadListener;
    }
}
