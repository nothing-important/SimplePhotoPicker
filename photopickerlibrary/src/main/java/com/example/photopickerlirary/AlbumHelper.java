package com.example.photopickerlirary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class AlbumHelper {

    private Context context;
    private int pickPhotoNums = 1;
    private ArrayList<String> photoList = new ArrayList<>();
    private int selectImg = 0 , unSelectImg = 0 , camearImg = 0;

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
        AlbumActivity.toAlbumActivity(context , pickPhotoNums , photoList , selectImg , unSelectImg , camearImg);
    }

}
