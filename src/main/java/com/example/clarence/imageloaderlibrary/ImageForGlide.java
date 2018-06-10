package com.example.clarence.imageloaderlibrary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by clarence on 2018/4/9.
 */

public class ImageForGlide implements ILoadImage {


    public ImageForGlide(ImageForGlideBuild imageForGlideBuild) {

    }

    public static class ImageForGlideBuild {

        public ImageForGlideBuild() {

        }

        public ImageForGlide build() {
            return new ImageForGlide(this);
        }
    }

    @Override
    public void loadImage(Context context, ILoadImageParams builder) {
        if (builder instanceof ImageForGlideParams) {
            ImageForGlideParams glideParams = (ImageForGlideParams) builder;
            if (glideParams.getTransformation() != null) {
                Glide.with(context).load(glideParams.getUrl())
                        .centerCrop()
                        .transform(glideParams.getTransformation())
                        .into(glideParams.getImageView());
            } else {
                Glide.with(context).load(glideParams.getUrl()).into(glideParams.getImageView());
            }
        }
    }

    @Override
    public void loadImage(FragmentActivity activity, ILoadImageParams builder) {
        if (builder instanceof ImageForGlideParams) {
            ImageForGlideParams glideParams = (ImageForGlideParams) builder;
            if (glideParams.getTransformation() != null) {
                Glide.with(activity).load(glideParams.getUrl())
                        .centerCrop()
                        .transform(glideParams.getTransformation())
                        .into(glideParams.getImageView());
            } else {
                Glide.with(activity).load(glideParams.getUrl()).into(glideParams.getImageView());
            }
        }
    }

    @Override
    public void loadImage(Fragment fragment, ILoadImageParams builder) {
        if (builder instanceof ImageForGlideParams) {
            ImageForGlideParams glideParams = (ImageForGlideParams) builder;
            if (glideParams.getTransformation() != null) {
                Glide.with(fragment).load(glideParams.getUrl())
                        .centerCrop()
                        .transform(glideParams.getTransformation())
                        .into(glideParams.getImageView());
            } else {
                Glide.with(fragment).load(glideParams.getUrl()).into(glideParams.getImageView());
            }
        }
    }
}
