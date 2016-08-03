package org.shadowvpn.shadowvpn;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

public class ShadowVPN {
    private static final int DEFAULT_MAXIMUM_TRANSMISSION_UNITS = 1432;
    private static final int DEFAULT_CONCURRENCY = 1;

    private ParcelFileDescriptor mTUNFileDescriptor;
    private String mPassword;
    private String mUserToken;
    private String mServer;
    private int mPort;
    private int mMaximumTransmissionUnits;
    private int mConcurrency;
    private boolean mIsRunning;

    public ShadowVPN(ParcelFileDescriptor TUNFileDescriptor, String password, String userToken, String server, int port) {
        this(TUNFileDescriptor, password, userToken, server, port, ShadowVPN.DEFAULT_MAXIMUM_TRANSMISSION_UNITS, ShadowVPN.DEFAULT_CONCURRENCY);
    }

    public ShadowVPN(ParcelFileDescriptor TUNFileDescriptor, String password, String userToken, String server, int port, int maximumTransmissionUnits, int concurrency) {
        mTUNFileDescriptor = TUNFileDescriptor;
        mPassword = password;
        mUserToken = userToken;
        mServer = server;
        mPort = port;
        mMaximumTransmissionUnits = maximumTransmissionUnits;
        mConcurrency = concurrency;
    }

    public void init() throws IOException {
        if (nativeInitVPN(mTUNFileDescriptor.getFd(), mPassword, mUserToken, mServer, mPort, mMaximumTransmissionUnits, mConcurrency) != 0) {
            throw new IOException("Failed to create ShadowVPN");
        }
    }

    public void start() {
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        nativeRunVPN();
        mIsRunning = false;
    }

    public void shouldStop() {
        mIsRunning = false;
        nativeStopVPN();
        try {
            mTUNFileDescriptor.close();
        } catch (IOException ioException) {
            Log.e(ShadowVPN.class.getSimpleName(), "", ioException);
        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public int getSockFileDescriptor() {
        return nativeGetSockFd();
    }

    protected native int nativeInitVPN(int TUNFileDescriptor, String password, String userToken, String server, int port, int maximumTransmissionUnits, int concurrency);

    protected native int nativeRunVPN();

    protected native int nativeStopVPN();

    protected native int nativeGetSockFd();

    static {
        System.loadLibrary("vpn");
    }
}
