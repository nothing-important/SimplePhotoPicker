package com.example.photopickerlirary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.photopickerlirary.entity.PhotoBean;
import com.example.photopickerlirary.interfaces.DetailPhotoClick;

import java.util.List;

public class DetailAdapter extends PagerAdapter {

    private List<PhotoBean> list;
    private Context context;
    private DetailPhotoClick detailPhotoClick;

    public DetailAdapter(List<PhotoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        ImageView imageView = new ImageView(context);
        Glide.with(context).load(list.get(position).getPath()).into(imageView);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailPhotoClick == null)return;
                detailPhotoClick.onDetailPhotoClick(position);
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setOnDetailPhotoClickListener(DetailPhotoClick detailPhotoClick){
        this.detailPhotoClick = detailPhotoClick;
    }
}
