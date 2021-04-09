package com.vnw.videoprovjp2021.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.vnw.videoprovjp2021.fragment.ImageFragment;
import com.vnw.videoprovjp2021.fragment.VideoFragment;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {
    public ViewpagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VideoFragment();
            case 1:
                return new ImageFragment();
            default:
                return new VideoFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Video";
                break;
            case 1:
                title = "Image";
                break;
        }
        return title;
    }
}
