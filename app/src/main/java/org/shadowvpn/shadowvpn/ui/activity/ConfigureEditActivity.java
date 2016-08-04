package org.shadowvpn.shadowvpn.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.shadowvpn.shadowvpn.R;
import org.shadowvpn.shadowvpn.ui.fragment.ConfigureEditFragment;

public class ConfigureEditActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_edit);

        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            if (extras == null) {
                Fragment editFragment = ConfigureEditFragment.newInstance();

                getSupportFragmentManager().beginTransaction().add(R.id.container, editFragment).commit();
            } else {
                String title = extras.getString(ConfigureEditActivity.EXTRA_TITLE);

                Fragment editFragment = ConfigureEditFragment.newInstance(title);

                getSupportFragmentManager().beginTransaction().add(R.id.container, editFragment).commit();
            }
        }
    }
}