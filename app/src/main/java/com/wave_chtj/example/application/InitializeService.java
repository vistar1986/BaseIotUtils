package com.wave_chtj.example.application;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chtj.base_iotutils.KLog;
import com.chtj.base_iotutils.keepservice.BaseIotUtils;
import com.chtj.base_iotutils.screen_adapta.activitylifecycle.SCREEN_TYPE;

/**
 * Create on 2019/10/16
 * author chtj
 * desc 启动app时的优化
 */
public class InitializeService extends IntentService {
    public static final String TAG="InitializeService";
    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.anly.githubapp.service.action.INIT";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        KLog.d("performInit begin:" + System.currentTimeMillis());
        //需要在 Application 的 onCreate() 中调用一次 BaseIotTools.instance()....
        //setBaseWidth setBaseHeight 是为了适配而去设置相关的值
        BaseIotUtils.instance().
                setBaseWidth(1080).//设置宽度布局尺寸 layout 布局文件以pt为单位
                setBaseHeight(1920).//设置高度布局尺寸 layout 布局文件以pt为单位
                setCreenType(SCREEN_TYPE.HEIGHT).//按照高度适配
                setAutoScreenAdaptation(true).//开启自动适配 true 开启  false关闭
                create(getApplication());

    }
}