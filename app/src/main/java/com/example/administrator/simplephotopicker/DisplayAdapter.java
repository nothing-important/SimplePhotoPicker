package com.example.administrator.simplephotopicker;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.DisplayAdapter_VH> {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public DisplayAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DisplayAdapter_VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_diaplay_view , viewGroup , false);
        return new DisplayAdapter_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayAdapter_VH displayAdapter_vh, int i) {
        displayAdapter_vh.display_img.setImageBitmap(BitmapFactory.decodeFile(list.get(i)));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class DisplayAdapter_VH extends RecyclerView.ViewHolder{

        private ImageView display_img;

        public DisplayAdapter_VH(@NonNull View itemView) {
            super(itemView);
            display_img = itemView.findViewById(R.id.display_img);
        }
    }

}
