package com.example.clarence.corelibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.clarence.datastorelibrary.StoreFactory;
import com.example.clarence.utillibrary.LogUtils;
import com.example.clarence.utillibrary.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 配置添加方法：在assets/xxx.properties文件中添加对应环境的配置，
 * 然后在 ConfigKey添加配置项key的常量定义
 */
public class ConfigManager {
    static final String sTag = "ConfigManager";

    private Environment mEnv;
    private Bundle metaData;
    private String envFlg;
    private static final String SP_KEY_ENV_FLAG = "sp-env-flag";
    private ConcurrentMap<String, String> config = new ConcurrentHashMap<String, String>();
    private Context mContext;

    public ConfigManager(Context context) {
        this.mContext = context;
        String value = StoreFactory.getStoreSharePreference().getStringValue(SP_KEY_ENV_FLAG, "");
        try {
            if (TextUtils.isEmpty(value)) {
                mEnv = Environment.valueOf(value);
            }
        } catch (Exception e) {
            LogUtils.e(sTag, e.getLocalizedMessage());
        }
        if (mEnv == null) {
            try {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA);
                metaData = applicationInfo.metaData;
                if (metaData != null) {
                    envFlg = StringUtils.trimToNull(metaData
                            .getString("ENV_MODE"));
                    if (envFlg != null) {
                        mEnv = Environment.valueOf(envFlg.toUpperCase());
                    } else {
                        throw new RuntimeException("error!! no 'ENV_MODE' find in manifest," +
                                " you need set this metaData!!!");
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                LogUtils.e(sTag, e.getMessage());
            }
        }
        try {
            init(context, mEnv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchEnv(Context context, Environment env) {
        config.clear();
        try {
            init(context, env);
            StoreFactory.getStoreSharePreference().setStringValue(SP_KEY_ENV_FLAG, env.name());
        } catch (IOException e) {
            LogUtils.e(sTag,e.getLocalizedMessage());
        }
    }

    private void init(Context context, Environment env) throws IOException {
        // 非配置文件中的重要配置
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            LogUtils.e(sTag, e.getMessage());
        }
        readConfFile(context, env.getFilePath());
    }


    /**
     * 读取最重要的 conf  file 比如product-env.properties
     * 该配置由app 配置
     *
     * @param context
     * @param fileName
     */
    private void readConfFile(Context context, String fileName) {
        InputStream is = null;
        try {
            // 初始化本地配置
            is = context.getAssets().open(fileName);
            Properties properties = new Properties();
            properties.load(is);
            Enumeration<Object> keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = properties.getProperty(key);
                config.put(key, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {//如果只是关闭时出现异常，先不抛出
                LogUtils.e(sTag, e.getMessage());
            }
        }
    }

    public String getString(String key) {
        return config.get(key);
    }

    public ConcurrentMap<String, String> getConfig() {
        return config;
    }

    public Environment getEnvironment() {
        return this.mEnv;
    }

    public Bundle getMetaData() {
        return metaData;
    }

    /**
     * 环境
     */
    public static enum Environment {
        /**
         * 线上
         */
        PRODUCT("product-env.properties", "线上"),
        /**
         * 测试
         */
        TEST("test-env.properties", "测试"),

        /**
         * 预发布环境
         */
        PRE_PRODUCT("pre-env.properties", "预发布");

        private String filePath;
        private String showName;

        Environment(String path, String showName) {
            this.filePath = path;
            this.showName = showName;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public String getFilePath() {
            return this.filePath;
        }
    }

    /**
     * 是否是 测试环境
     * 我要重命名 你们等着!
     *
     * @return
     */
    public boolean isTest() {
        return StringUtils.equals(envFlg, Environment.TEST.name());
    }

    /**
     * 是否是开发环境
     *
     * @return
     */
    public boolean isDebug() {
        return (0 != (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
    }

    public static boolean isDebug(Context context) {
        return (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
    }

    public Environment getCurrentEnv() {
        return mEnv;
    }
}
