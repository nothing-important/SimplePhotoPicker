package com.example.photopickerlirary;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumAdapter_VH> {

    private static final String TAG = "AlbumAdapter";
    private List<PhotoBean> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private int viewWidth , selectImg , unSelectImg , camearImg;
    private PhotoClickListener photoClickListener;

    public AlbumAdapter(List<PhotoBean> list, Context context , int selectImg , int unSelectImg , int camearImg) {
        this.list = list;
        this.context = context;
        this.selectImg = selectImg;
        this.unSelectImg = unSelectImg;
        this.camearImg = camearImg;
        layoutInflater = LayoutInflater.from(context);
        int windowWidth = getWindowWidth(context);
        viewWidth = (windowWidth - dp2px(context , 64)) / 4;//64的值为左右边距dp值的相加
        addCameraBean(this.list);
    }

    @NonNull
    @Override
    public AlbumAdapter_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.itemview_album, parent , false);
        return new AlbumAdapter_VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumAdapter_VH holder, final int position) {
        final PhotoBean bean = list.get(position);
        holder.album_item_select.setImageResource(R.drawable.icon_circle);
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.album_item_img.getLayoutParams();
        layoutParams.width = viewWidth;
        layoutParams.height = viewWidth;
        holder.album_item_img.setLayoutParams(layoutParams);
        if (bean.getPath() == null && !bean.isSelected()){//相机
            holder.album_item_select.setVisibility(View.GONE);
            if (camearImg != 0){
                Glide.with(context).load(camearImg).into(holder.album_item_img);
            }else {
                Glide.with(context).load(R.drawable.camera).into(holder.album_item_img);
            }
        }else {//图片
            holder.album_item_select.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
            Glide.with(context).load(bean.getPath()).apply(requestOptions).thumbnail(0.4f).into(holder.album_item_img);
            if (bean.isSelected()){
                if (selectImg != 0){
                    holder.album_item_select.setImageResource(selectImg);
                }else {
                    holder.album_item_select.setImageResource(R.drawable.icon_duigou);
                }
            }else {
                if (unSelectImg != 0){
                    holder.album_item_select.setImageResource(unSelectImg);
                }else {
                    holder.album_item_select.setImageResource(R.drawable.icon_circle);
                }
            }
        }
        holder.album_item_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoClickListener == null){
                    Log.e(TAG, "click listener is null , you must set click listener");
                    return;
                }
                photoClickListener.onPhotoSelectClick(position);
            }
        });
        holder.album_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoClickListener == null){
                    Log.e(TAG, "click listener is null , you must set click listener");
                    return;
                }
                if (bean.getPath() == null && !bean.isSelected()){
                    photoClickListener.onCameraClick();
                }else {
                    photoClickListener.onPhotoClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class AlbumAdapter_VH extends RecyclerView.ViewHolder{
        ImageView album_item_img , album_item_select;
        public AlbumAdapter_VH(View itemView) {
            super(itemView);
            album_item_img = itemView.findViewById(R.id.album_item_img);
            album_item_select = itemView.findViewById(R.id.album_item_select);
        }
    }

    private int dp2px(Context context , float dpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    /**
     * 获取屏幕宽度
     */
    private int getWindowWidth(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 图片点击接口
     */
    public interface PhotoClickListener{
        void onPhotoSelectClick(int psn);
        void onPhotoClick(int psn);
        void onCameraClick();
    }

    public void setOnPhotoClickListener(PhotoClickListener photoClickListener){
        this.photoClickListener = photoClickListener;
    }

    private void addCameraBean(List<PhotoBean> list){
        PhotoBean photoBean = new PhotoBean();
        photoBean.setPath(null);
        photoBean.setSelected(false);
        list.add(photoBean);
    }

}
