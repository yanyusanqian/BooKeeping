package com.wyk.bookeeping.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;
import com.wyk.bookeeping.R;

public class ImageLoader implements IZoomMediaLoader {
    RequestOptions options;

    {
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.headportrait)
                .error(R.mipmap.headportrait)
                .priority(Priority.HIGH);
    }

    @Override
    public void displayImage(Fragment context, String path, final MySimpleTarget<Bitmap> simpleTarget) {
        Glide.with(context)
                .asBitmap()
                .load(path)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        simpleTarget.onResourceReady(resource);
                    }
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        simpleTarget.onLoadStarted();
                    }
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        simpleTarget.onLoadFailed(errorDrawable);
                    }
                });
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        Glide.with(context).onStop();
    }
    @Override
    public void clearMemory(@NonNull Context c) {
        Glide.get(c).clearMemory();
    }
}