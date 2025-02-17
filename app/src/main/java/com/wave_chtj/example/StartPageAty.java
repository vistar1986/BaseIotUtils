package com.wave_chtj.example;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.wave_chtj.example.base.BaseActivity;
import com.wave_chtj.example.install.InstallAPkAty;
import com.wave_chtj.example.network.NetMonitorAty;
import com.wave_chtj.example.network.NetTimerAty;
import com.wave_chtj.example.reboot.RebootAty;
import com.wave_chtj.example.serialport.SerialPortAty;
import com.wave_chtj.example.test.TestAty;
import com.wave_chtj.example.util.PACKAGES;

/**
 * Create on 2019/10/16
 * author chtj
 * desc $ 启动页
 */
public class StartPageAty extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (BuildConfig.APPLICATION_ID) {
            case PACKAGES.PKG_SERIALPORT:
                startAty(SerialPortAty.class);
                break;
            case PACKAGES.PKG_REBOOT:
                startAty(RebootAty.class);
                break;
            case PACKAGES.PKG_NETMONITOR:
                startAty(NetMonitorAty.class);
                break;
            case PACKAGES.PKG_NETTIMER:
            case PACKAGES.PKG_NETTIMER1:
                startAty(NetTimerAty.class);
                break;
            case PACKAGES.PKG_ZTOCABINET:
            case PACKAGES.PKG_CABINET:
                startAty(TestAty.class);
                break;
            case PACKAGES.PKG_INTENT_INSTALL:
                startAty(InstallAPkAty.class);
                break;
            case PACKAGES.PKG_EXAMPLE:
            default:
                startAty(OptionAty.class);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
