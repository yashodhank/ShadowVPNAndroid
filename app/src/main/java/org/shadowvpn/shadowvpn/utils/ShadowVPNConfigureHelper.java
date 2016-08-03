package org.shadowvpn.shadowvpn.utils;

import android.content.Context;

import org.shadowvpn.shadowvpn.model.ShadowVPNConfigure;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ShadowVPNConfigureHelper {
    public static final String DEFAULT_LOCAL_IP = "10.7.0.2";
    public static final int DEFAULT_MAXIMUM_TRANSMISSION_UNITS = 1432;
    public static final int DEFAULT_CONCURRENCY = 1;

    public static ShadowVPNConfigure create(Context context, String title, String serverIP, int port, String password, String userToken, String localIP, int maximumTransmissionUnits, int concurrency, boolean bypassChinaRoutes) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        ShadowVPNConfigure configure = realm.createObject(ShadowVPNConfigure.class);
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

        RealmQuery<ShadowVPNConfigure> shadowVPNConfigureRealmQuery = realm.where(ShadowVPNConfigure.class);
        shadowVPNConfigureRealmQuery.equalTo("title", title);

        shadowVPNConfigureRealmQuery.findAll().clear();

        realm.commitTransaction();
    }

    public static ShadowVPNConfigure exists(Context context, String title) {
        Realm realm = Realm.getInstance(context);

        RealmQuery<ShadowVPNConfigure> shadowVPNConfigureRealmQuery = realm.where(ShadowVPNConfigure.class);
        shadowVPNConfigureRealmQuery.equalTo("title", title);

        return shadowVPNConfigureRealmQuery.findFirst();
    }

    public static RealmResults<ShadowVPNConfigure> getAll(Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        RealmQuery<ShadowVPNConfigure> query = realm.where(ShadowVPNConfigure.class);
        RealmResults<ShadowVPNConfigure> configures = query.findAll();

        realm.commitTransaction();

        return configures;
    }

    public static ShadowVPNConfigure update(Context context, ShadowVPNConfigure shadowVPNConfigure, String title, String serverIP, int port, String password, String userToken, String localIP, int maximumTransmissionUnits, int concurrency, boolean bypassChinaRoutes, boolean selected) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        shadowVPNConfigure.setTitle(title);
        shadowVPNConfigure.setServerIP(serverIP);
        shadowVPNConfigure.setPort(port);
        shadowVPNConfigure.setPassword(password);
        shadowVPNConfigure.setUserToken(userToken);
        shadowVPNConfigure.setLocalIP(localIP);
        shadowVPNConfigure.setMaximumTransmissionUnits(maximumTransmissionUnits);
        shadowVPNConfigure.setConcurrency(concurrency);
        shadowVPNConfigure.setBypassChinaRoutes(bypassChinaRoutes);
        shadowVPNConfigure.setSelected(selected);

        realm.commitTransaction();

        return shadowVPNConfigure;
    }

    public static void selectShadowVPNConfigure(Context context, String title) {
        ShadowVPNConfigure configure = ShadowVPNConfigureHelper.exists(context, title);

        if (configure != null) {
            ShadowVPNConfigureHelper.update(context, configure, configure.getTitle(), configure.getServerIP(), configure.getPort(), configure.getPassword(), configure.getUserToken(), configure.getLocalIP(), configure.getMaximumTransmissionUnits(), configure.getConcurrency(), configure.isBypassChinaRoutes(), true);
        }
    }

    public static void resetAllSelectedValue(Context context) {
        RealmResults<ShadowVPNConfigure> shadowVPNConfigureRealmResults = ShadowVPNConfigureHelper.getAll(context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        for (int i = 0; i < shadowVPNConfigureRealmResults.size(); i++) {
            shadowVPNConfigureRealmResults.get(i).setSelected(false);
        }

        realm.commitTransaction();
    }
}