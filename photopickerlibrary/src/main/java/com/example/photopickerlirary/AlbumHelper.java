package com.example.photopickerlirary;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.photopickerlirary.activity.AlbumActivity;
import com.example.photopickerlirary.interfaces.SelectPhotoResult;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class AlbumHelper {

    private Context context;
    private int pickPhotoNums = 1;
    private ArrayList<String> photoList = new ArrayList<>();
    private int selectImg = 0 , unSelectImg = 0 , camearImg = 0 , titleView = 0;
    private boolean isInDetailpage = true;

    public AlbumHelper with(Context context){
        this.context = context;
        return this;
    }

    public AlbumHelper setPickPhotoNums(int pickPhotoNums){
        this.pickPhotoNums = pickPhotoNums;
        return this;
    }

    public AlbumHelper setOnReceiveResultListener(SelectPhotoResult selectPhotoResult){
        AlbumActivity.setOnSelectResultListener(selectPhotoResult);
        return this;
    }

    public AlbumHelper setResultPhotoList(ArrayList<String> list){
        photoList = list;
        return this;
    }

    public AlbumHelper setTitleView(int titleView){
        this.titleView = titleView;
        return this;
    }

    public AlbumHelper isInDetailpage(boolean isInDetailpage){
        this.isInDetailpage = isInDetailpage;
        return this;
    }

    public AlbumHelper setSelectImg(int selectImg){
        this.selectImg = selectImg;
        return this;
    }

    public AlbumHelper setUnSelectImg(int unSelectImg){
        this.unSelectImg = unSelectImg;
        return this;
    }

    public AlbumHelper setCameraImg(int camearImg){
        this.camearImg = camearImg;
        return this;
    }

    public void start(){
        AlbumActivity.toAlbumActivity(context , pickPhotoNums , photoList , selectImg , unSelectImg , camearImg , titleView , isInDetailpage);
    }

}
