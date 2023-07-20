package com.wave_chtj.example.network;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chtj.base_framework.network.FLteTools;
import com.chtj.base_framework.network.NetDbmListener;
import com.face_chtj.base_iotutils.KLog;
import com.face_chtj.base_iotutils.NetMonitorUtils;
import com.face_chtj.base_iotutils.NetUtils;
import com.face_chtj.base_iotutils.ObjectUtils;
import com.face_chtj.base_iotutils.TimeUtils;
import com.face_chtj.base_iotutils.ToastUtils;
import com.face_chtj.base_iotutils.callback.INetChangeCallBack;
import com.face_chtj.base_iotutils.entity.DnsBean;
import com.wave_chtj.example.R;
import com.wave_chtj.example.base.BaseActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Create on 2020/1/3
 * author chtj
 * desc 网络监听
 */
public class NetChangeAty extends BaseActivity implements INetChangeCallBack {
    private static final String TAG = "NetChangeAty";
    private TextView tvStatus;
    private TextView tvType;
    private TextView tvDbm;
    private TextView tvDns;
    private TextView tvDnsList;
    private TextView tvCmd;
    private TextView tvResult;
    private Button btnClear;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        btnClear = findViewById(R.id.btnClear);
        tvCmd = findViewById(R.id.tvCmd);
        tvResult = findViewById(R.id.tvResult);
        tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvDnsList = findViewById(R.id.tvDnsList);
        tvDns = findViewById(R.id.tvDns);
        tvDbm = findViewById(R.id.tvDbm);
        tvStatus = findViewById(R.id.tvStatus);
        tvType = findViewById(R.id.tvType);
        if (NetUtils.getNetWorkType() == NetUtils.NETWORK_NO) {
            ToastUtils.error("当前无网络连接！");
            tvType.setText("无");
            tvStatus.setText("false");
        }
        FLteTools.init();
        NetMonitorUtils.register();
        NetMonitorUtils.addCallBack(this);
        handler.postDelayed(runnable, 0);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshNet();
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetMonitorUtils.removeCallback(this);
        NetMonitorUtils.unRegister();
        handler.removeCallbacks(runnable);
    }

    public void getDbmClick(View view) {
        tvDbm.setText("信号值：" + FLteTools.getDbm());
    }

    public void refreshNetClick(View view) {
        refreshNet();
    }

    private void refreshNet() {
        DnsBean dnsBean = NetUtils.checkNetWork();
        //KLog.d("refreshNet>> " + dnsBean.toString());
        String netWorkTypeName = NetUtils.getNetWorkTypeName();
        tvStatus.setText(dnsBean.isPass + " ttl=" + dnsBean.ttl + " time=" + dnsBean.delay + " ms");
        tvDns.setText(dnsBean.dns);
        tvType.setText(netWorkTypeName);
        tvDnsList.setText("可用DNS列表：" + Arrays.toString(NetUtils.getConvertDns()));
        tvCmd.setText(dnsBean.cmd);
        showData(dnsBean.toString());
    }

    @Override
    public void netChange(int netType, boolean pingResult) {
        //isNormal 网络经过ping后 true为网络正常 false为网络异常
        String netTypeName = NetUtils.convertNetTypeName(netType);
        tvType.setText("" + netTypeName);
        tvStatus.setText("" + pingResult);
        tvDbm.setText("信号值：" + FLteTools.getDbm());
    }

    public void clearClick(View view) {
        tvResult.setText("");
    }


    /**
     * 显示数据到UI
     *
     * @param htmlStr
     */
    public void showData(String htmlStr) {
        tvResult.append(Html.fromHtml(TimeUtils.getTodayDateHms("yy-MM-dd HH:mm:ss") + "：" + htmlStr));
        tvResult.append("\n");
        //刷新最新行显示
        int offset = tvResult.getLineCount() * tvResult.getLineHeight();
        int tvHeight = tvResult.getHeight();
        if (offset > 6000) {
            tvResult.setText("");
            tvResult.scrollTo(0, 0);
        } else {
            if (offset > tvHeight) {
                //Log.d(TAG, "showData: offset >> " + offset + " ,tvHeight >> " + tvHeight);
                tvResult.scrollTo(0, offset - tvHeight);
            }
        }
    }

}
