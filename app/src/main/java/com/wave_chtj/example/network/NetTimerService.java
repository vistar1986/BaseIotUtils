package com.wave_chtj.example.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.chtj.base_framework.FBaseTools;
import com.chtj.base_framework.network.FLteTools;
import com.face_chtj.base_iotutils.DeviceUtils;
import com.face_chtj.base_iotutils.FileUtils;
import com.face_chtj.base_iotutils.KLog;
import com.face_chtj.base_iotutils.NetUtils;
import com.face_chtj.base_iotutils.SPUtils;
import com.face_chtj.base_iotutils.TimeUtils;
import com.face_chtj.base_iotutils.ToastUtils;
import com.face_chtj.base_iotutils.entity.DnsBean;
import com.wave_chtj.example.callback.INetTimerCallback;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NetTimerService extends Service {
    static Disposable disposable;
    static INetTimerCallback iNetTimerCallback;
    public NetTimerBinder netTimerBinder = new NetTimerBinder();
    public static final String KEY_ERRCOUNT = "errCount";
    public static final String KEY_SUCCCOUNT = "succCount";
    public static final String SAVE_PATH="/sdcard/DCIM/nettimer.log";
    public String[] pingDns=new String[]{"223.6.6.6"};

    public void setiNetTimerCallback(INetTimerCallback iNetTimerCallback) {
        this.iNetTimerCallback = iNetTimerCallback;
    }

    class NetTimerBinder extends Binder {
        public NetTimerService getService() {
            return NetTimerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return netTimerBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FLteTools.init();
        startNetCheck();
    }

    public void startNetCheck() {
        if (disposable == null) {
            KLog.d("startNetCheck() >> ");
            disposable = Observable.interval(0, 5, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())//调用切换之前的线程。
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            List<DnsBean> dnsBeanList = NetUtils.checkNetWork(pingDns);
                            boolean isPingResult=false;
                            for (int i = 0; i < dnsBeanList.size(); i++) {
                                if(dnsBeanList.get(i).isPass){
                                    isPingResult=true;
                                    break;
                                }
                            }
                            //boolean isPingResult=NetUtils.ping(pingDns[0],3,5,0,1024);
                            putCount(isPingResult);
                            String netType = NetUtils.getNetWorkTypeName();
                            String time = TimeUtils.getTodayDateHms("yyyy-MM-dd HH:mm:ss");
                            boolean isNet4G = NetUtils.is4G();
                            String dbm = FLteTools.getDbm();
                            String localIp = DeviceUtils.getLocalIp();
                            FileUtils.writeFileData(SAVE_PATH,
                                    "\ntime：" + time +", dns：" + Arrays.toString(pingDns) + ", netType：" + netType + ", isNet4G=" + NetUtils.is4G() + ", pingResult：" + isPingResult + ", dbm：" + dbm+ ", localIp：" + localIp, false);
                            if (iNetTimerCallback != null) {
                                iNetTimerCallback.refreshNet(time,pingDns, dbm,localIp, netType, isNet4G,
                                        /*dnsBeanList.get(0).isPass*/isPingResult);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            KLog.d("accept() >> " + throwable.getMessage());
                            cancel();
                            startNetCheck();
                        }
                    });
        } else {
            ToastUtils.success("已开启");
        }
    }

    public synchronized void putCount(boolean pingResult){
        if (pingResult) {
            SPUtils.putInt(KEY_SUCCCOUNT, SPUtils.getInt(KEY_SUCCCOUNT, 0) + 1);
        } else {
            SPUtils.putInt(KEY_ERRCOUNT, SPUtils.getInt(KEY_ERRCOUNT, 0) + 1);
        }
    }

    public int getErrCount() {
        return SPUtils.getInt(KEY_ERRCOUNT, 0);
    }

    public int getSuccCount() {
        return SPUtils.getInt(KEY_SUCCCOUNT, 0);
    }

    public synchronized void clearCount() {
        SPUtils.putInt(KEY_ERRCOUNT, 0);
        SPUtils.putInt(KEY_SUCCCOUNT, 0);
        ToastUtils.success("清除成功！");
    }

    public void cancel() {
        if (disposable != null) {
            KLog.d("cancel() >> ");
            try {
                disposable.dispose();
            } catch (Throwable e) {
                e.printStackTrace();
                KLog.e("errMeg:" + e.getMessage());
            }
        }
        disposable = null;
        ToastUtils.success("关闭成功！");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FLteTools.cancel();
    }
}
