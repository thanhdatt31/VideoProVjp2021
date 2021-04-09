package com.vnw.videoprovjp2021;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CodeBoy.MediaFacer.MediaFacer;
import com.CodeBoy.MediaFacer.VideoGet;
import com.CodeBoy.MediaFacer.mediaHolders.videoFolderContent;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.vnw.videoprovjp2021.adapter.SelectFolderVideoAdapter;

import java.util.ArrayList;
import java.util.List;


public class SelectFolderVideo extends AppCompatActivity {
    Toolbar toolbar;
    SelectFolderVideoAdapter selectFolderVideoAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_folder_video);
        initToolbar();
        checkPermission();
    }

    private void initRecyclerview() {
        selectFolderVideoAdapter = new SelectFolderVideoAdapter();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        getFolderContainVideo();
        recyclerView.setAdapter(selectFolderVideoAdapter);
        selectFolderVideoAdapter.setCallback(new SelectFolderVideoAdapter.callback() {
            @Override
            public void onClick(String folderPath) {
                Intent intent = new Intent(SelectFolderVideo.this,SelectVideo.class);
                intent.putExtra("folderPath",folderPath);
                startActivity(intent);
            }
        });
    }

    private void getFolderContainVideo() {
        ArrayList<videoFolderContent> videoFolders = new ArrayList<>(MediaFacer.withVideoContex(this).getVideoFolders(VideoGet.externalContentUri));
        for (int i = 0; i < videoFolders.size(); i++) {
            videoFolders.get(i)
                    .setVideoFiles(MediaFacer.withVideoContex(this)
                            .getAllVideoContentByBucket_id(videoFolders.get(i).getBucket_id()));
        }
        selectFolderVideoAdapter.setData(videoFolders);
    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                initRecyclerview();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(SelectFolderVideo.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}