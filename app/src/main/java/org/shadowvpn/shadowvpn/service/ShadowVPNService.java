package org.shadowvpn.shadowvpn.service;

import android.app.Service;
import android.content.Intent;
import android.net.VpnService;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.ShadowVPN;
import org.shadowvpn.shadowvpn.utils.ShadowVPNConfigureHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShadowVPNService extends VpnService {
    public static final String EXTRA_VPN_TITLE = "extra_vpn_title";
    public static final String EXTRA_VPN_SERVER_IP = "extra_vpn_server_ip";
    public static final String EXTRA_VPN_PORT = "extra_vpn_port";
    public static final String EXTRA_VPN_PASSWORD = "extra_vpn_password";
    public static final String EXTRA_VPN_USER_TOKEN = "extra_vpn_user_token";
    public static final String EXTRA_VPN_LOCAL_IP = "extra_vpn_local_ip";
    public static final String EXTRA_VPN_MAXIMUM_TRANSMISSION_UNITS = "extra_vpn_maximum_transmission_units";
    public static final String EXTRA_VPN_CONCURRENCY = "extra_vpn_concurrency";
    public static final String EXTRA_VPN_BYPASS_CHINA_ROUTES = "extra_vpn_bypass_china_routes";

    private final IBinder mBinder = new ShadowVPNServiceBinder();
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private ShadowVPN mShadowVPN;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            ShadowVPNService.this.onHandleIntent((Intent) msg.obj);

            ShadowVPNService.this.stopSelf(msg.arg1);
        }
    }

    public final class ShadowVPNServiceBinder extends Binder {
        public ShadowVPNService getShadowVPNService() {
            return ShadowVPNService.this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == IBinder.LAST_CALL_TRANSACTION) {
                ShadowVPNService.this.onRevoke();

                return true;
            }

            return super.onTransact(code, data, reply, flags);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("ShadowVPNService");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message message = mServiceHandler.obtainMessage();
        message.obj = intent;
        message.arg1 = startId;

        mServiceHandler.sendMessage(message);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras == null) {
            return;
        }

        stopVPN();

        String title = extras.getString(ShadowVPNService.EXTRA_VPN_TITLE);
        String serverIP = extras.getString(ShadowVPNService.EXTRA_VPN_SERVER_IP);
        int port = extras.getInt(ShadowVPNService.EXTRA_VPN_PORT);
        String password = extras.getString(ShadowVPNService.EXTRA_VPN_PASSWORD);
        String userToken = extras.getString(ShadowVPNService.EXTRA_VPN_USER_TOKEN);
        String localIP = extras.getString(ShadowVPNService.EXTRA_VPN_LOCAL_IP);
        int maximumTransmissionUnits = extras.getInt(ShadowVPNService.EXTRA_VPN_MAXIMUM_TRANSMISSION_UNITS);
        int concurrency = extras.getInt(ShadowVPNService.EXTRA_VPN_CONCURRENCY);
        boolean bypassChinaRoutes = extras.getBoolean(ShadowVPNService.EXTRA_VPN_BYPASS_CHINA_ROUTES);

        Builder builder = new Builder();
        builder.addAddress(localIP, 24);
        setupShadowVPNRoute(builder, bypassChinaRoutes);
        builder.addDnsServer("8.8.8.8");
        builder.addDnsServer("8.8.4.4");
        builder.setSession(getString(R.string.app_name) + "[" + title + "]");

        ParcelFileDescriptor fileDescriptor = builder.establish();

        if (fileDescriptor == null) {
            return;
        }

        mShadowVPN = new ShadowVPN(fileDescriptor, password, userToken, serverIP, port, maximumTransmissionUnits, concurrency);

        try {
            mShadowVPN.init();
        } catch (IOException e) {
            Log.e(ShadowVPNService.class.getSimpleName(), "", e);
        }

        protect(mShadowVPN.getSockFileDescriptor());

        ShadowVPNConfigureHelper.selectShadowVPNConfigure(this, title);

        mShadowVPN.start();
    }

    private void setupShadowVPNRoute(Builder builder, boolean bypassChinaRoutes) {
        if (bypassChinaRoutes) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.foreign)));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    final String[] route = line.split("/");

                    if (route.length == 2) {
                        builder.addRoute(route[0], Integer.parseInt(route[1]));
                    }
                }
            } catch (Throwable throwable) {
                Log.e(ShadowVPNService.class.getSimpleName(), "", throwable);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        } else {
            builder.addRoute("0.0.0.0", 0);
        }
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();

        ShadowVPNConfigureHelper.resetAllSelectedValue(this);

        stopVPN();
    }

    @Override
    public void onRevoke() {
        super.onRevoke();

        stopVPN();
    }

    public boolean isShadowVPNRunning() {
        return mShadowVPN != null && mShadowVPN.isRunning();
    }

    public void stopVPN() {
        if (mShadowVPN != null && mShadowVPN.isRunning()) {
            mShadowVPN.shouldStop();
        }
    }
}
