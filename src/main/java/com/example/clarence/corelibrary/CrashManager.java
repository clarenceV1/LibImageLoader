package com.example.clarence.corelibrary;


import android.content.Context;
import android.os.Environment;
import android.os.Looper;

import com.example.clarence.utillibrary.FileUtils;
import com.example.clarence.utillibrary.LogUtils;
import com.example.clarence.utillibrary.PackageUtils;
import com.example.clarence.utillibrary.ToastUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。
 * 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框
 * 实现该接口并注册为程序中的默认未捕获异常处理
 * 这样当未捕获异常发生时，就可以做些异常处理操作
 * 例如：收集异常信息，发送错误报告 等。
 * <p/>
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class CrashManager implements UncaughtExceptionHandler {
    /**
     * Debug Log Tag
     */
    public static final String TAG = "CrashManager";
    /**
     * 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能
     */
    public static final boolean DEBUG = true;
    /**
     * CrashHandler实例
     */
    private static CrashManager INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    //private Properties mDeviceCrashInfo = new Properties();

    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashManager() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashManager();
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtils.e(TAG, "Error : " + e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        try {
            if (ex == null) {
                return true;
            }
            ex.printStackTrace();
            showToastForUser();
            sendCrashToServer(mContext, ex);
            saveCrashInfoToFile(ex);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    /**
     * 提示用户异常
     */
    private void showToastForUser() {
        // TODO: 等待优化，统一管理线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                ToastUtils.showToast(mContext,R.string.module_core_crash_notice);
            }
        }.start();
    }

    /**
     * 发送到服务端
     *
     * @param context
     * @param ex
     */
    private void sendCrashToServer(Context context, Throwable ex) {
        // TODO  undo
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private void saveCrashInfoToFile(Throwable ex) {
        try {
            Writer info = new StringWriter();
            PrintWriter printWriter = new PrintWriter(info);
            // printStackTrace(PrintWriter s)
            // 将此 throwable 及其追踪输出到指定的 PrintWriter
            ex.printStackTrace(printWriter);
            // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }

            // toString() 以字符串的形式返回该缓冲区的当前值。
            String result = info.toString();
            printWriter.close();
            StringBuilder stringBuilder = new StringBuilder();
            //time
            Calendar calendar = (Calendar) Calendar.getInstance().clone();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = simpleDateFormat.format(calendar.getTime());
            stringBuilder.append("crash_log_time:").append(time).append("\n").append("crash_log:").append("\n").append(result).append("\n");

            //path filename
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/meiyou";
            String filename = PackageUtils.getPackageInfo(mContext).packageName + "-" + time + ".log";
            //incase too many log
            try {
                File file = new File(path);
                file.mkdir();
                File[] files = file.listFiles();
                if (files != null && files.length > 5) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //save to dir
            FileUtils.writeStringToFile(path, filename, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
