package com.vnw.videoprovjp2021;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.vnw.videoprovjp2021.adapter.ImageSlideShowAdapter;
import com.vnw.videoprovjp2021.ultis.MyAsyncTaskLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;

import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_SUCCESS;

public class SlideShowMaker extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    Toolbar toolbar;
    Button makeVideoButton;
    ImageView chooseImage, chooseAudio, playAudio, pauseAudio;
    RecyclerView recyclerView;
    ImageSlideShowAdapter imageSlideShowAdapter;
    TextView audioTitle;
    MediaPlayer mediaPlayer;
    LoaderManager mLoaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_maker);
        initToolbar();
        mLoaderManager = LoaderManager.getInstance(this);
        mediaPlayer = new MediaPlayer();
        chooseAudio = findViewById(R.id.img_choose_audio);
        makeVideoButton = findViewById(R.id.btn_make_video);
        playAudio = findViewById(R.id.img_play_audio);
        pauseAudio = findViewById(R.id.img_pause_audio);
        audioTitle = findViewById(R.id.tv_audio_title);
        chooseImage = findViewById(R.id.img_choose_image);
        chooseImage.setOnClickListener(v -> {
            TedImagePicker.with(this)
                    .startMultiImage(uriList -> {
                        showImage(uriList);
                        try {
                            createImageTextList(uriList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        makeVideoButton.setOnClickListener(this::startMyAsyncTask);
                    });
        });
        chooseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Gallery"), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                playAudio.setVisibility(View.VISIBLE);
                audioTitle.setVisibility(View.VISIBLE);
                audioTitle.setText(getFileName(uri));
                try {
                    mediaPlayer.setDataSource(this, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                playAudio.setOnClickListener(v -> {
                    mediaPlayer.start();
                });
                try {
                    createAudioTextFile(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createAudioTextFile(Uri uri) throws IOException {
        String data = "file " + getPathFromURI(uri);
        writeAudioToFile(data);
    }

    private void showImage(List<? extends Uri> uriList) {
        recyclerView = findViewById(R.id.recyclerview);
        imageSlideShowAdapter = new ImageSlideShowAdapter();
        imageSlideShowAdapter.setData(uriList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(imageSlideShowAdapter);
        if (uriList != null) {
            chooseAudio.setVisibility(View.VISIBLE);
            makeVideoButton.setVisibility(View.VISIBLE);
        }
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

    private void createImageTextList(List<? extends Uri> uriList) throws IOException {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < uriList.size(); i++) {
            if (i == (uriList.size() - 1)) {
                data.append("file ").append(getPathFromURI(uriList.get(i))).append("\nduration 3\n").append("file ").append(getPathFromURI(uriList.get(i)));

            } else {
                data.append("file ").append(getPathFromURI(uriList.get(i))).append("\nduration 3\n");
            }
        }
        writeToFile(data.toString());
    }

//    private String getPathFromURI(List<? extends Uri> uriList) {
//        StringBuilder data = new StringBuilder();
//        for (Uri uri : uriList) {
//            data.append(getPathFromURI(uri)).append("\nduration 3\n");
//        }
//        return data.toString();
//    }

    private void writeToFile(String data) throws IOException {
        Writer writer = null;
        try {
            // get config dir
            final File configDir = new File(Environment.getExternalStorageDirectory(), "FFmpeg");
            //make sure it's created
            configDir.mkdir();
            // open a stream
            writer = new OutputStreamWriter(new FileOutputStream(new File(configDir, "images.txt")));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    private void writeAudioToFile(String data) throws IOException {
        Writer writer = null;
        try {
            // get config dir
            final File configDir = new File(Environment.getExternalStorageDirectory(), "FFmpeg");
            //make sure it's created
            configDir.mkdir();
            // open a stream
            writer = new OutputStreamWriter(new FileOutputStream(new File(configDir, "audio.txt")));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, "", null, "");
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MyAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void startMyAsyncTask(View view) {
        mLoaderManager.initLoader(1, null, this);
    }
}