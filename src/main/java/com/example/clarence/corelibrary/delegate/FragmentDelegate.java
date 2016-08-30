package com.example.clarence.corelibrary.delegate;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

/**
 * Created by clarence on 16/6/13.
 */
public abstract class FragmentDelegate extends ActivityDelegate {
    public abstract String getFragmentTag();

    public Fragment getFragment() {
        if(TextUtils.isEmpty(getFragmentTag())){
            return null;
        }
        Fragment fragment = ((FragmentActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(getFragmentTag());
        return fragment;
    }
}
