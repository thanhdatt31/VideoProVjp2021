package com.vnw.videoprovjp2021.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vnw.videoprovjp2021.R;
import com.vnw.videoprovjp2021.ultis.TimeHelper;

import java.io.File;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class SelectVideoAdapter extends RecyclerView.Adapter<SelectVideoAdapter.ViewHolder> {
    List<File> fileList;
    callback callback;

    public void setCallback(SelectVideoAdapter.callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public SelectVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectVideoAdapter.ViewHolder holder, int position) {
        File file = fileList.get(position);
        holder.tvTitle.setText(file.getName());
        Uri uri = Uri.fromFile(file);
        Glide.with(holder.itemView.getContext())
                .load(uri)
                .thumbnail(0.1f)
                .centerCrop()
                .into(holder.imgThumbnail);
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        mmr.setDataSource(holder.itemView.getContext(), uri);
        String time = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
        TimeHelper helper = new TimeHelper();
        long timeInMillisec = Long.parseLong(time);
        holder.tvDuration.setText(helper.milliSecondsToTimer(timeInMillisec));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(uri);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onLongPress(uri);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle, tvDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        }
    }

    public void setData(List<File> data) {
        this.fileList = data;
        notifyDataSetChanged();
    }
    public interface callback {
        void onClick(Uri uri);
        void onLongPress(Uri uri);
    }
}
