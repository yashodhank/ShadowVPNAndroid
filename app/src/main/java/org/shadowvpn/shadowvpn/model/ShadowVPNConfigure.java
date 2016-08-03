package org.shadowvpn.shadowvpn.model;

import io.realm.RealmObject;

public class ShadowVPNConfigure extends RealmObject {
    private String title;
    private String serverIP;
    private int port;
    private String password;
    private String userToken;
    private String localIP;
    private int maximumTransmissionUnits;
    private int concurrency;
    private boolean bypassChinaRoutes;
    private boolean selected;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getServerIP() {
        return this.serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserToken() {
        return this.userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getLocalIP() {
        return this.localIP;
    }

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public int getMaximumTransmissionUnits() {
        return this.maximumTransmissionUnits;
    }

    public void setMaximumTransmissionUnits(int maximumTransmissionUnits) {
        this.maximumTransmissionUnits = maximumTransmissionUnits;
    }

    public int getConcurrency() {
        return this.concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public boolean isBypassChinaRoutes() {
        return this.bypassChinaRoutes;
    }

    public void setBypassChinaRoutes(boolean bypassChinaRoutes) {
        this.bypassChinaRoutes = bypassChinaRoutes;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}