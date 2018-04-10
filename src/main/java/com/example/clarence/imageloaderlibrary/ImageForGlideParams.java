package com.example.clarence.imageloaderlibrary;

import android.widget.ImageView;

/**
 * Created by clarence on 2018/4/9.
 */

public class ImageForGlideParams implements ILoadImageParams {
    private ImageView imageView;
    private String url;

    private ImageForGlideParams(Builder builder) {
        url = builder.url;
    }

    public ImageView getImageView() {
        return imageView;
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

        public Builder() {
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public ImageForGlideParams build() {
            return new ImageForGlideParams(this);
        }
    }
}
