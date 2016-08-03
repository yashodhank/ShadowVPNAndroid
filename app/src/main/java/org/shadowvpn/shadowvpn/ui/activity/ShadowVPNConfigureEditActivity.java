package org.shadowvpn.shadowvpn.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.ui.fragment.ShadowVPNConfigureEditFragment;

public class ShadowVPNConfigureEditActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow_vpn_configure_edit);

        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            if (extras == null) {
                Fragment editFragment = ShadowVPNConfigureEditFragment.newInstance();

                getSupportFragmentManager().beginTransaction().add(R.id.container, editFragment).commit();
            } else {
                String title = extras.getString(ShadowVPNConfigureEditActivity.EXTRA_TITLE);

                Fragment editFragment = ShadowVPNConfigureEditFragment.newInstance(title);

                getSupportFragmentManager().beginTransaction().add(R.id.container, editFragment).commit();
            }
        }
    }
}