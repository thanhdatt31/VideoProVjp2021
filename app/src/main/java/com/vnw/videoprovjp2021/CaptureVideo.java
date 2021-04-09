package com.vnw.videoprovjp2021;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class CaptureVideo extends AppCompatActivity {
    VideoView videoView;
    ImageView btnPlay, btnPause, btnForward, btnBack;
    Toolbar toolbar;
    Uri uri;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);
        initToolbar();
        Bundle bundle = getIntent().getExtras();
        uri = bundle.getParcelable("uriVideo");
        videoView = findViewById(R.id.videoplayer);
        videoView.setVideoURI(uri);
        videoView.start();
        controllerVideo();
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.capture_menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.capture:
                try {
                    captureFrame(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.done:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void captureFrame(Uri uri) throws IOException {
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        mmr.setDataSource(this, uri);
        Bitmap captureBitmap = mmr.getFrameAtTime(videoView.getCurrentPosition() * 1000);
        saveBitmap(captureBitmap);
    }

    private void saveBitmap(Bitmap captureBitmap) throws IOException {
        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsolutePath() + "/MyCaptured");
        dir.mkdirs();
        File file = new File(dir, "IMG_" + System.currentTimeMillis() + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            captureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(this, "Capture Saved", Toast.LENGTH_SHORT).show();
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void controllerVideo() {
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnForward = findViewById(R.id.btn_forward);
        btnBack = findViewById(R.id.btn_back);
        btnPause.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.INVISIBLE);
        btnPause.setOnClickListener(v -> {
            videoView.pause();
            btnPause.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            currentPosition = videoView.getCurrentPosition();
            btnPause.setVisibility(View.INVISIBLE);
            btnPlay.setVisibility(View.VISIBLE);
        });
        btnPlay.setOnClickListener(v -> {
            if (videoView.getCurrentPosition() == 0) {
                videoView.start();
            } else {
                resumeVideo();
            }
            btnPause.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        });
        btnForward.setOnClickListener(v -> {
            videoView.seekTo(videoView.getCurrentPosition() + 5000);
            videoView.start();
            btnPause.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        });
        btnBack.setOnClickListener(v -> {
            videoView.seekTo(videoView.getCurrentPosition() - 5000);
            videoView.start();
            btnPause.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        });

    }

    private void resumeVideo() {
        videoView.seekTo(currentPosition);
        videoView.start();
    }
}