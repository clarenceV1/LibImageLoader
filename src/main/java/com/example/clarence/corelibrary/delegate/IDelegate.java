package com.example.clarence.corelibrary.delegate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 试图层代理接口协议
 * Created by clarence on 16/5/29.
 */
public interface IDelegate {
    void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    View getRootView();

    void initWidget();

    void onDestroy();
}
