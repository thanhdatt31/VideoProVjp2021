package com.vnw.videoprovjp2021.adapter;

import android.net.Uri;
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

public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ViewHolder> {
    List<File> fileList;

    public void setCallback(MyImageAdapter.callback callback) {
        this.callback = callback;
    }

    callback callback;

    @NonNull
    @Override
    public MyImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageAdapter.ViewHolder holder, int position) {
        File file = fileList.get(position);
        Uri uri = Uri.fromFile(file);
        Glide.with(holder.itemView.getContext())
                .load(uri)
                .thumbnail(0.1f)
                .centerCrop()
                .into(holder.imgThumbnail);
        holder.itemView.setOnClickListener( v -> {
            callback.onClick(uri);
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }

    public void setData(List<File> data) {
        this.fileList = data;
        notifyDataSetChanged();
    }
    public interface  callback {
        void onClick(Uri uri);
    }
}
