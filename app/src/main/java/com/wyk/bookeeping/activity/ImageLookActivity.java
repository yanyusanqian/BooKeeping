package com.wyk.bookeeping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.wyk.bookeeping.R;

public class ImageLookActivity extends AppCompatActivity {
    private String Path="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagelook);
        ImageView imagelook = findViewById(R.id.imagelook);
        LinearLayout imagelook_return = findViewById(R.id.imagelook_return);

        Intent intent = getIntent();
        Path = intent.getStringExtra("IMAGE");


        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.headportrait)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Log.i("TAG",Path);
        Glide.with(this)
                .load(Path)
                .apply(requestOptions).into(imagelook);

        imagelook_return.setOnClickListener(v->{
            finish();
        });
    }
}
