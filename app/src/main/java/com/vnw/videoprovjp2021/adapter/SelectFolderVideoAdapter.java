package com.vnw.videoprovjp2021.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodeBoy.MediaFacer.mediaHolders.videoFolderContent;
import com.vnw.videoprovjp2021.R;

import java.util.List;

public class SelectFolderVideoAdapter extends RecyclerView.Adapter<SelectFolderVideoAdapter.ViewHolder> {
    List<videoFolderContent> videoFolderContents;
    callback callback;

    public void setCallback(SelectFolderVideoAdapter.callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public SelectFolderVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_folder_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectFolderVideoAdapter.ViewHolder holder, int position) {
        videoFolderContent videoFolderContent = videoFolderContents.get(position);
        String title = String.format("%s (%s)",videoFolderContent.getFolderName(),videoFolderContent.getVideoFiles().size());
        holder.folderTitle.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(videoFolderContent.getFolderPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoFolderContents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderTitle = itemView.findViewById(R.id.tv_title_folder);
        }
    }

    public void setData(List<videoFolderContent> data) {
        this.videoFolderContents = data;
        notifyDataSetChanged();
    }

    public interface callback{
        void onClick(String folderPath);
    }
}
