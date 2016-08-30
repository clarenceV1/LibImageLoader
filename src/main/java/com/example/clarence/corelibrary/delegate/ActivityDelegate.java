package com.example.clarence.corelibrary.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 视图层代理基类
 * Created by clarence on 16/5/29.
 */
public abstract class ActivityDelegate implements IDelegate {
    //protected final SparseArray<View> mViews = new SparseArray<View>();
    protected View rootView;

    public abstract int getRootLayoutId();

    /**
     * 是否关闭,true已经打开(防止多次关闭或打开)
     */
    public boolean isButterKnifeOpen;

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        rootView = inflater.inflate(rootLayoutId, container, false);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void initWidget() {
        butterKnifeSwitch(true);
    }

    /**
     * 比butterknife好用
     */
//    public <T extends View> T get(int id) {
//        return (T) bindView(id);
//    }
//    public <T extends View> T bindView(int id) {
//        T view = (T) mViews.get(id);
//        if (view == null) {
//            view = (T) rootView.findViewById(id);
//            mViews.put(id, view);
//        }
//        return view;
//    }
//
//    public void setOnClickListener(View.OnClickListener listener, int... ids) {
//        if (ids == null) {
//            return;
//        }
//        for (int id : ids) {
//            get(id).setOnClickListener(listener);
//        }
//    }

    /**
     * 控件注入(注册与注销)
     */
    public void butterKnifeSwitch(boolean butterKnifeSwitch) {
        if (butterKnifeSwitch && !isButterKnifeOpen) {//开启
            isButterKnifeOpen = true;
            ButterKnife.bind(this,getRootView());
        } else if (!butterKnifeSwitch && isButterKnifeOpen) {//关闭
            isButterKnifeOpen = true;
            ButterKnife.unbind(this);
        }
    }

    public <T extends Activity> T getActivity() {
        return (T) rootView.getContext();
    }

    @Override
    public void onDestroy() {
        butterKnifeSwitch(false);
    }
}
