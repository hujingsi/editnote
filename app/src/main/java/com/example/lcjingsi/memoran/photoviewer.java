package com.example.lcjingsi.memoran;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.io.File;

public class photoviewer extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iv = new ImageView(this);
        setContentView(iv);

        String path = getIntent().getStringExtra(EXTRA_PATH);
        if(path!= null){
            iv.setImageURI(Uri.fromFile(new File(path)));
        }else {
            finish();
        }
    }
    private ImageView iv;
    public static final String EXTRA_PATH ="path";
}
