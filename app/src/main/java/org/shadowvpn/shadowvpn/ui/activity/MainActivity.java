package org.shadowvpn.shadowvpn.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.model.ShadowVPNConfigure;
import org.shadowvpn.shadowvpn.service.ShadowVPNService;
import org.shadowvpn.shadowvpn.service.ShadowVPNService.ShadowVPNServiceBinder;
import org.shadowvpn.shadowvpn.ui.fragment.ShadowVPNListFragment;
import org.shadowvpn.shadowvpn.ui.fragment.ShadowVPNListFragment.IOnFragmentInteractionListener;
import org.shadowvpn.shadowvpn.utils.Intents;
import org.shadowvpn.shadowvpn.utils.ShadowVPNConfigureHelper;

public class MainActivity extends AppCompatActivity implements IOnFragmentInteractionListener, ServiceConnection {
    private static final int REQUEST_CODE_VPN_PREPARE = 1;
    private ShadowVPNService mShadowVPNService;
    private ShadowVPNConfigure mCurrentSelectedShadowVPNConfigure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, ShadowVPNListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, ShadowVPNService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, ShadowVPNService.class);
        bindService(intent, this, Context.BIND_ABOVE_CLIENT);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_vpn_add:
                Intents.addShadowVPNConfigure(this);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onShadowVPNConfigureClick(ShadowVPNConfigure shadowVPNConfigure) {
        mCurrentSelectedShadowVPNConfigure = shadowVPNConfigure;

        if (!mCurrentSelectedShadowVPNConfigure.isSelected()) {
            prepareShadowVPN();
        }
    }

    @Override
    public void onShadowVPNConfigureStop(ShadowVPNConfigure shadowVPNConfigure) {
        if (mShadowVPNService != null) {
            mShadowVPNService.stopVPN();
        }
    }

    @Override
    public void onShadowVPNConfigureEdit(ShadowVPNConfigure shadowVPNConfigure) {
        Intents.editShadowVPNConfigure(this, shadowVPNConfigure);
    }

    @Override
    public void onShadowVPNConfigureDelete(ShadowVPNConfigure shadowVPNConfigure) {
        ShadowVPNConfigureHelper.delete(this, shadowVPNConfigure.getTitle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.REQUEST_CODE_VPN_PREPARE && resultCode == MainActivity.RESULT_OK) {
            startShadowVPN();
        }
    }

    public void prepareShadowVPN() {
        Intent intent = ShadowVPNService.prepare(this);

        if (intent != null) {
            startActivityForResult(intent, MainActivity.REQUEST_CODE_VPN_PREPARE);
        } else {
            startShadowVPN();
        }
    }

    private void startShadowVPN() {
        if (mCurrentSelectedShadowVPNConfigure != null) {
            Intent intent = new Intent(this, ShadowVPNService.class);
            intent.putExtra(ShadowVPNService.EXTRA_VPN_TITLE, mCurrentSelectedShadowVPNConfigure.getTitle());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_SERVER_IP, mCurrentSelectedShadowVPNConfigure.getServerIP());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_PORT, mCurrentSelectedShadowVPNConfigure.getPort());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_PASSWORD, mCurrentSelectedShadowVPNConfigure.getPassword());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_USER_TOKEN, mCurrentSelectedShadowVPNConfigure.getUserToken());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_LOCAL_IP, mCurrentSelectedShadowVPNConfigure.getLocalIP());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_MAXIMUM_TRANSMISSION_UNITS, mCurrentSelectedShadowVPNConfigure.getMaximumTransmissionUnits());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_CONCURRENCY, mCurrentSelectedShadowVPNConfigure.getConcurrency());
            intent.putExtra(ShadowVPNService.EXTRA_VPN_BYPASS_CHINA_ROUTES, mCurrentSelectedShadowVPNConfigure.isBypassChinaRoutes());

            startService(intent);
            bindService(intent, this, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ShadowVPNServiceBinder binder = (ShadowVPNServiceBinder) iBinder;
        mShadowVPNService = binder.getShadowVPNService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mShadowVPNService = null;
    }
}