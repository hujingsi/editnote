package com.example.lcjingsi.memoran;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.VideoView;
import java.io.File;

public class videoviewer extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vv = new VideoView(this);

        vv.setMediaController(new MediaController(this));
        setContentView(vv);

        String path = getIntent().getStringExtra(EXTRA_PATH);
        if(path!= null){
            vv.setVideoPath(path);
        }else {
            finish();
        }
    }

    private VideoView vv;
    public static final  String EXTRA_PATH ="path";
}

