package com.vnw.videoprovjp2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
Toolbar toolbar;
ImageButton btnSelectVideo,btnMyCapture,btnSlideshow,btnSettings;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        btnSelectVideo = findViewById(R.id.btn_select);
        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SelectFolderVideo.class);
                startActivity(intent);
            }
        });
        btnMyCapture = findViewById(R.id.btn_capture);
        btnMyCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,MyCapture.class);
                startActivity(intent);
            }
        });
        btnSlideshow = findViewById(R.id.btn_slide_show);
        btnSlideshow.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this,SlideShowMaker.class);
            startActivity(intent);
        });
        btnSettings = findViewById(R.id.btn_setting);
        btnSettings.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this,Settings.class);
            startActivity(intent);
        });

    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                return true;
            case R.id.about:
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.rate:
                Toast.makeText(this, "rate", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}