package org.shadowvpn.shadowvpn.utils;

import android.content.Context;

import org.shadowvpn.shadowvpn.model.VpnConfigure;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ConfigureHelper {
    public static final String DEFAULT_LOCAL_IP = "10.7.0.2";
    public static final int DEFAULT_MAXIMUM_TRANSMISSION_UNITS = 1432;
    public static final int DEFAULT_CONCURRENCY = 1;

    public static VpnConfigure create(Context context, String title, String serverIP, int port, String password, String userToken, String localIP, int maximumTransmissionUnits, int concurrency, boolean bypassChinaRoutes) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        VpnConfigure configure = realm.createObject(VpnConfigure.class);
        configure.setTitle(title);
        configure.setServerIP(serverIP);
        configure.setPort(port);
        configure.setPassword(password);
        configure.setUserToken(userToken);
        configure.setLocalIP(localIP);
        configure.setMaximumTransmissionUnits(maximumTransmissionUnits);
        configure.setConcurrency(concurrency);
        configure.setBypassChinaRoutes(bypassChinaRoutes);

        realm.commitTransaction();

        return configure;
    }

    public static void delete(Context context, String title) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        RealmQuery<VpnConfigure> shadowVPNConfigureRealmQuery = realm.where(VpnConfigure.class);
        shadowVPNConfigureRealmQuery.equalTo("title", title);

        shadowVPNConfigureRealmQuery.findAll().clear();

        realm.commitTransaction();
    }

    public static VpnConfigure exists(Context context, String title) {
        Realm realm = Realm.getInstance(context);

        RealmQuery<VpnConfigure> shadowVPNConfigureRealmQuery = realm.where(VpnConfigure.class);
        shadowVPNConfigureRealmQuery.equalTo("title", title);

        return shadowVPNConfigureRealmQuery.findFirst();
    }

    public static RealmResults<VpnConfigure> getAll(Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        RealmQuery<VpnConfigure> query = realm.where(VpnConfigure.class);
        RealmResults<VpnConfigure> configures = query.findAll();

        realm.commitTransaction();

        return configures;
    }

    public static VpnConfigure update(Context context, VpnConfigure vpnConfigure, String title, String serverIP, int port, String password, String userToken, String localIP, int maximumTransmissionUnits, int concurrency, boolean bypassChinaRoutes, boolean selected) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        vpnConfigure.setTitle(title);
        vpnConfigure.setServerIP(serverIP);
        vpnConfigure.setPort(port);
        vpnConfigure.setPassword(password);
        vpnConfigure.setUserToken(userToken);
        vpnConfigure.setLocalIP(localIP);
        vpnConfigure.setMaximumTransmissionUnits(maximumTransmissionUnits);
        vpnConfigure.setConcurrency(concurrency);
        vpnConfigure.setBypassChinaRoutes(bypassChinaRoutes);
        vpnConfigure.setSelected(selected);

        realm.commitTransaction();

        return vpnConfigure;
    }

    public static void selectShadowVPNConfigure(Context context, String title) {
        VpnConfigure configure = ConfigureHelper.exists(context, title);

        if (configure != null) {
            ConfigureHelper.update(context, configure, configure.getTitle(), configure.getServerIP(), configure.getPort(), configure.getPassword(), configure.getUserToken(), configure.getLocalIP(), configure.getMaximumTransmissionUnits(), configure.getConcurrency(), configure.isBypassChinaRoutes(), true);
        }
    }

    public static void resetAllSelectedValue(Context context) {
        RealmResults<VpnConfigure> vpnConfigureRealmResults = ConfigureHelper.getAll(context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        for (int i = 0; i < vpnConfigureRealmResults.size(); i++) {
            vpnConfigureRealmResults.get(i).setSelected(false);
        }

        realm.commitTransaction();
    }
}