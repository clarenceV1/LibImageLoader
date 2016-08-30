package com.example.clarence.corelibrary;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.example.clarence.utillibrary.AnonymityUtils;
import com.example.clarence.utillibrary.LogUtils;
import com.example.clarence.utillibrary.PackageUtils;
import com.example.clarence.utillibrary.StringUtils;

import java.util.List;

/**
 * Created by clarence on 3/27/16.
 */
public class CApplication extends BaseApplication {
    public static final String KEY_DEX2_SHA1 = "DEX2-SHA1-DIGEST";
    public static final String MANIFEST_KEY = "SHA1-Digest";
    protected ConfigSwitch configSwitch;

    @Override
    public void onCreate() {
        super.onCreate();
        multidex();
        initConf();
    }

    private void multidex() {
        //// TODO: 3/27/16
    }

    /**
     * 不要移除 natvie层会调用
     */
    protected void initConf() {
        configSwitch = new ConfigSwitch(this);
        configSwitch.initHttp();
        configSwitch.initDataStore();
//        configSwitch.initImageLoader();
//        configSwitch.initEncry();
//        configSwitch.initCrashManager();
    }

    public ConfigSwitch getConfigSwitch() {
        return configSwitch;
    }
}
