package com.example.clarence.corelibrary;

import android.content.Context;

import com.example.clarence.datastorelibrary.StoreFactory;
import com.example.clarence.utillibrary.encrypt.EncryptManager;

import de.greenrobot.event.EventBus;

/**
 * 工具初始化 要移动到主项目比较好
 */
public class ConfigSwitch {
    public Context context;

    /**
     * 是否关闭,true已经打开(防止多次关闭或打开)
     */
    public boolean isEventBusOpen;

    public ConfigSwitch(Context context) {
        this.context = context;
    }


    /**
     * 初始化Http
     */
    public void initHttp() {

    }

    /**
     * 初始化数据存储
     */
    public void initDataStore() {
        StoreFactory.getStoreSharePreference().init(context);
    }

    /**
     * 初始化加密工具
     */
    public void initEncry() {
        EncryptManager.getInstance().init(context);
    }

    /**
     * 奔溃日志处理
     */
    public void initCrashManager() {
        //启动闪退日志捕获
        CrashManager.getInstance().init(context);
    }

    /**
     * 事件分发
     *
     * @param evnetBusSwitch
     */
    public void evenBusSwitch(boolean evnetBusSwitch) {
        if (evnetBusSwitch && !isEventBusOpen) {
            EventBus.getDefault().register(context);
        } else if (!evnetBusSwitch && isEventBusOpen) {
            EventBus.getDefault().unregister(context);
        }
    }
}
