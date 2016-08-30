package com.example.clarence.corelibrary.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clarence.corelibrary.BaseFragment;
import com.example.clarence.corelibrary.delegate.IDelegate;

/**
 * Created by clarence on 16/5/29.
 */
public abstract class BaseFragmentPresenter extends BaseFragment {
    public IDelegate viewDelegate;

    public abstract IDelegate getViewDelegate();

    public abstract String getFragmentTag();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            viewDelegate = getViewDelegate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDelegate.initWidget();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewDelegate.create(inflater, container, savedInstanceState);
        return viewDelegate.getRootView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getViewDelegate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDelegate = null;
    }
}
