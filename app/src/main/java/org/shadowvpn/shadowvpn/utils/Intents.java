package org.shadowvpn.shadowvpn.utils;

import android.content.Context;
import android.content.Intent;

import org.shadowvpn.shadowvpn.model.ShadowVPNConfigure;
import org.shadowvpn.shadowvpn.ui.activity.ShadowVPNConfigureEditActivity;

public class Intents {
    public static void addShadowVPNConfigure(Context context) {
        Intent intent = new Intent(context, ShadowVPNConfigureEditActivity.class);
        context.startActivity(intent);
    }

    public static void editShadowVPNConfigure(Context context, ShadowVPNConfigure shadowVPNConfigure) {
        Intent intent = new Intent(context, ShadowVPNConfigureEditActivity.class);
        intent.putExtra(ShadowVPNConfigureEditActivity.EXTRA_TITLE, shadowVPNConfigure.getTitle());
        context.startActivity(intent);
    }
}
