package com.vnw.videoprovjp2021.adapter;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vnw.videoprovjp2021.R;

import java.io.File;
import java.util.List;

public class ImageSlideShowAdapter extends RecyclerView.Adapter<ImageSlideShowAdapter.ViewHolder> {
    List<? extends Uri> listImages;
    @NonNull
    @Override
    public ImageSlideShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSlideShowAdapter.ViewHolder holder, int position) {
        Uri uri = listImages.get(position);
        Glide.with(holder.itemView.getContext())
                .load(uri)
                .centerCrop()
                .into(holder.imageThumbnail);
    }

    @Override
    public int getItemCount() {
        if (listImages.size() < 9){
            return listImages.size();
        } else {
            return 9;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageThumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }
    public void setData(List<? extends Uri> data){
        this.listImages = data;
        notifyDataSetChanged();
    }

}
