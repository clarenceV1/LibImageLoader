package com.example.clarence.corelibrary.presenter;

import android.os.Bundle;

import com.example.clarence.corelibrary.BaseActivity;
import com.example.clarence.corelibrary.delegate.IDelegate;


/**
 * 处理Activity上的视图
 * Created by clarence on 16/5/29.
 */
public abstract class BaseActivityPresenter extends BaseActivity {
    protected IDelegate viewDelegate;

    public BaseActivityPresenter() {
        viewDelegate = getViewDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            viewDelegate.create(getLayoutInflater(), null, savedInstanceState);
            setContentView(viewDelegate.getRootView());
            viewDelegate.initWidget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract IDelegate getViewDelegate();

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getViewDelegate();
            } catch (Exception e) {
                throw new RuntimeException("create IDelegate error");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewDelegate.onDestroy();
        viewDelegate = null;
    }
}
