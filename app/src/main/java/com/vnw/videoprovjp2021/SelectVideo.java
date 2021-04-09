package com.vnw.videoprovjp2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vnw.videoprovjp2021.adapter.SelectVideoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectVideo extends AppCompatActivity {
    Toolbar toolbar;
    String folderPath;
    RecyclerView recyclerView;
    SelectVideoAdapter selectVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        folderPath = getIntent().getStringExtra("folderPath");
        initToolbar();
        initRecyclerView();
        getListFile();
        selectVideoAdapter.setCallback(new SelectVideoAdapter.callback() {
            @Override
            public void onClick(Uri uri) {
                Intent intent = new Intent(SelectVideo.this,CaptureVideo.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("uriVideo",uri);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongPress(Uri uri) {
                Toast.makeText(SelectVideo.this, uri.getPath(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListFile() {
        if(!folderPath.isEmpty()){
            File f = new File(folderPath);
            File[] file = f.listFiles();
            List<File> fileList = new ArrayList<>();
            for (File value : file) {
                if (value.getName().contains(".mp4")) {
                    fileList.add(value);
                }
            }
            Collections.reverse(fileList);
            selectVideoAdapter.setData(fileList);
            recyclerView.setAdapter(selectVideoAdapter);
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        selectVideoAdapter = new SelectVideoAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
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