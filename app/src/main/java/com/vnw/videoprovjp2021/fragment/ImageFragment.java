package com.vnw.videoprovjp2021.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vnw.videoprovjp2021.R;
import com.vnw.videoprovjp2021.ShowImage;
import com.vnw.videoprovjp2021.adapter.MyImageAdapter;
import com.vnw.videoprovjp2021.adapter.SelectVideoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageFragment extends Fragment {
    RecyclerView recyclerView;
    MyImageAdapter myImageAdapter;
    String folderPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        folderPath = Environment.getExternalStorageDirectory() + "/MyCaptured";
        initRecyclerView(view);
        getListFile();
        myImageAdapter.setCallback(new MyImageAdapter.callback() {
            @Override
            public void onClick(Uri uri) {
                Intent intent = new Intent(getContext(), ShowImage.class);
                intent.putExtra("image_uri",uri);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        myImageAdapter = new MyImageAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
    }

    private void getListFile() {
        if (!folderPath.isEmpty()) {
            File f = new File(folderPath);
            File[] file = f.listFiles();
            List<File> fileList = new ArrayList<>();
            for (File value : file) {
                if (value.getName().contains("IMG")) {
                    fileList.add(value);
                }
            }
            Collections.reverse(fileList);
            myImageAdapter.setData(fileList);
            recyclerView.setAdapter(myImageAdapter);
        }
    }
}