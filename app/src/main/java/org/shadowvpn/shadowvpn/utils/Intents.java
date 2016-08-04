package org.shadowvpn.shadowvpn.utils;

import android.content.Context;
import android.content.Intent;

import org.shadowvpn.shadowvpn.model.VpnConfigure;
import org.shadowvpn.shadowvpn.ui.activity.ConfigureEditActivity;

public class Intents {
    public static void addShadowVPNConfigure(Context context) {
        Intent intent = new Intent(context, ConfigureEditActivity.class);
        context.startActivity(intent);
    }

    public static void editShadowVPNConfigure(Context context, VpnConfigure vpnConfigure) {
        Intent intent = new Intent(context, ConfigureEditActivity.class);
        intent.putExtra(ConfigureEditActivity.EXTRA_TITLE, vpnConfigure.getTitle());
        context.startActivity(intent);
    }
}
