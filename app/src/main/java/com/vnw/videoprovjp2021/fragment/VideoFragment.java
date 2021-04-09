package com.vnw.videoprovjp2021.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vnw.videoprovjp2021.MainActivity;
import com.vnw.videoprovjp2021.R;
import com.vnw.videoprovjp2021.adapter.SelectVideoAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class VideoFragment extends Fragment {
    String folderPath;
    SelectVideoAdapter selectVideoAdapter;
    RecyclerView recyclerView;
    final String[] Options = {"Open", "Rename", "Delete", "Share", "Details"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FFmpeg/";
        initRecyclerView(view);
        getListFile();
        selectVideoAdapter.setCallback(new SelectVideoAdapter.callback() {
            @Override
            public void onClick(Uri uri) {
                openVideo(uri);
            }

            @Override
            public void onLongPress(Uri uri) {
                showOptionDialog(uri);
            }
        });
        return view;
    }

    private void openVideo(Uri uri) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void showOptionDialog(Uri uri) {
        AlertDialog.Builder window;
        window = new AlertDialog.Builder(requireContext());
        window.setTitle("Choose An Option");
        window.setItems(Options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openVideo(uri);
                        break;
                    case 1:
                        openRenameDialog(uri);
                        dialog.dismiss();
                        break;
                }
            }
        });

        window.show();
    }

    private void openRenameDialog(Uri uri) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_option, null, false);
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle("Your New File Name");
        alertDialog.setCancelable(false);
        final EditText etComments = (EditText) view.findViewById(R.id.etComments);
        etComments.setText(getFileName(uri));
        Log.d("datnt", uri.getPath());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), etComments.getText().toString(), Toast.LENGTH_SHORT).show();
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeAllViews();
                renameVideo(uri,etComments.getText().toString());
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeAllViews();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void renameVideo(Uri uri,String fileName) {
        File filePath = Environment.getExternalStorageDirectory();
        File from = new File(uri.getPath());
        File to = new File(filePath + "/FFmpeg",fileName);
        from.renameTo(to);
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        selectVideoAdapter = new SelectVideoAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
    }

    private void getListFile() {
        if (!folderPath.isEmpty()) {
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
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}