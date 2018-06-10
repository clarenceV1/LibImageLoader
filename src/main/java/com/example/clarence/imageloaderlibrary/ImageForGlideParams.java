package com.example.clarence.imageloaderlibrary;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by clarence on 2018/4/9.
 */

public class ImageForGlideParams implements ILoadImageParams {
    private ImageView imageView;
    private String url;
    private BitmapTransformation transformation;

    private ImageForGlideParams(Builder builder) {
        url = builder.url;
        transformation = builder.transformation;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    @Override
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getUrl() {
        return url;
    }

    public static final class Builder {
        private String url;
        private BitmapTransformation transformation;

        public Builder() {
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder transformation(BitmapTransformation bitmapTransformation) {
            transformation = bitmapTransformation;
            return this;
        }

        public ImageForGlideParams build() {
            return new ImageForGlideParams(this);
        }
    }
}
