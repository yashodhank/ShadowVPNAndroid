package org.shadowvpn.shadowvpn.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marvinlabs.widget.floatinglabel.edittext.FloatingLabelEditText;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.model.ShadowVPNConfigure;
import org.shadowvpn.shadowvpn.utils.ShadowVPNConfigureHelper;

public class ShadowVPNConfigureEditFragment extends Fragment {
    private static final String KEY_TITLE = "key_title";

    public static ShadowVPNConfigureEditFragment newInstance() {
        return ShadowVPNConfigureEditFragment.newInstance(null);
    }

    public static ShadowVPNConfigureEditFragment newInstance(String title) {
        ShadowVPNConfigureEditFragment fragment = new ShadowVPNConfigureEditFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ShadowVPNConfigureEditFragment.KEY_TITLE, title);
        fragment.setArguments(arguments);

        return fragment;
    }

    private String mTitle;
    private FloatingLabelEditText mTitleText;
    private FloatingLabelEditText mServerIPText;
    private FloatingLabelEditText mPortText;
    private FloatingLabelEditText mPasswordText;
    private FloatingLabelEditText mUserTokenText;
    private FloatingLabelEditText mLocalIPText;
    private FloatingLabelEditText mMaximumTransmissionUnitsText;
    private FloatingLabelEditText mConcurrency;
    private SwitchCompat mBypassChinaRoutesSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mTitle = getArguments().getString(ShadowVPNConfigureEditFragment.KEY_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_shadow_vpn_configure_edit, container, false);

        mTitleText = (FloatingLabelEditText) view.findViewById(R.id.text_title);
        mServerIPText = (FloatingLabelEditText) view.findViewById(R.id.text_server_ip);
        mPortText = (FloatingLabelEditText) view.findViewById(R.id.text_port);
        mPasswordText = (FloatingLabelEditText) view.findViewById(R.id.text_password);
        mUserTokenText = (FloatingLabelEditText) view.findViewById(R.id.text_user_token);
        mLocalIPText = (FloatingLabelEditText) view.findViewById(R.id.text_local_ip);
        mMaximumTransmissionUnitsText = (FloatingLabelEditText) view.findViewById(R.id.text_maximum_transmission_units);
        mConcurrency = (FloatingLabelEditText) view.findViewById(R.id.text_concurrency);
        mBypassChinaRoutesSwitch = (SwitchCompat) view.findViewById(R.id.switch_bypass_china_routes);

        if (TextUtils.isEmpty(mTitle)) {
            mPortText.setInputWidgetText(String.valueOf(0));
            mLocalIPText.setInputWidgetText(ShadowVPNConfigureHelper.DEFAULT_LOCAL_IP);
            mMaximumTransmissionUnitsText.setInputWidgetText(String.valueOf(ShadowVPNConfigureHelper.DEFAULT_MAXIMUM_TRANSMISSION_UNITS));
            mConcurrency.setInputWidgetText(String.valueOf(ShadowVPNConfigureHelper.DEFAULT_CONCURRENCY));
        } else {
            ShadowVPNConfigure configure = ShadowVPNConfigureHelper.exists(getActivity(), mTitle);

            mTitleText.setInputWidgetText(configure.getTitle());
            mServerIPText.setInputWidgetText(configure.getServerIP());
            mPortText.setInputWidgetText(String.valueOf(configure.getPort()));
            mPasswordText.setInputWidgetText(configure.getPassword());
            mUserTokenText.setInputWidgetText(configure.getUserToken());
            mLocalIPText.setInputWidgetText(configure.getLocalIP());
            mMaximumTransmissionUnitsText.setInputWidgetText(String.valueOf(configure.getMaximumTransmissionUnits()));
            mConcurrency.setInputWidgetText(String.valueOf(configure.getConcurrency()));
            mBypassChinaRoutesSwitch.setChecked(configure.isBypassChinaRoutes());
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fragment_shadow_vpn_configure_edit, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_delete).setVisible(!TextUtils.isEmpty(mTitle));
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (TextUtils.isEmpty(mTitle)) {
                    if (createShadowVPNConfigure()) {
                        getActivity().finish();
                    }
                } else {
                    if (updateShadowVPNConfigure()) {
                        getActivity().finish();
                    }
                }
                return true;
            case R.id.menu_discard:
                getActivity().finish();
                return true;
            case R.id.menu_delete:
                ShadowVPNConfigureHelper.delete(getActivity(), mTitle);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private boolean createShadowVPNConfigure() {
        boolean inputResult = checkInput();

        if (inputResult) {
            boolean existsResult = checkConfigureExists();

            if (!existsResult) {
                String title = mTitleText.getInputWidgetText().toString();
                String serverIP = mServerIPText.getInputWidgetText().toString();
                int port = Integer.parseInt(mPortText.getInputWidgetText().toString());
                String password = mPasswordText.getInputWidgetText().toString();
                String userToken = mUserTokenText.getInputWidgetText().toString();
                String localIP = mLocalIPText.getInputWidgetText().toString();
                int maximumTransmissionUnits = Integer.parseInt(mMaximumTransmissionUnitsText.getInputWidgetText().toString());
                int concurrency = Integer.parseInt(mConcurrency.getInputWidgetText().toString());
                boolean bypassChinaRoutes = mBypassChinaRoutesSwitch.isChecked();

                ShadowVPNConfigureHelper.create(getActivity(), title, serverIP, port, password, userToken, localIP, maximumTransmissionUnits, concurrency, bypassChinaRoutes);

                return true;
            }
        }

        return false;
    }

    private boolean updateShadowVPNConfigure() {
        boolean inputResult = checkInput();

        if (inputResult) {
            ShadowVPNConfigure shadowVPNConfigure = ShadowVPNConfigureHelper.exists(getActivity(), mTitle);

            String title = mTitleText.getInputWidgetText().toString();
            String serverIP = mServerIPText.getInputWidgetText().toString();
            int port = Integer.parseInt(mPortText.getInputWidgetText().toString());
            String password = mPasswordText.getInputWidgetText().toString();
            String userToken = mUserTokenText.getInputWidgetText().toString();
            String localIP = mLocalIPText.getInputWidgetText().toString();
            int maximumTransmissionUnits = Integer.parseInt(mMaximumTransmissionUnitsText.getInputWidgetText().toString());
            int concurrency = Integer.parseInt(mConcurrency.getInputWidgetText().toString());
            boolean bypassChinaRoutes = mBypassChinaRoutesSwitch.isChecked();

            ShadowVPNConfigureHelper.update(getActivity(), shadowVPNConfigure, title, serverIP, port, password, userToken, localIP, maximumTransmissionUnits, concurrency, bypassChinaRoutes, shadowVPNConfigure.isSelected());
        }

        return inputResult;
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mTitleText.getInputWidgetText().toString())) {
            Toast.makeText(getActivity(), R.string.toast_vpn_configure_title_null, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(mServerIPText.getInputWidgetText().toString())) {
            Toast.makeText(getActivity(), R.string.toast_vpn_configure_server_ip_null, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(mPortText.getInputWidgetText().toString())) {
            Toast.makeText(getActivity(), R.string.toast_vpn_configure_port_null, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(mPasswordText.getInputWidgetText().toString())) {
            Toast.makeText(getActivity(), R.string.toast_vpn_configure_password_null, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(mLocalIPText.getInputWidgetText().toString())) {
            Toast.makeText(getActivity(), R.string.toast_vpn_configure_local_ip_null, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(mMaximumTransmissionUnitsText.getInputWidgetText().toString())) {
            Toast.makeText(getActivity(), R.string.toast_vpn_configure_maximum_transmission_units_null, Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    private boolean checkConfigureExists() {
        String title = mTitleText.getInputWidgetText().toString();

        ShadowVPNConfigure configure = ShadowVPNConfigureHelper.exists(getActivity(), title);

        if (configure != null) {
            Toast.makeText(getActivity(), getString(R.string.toast_vpn_configure_exists, title), Toast.LENGTH_SHORT).show();

            return true;
        } else {
            return false;
        }
    }
}