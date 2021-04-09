package com.vnw.videoprovjp2021.ultis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.util.List;

import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_SUCCESS;

public class MyAsyncTaskLoader extends AsyncTaskLoader<String> {
    ProgressDialog progressDialog;
    public MyAsyncTaskLoader(@NonNull Context context) {
        super(context);
        progressDialog = new ProgressDialog(context);
    }


    @Nullable
    @Override
    public String loadInBackground() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String videoName = "VIDEO_" + System.currentTimeMillis() + ".mp4";
        int rc = FFmpeg.execute("-f concat -safe 0 -i " + filePath + "/FFmpeg/images.txt" + " -f concat -safe 0 -i " + filePath + "/FFmpeg/audio.txt" + " -c:a aac -pix_fmt yuv420p -crf 23 -r 24 -shortest -y -vf scale=trunc(iw/2)*2:trunc(ih/2)*2 " + filePath + "/FFmpeg/" + videoName);
        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
            progressDialog.dismiss();
            return "Successfully";
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
            progressDialog.dismiss();
            return "Failed";
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            progressDialog.dismiss();
            return "Failed";
        }

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        progressDialog.setMessage("Doing Something Great...");
        progressDialog.show();
    }
}
