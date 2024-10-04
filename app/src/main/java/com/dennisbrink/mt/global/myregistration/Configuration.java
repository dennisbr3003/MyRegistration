package com.dennisbrink.mt.global.myregistration;

import android.content.Context;
import android.provider.Settings;

import java.io.Serializable;

public class Configuration implements Serializable {
    Player player;
    private String deviceId, callSign, displayName, email, language;
    private boolean isRegistered, doUpsertOnline;

    public Configuration(Context context) {
        this.isRegistered = false;
        this.doUpsertOnline = true;
        this.deviceId = setDeviceId(context);
    }

    public boolean isDoUpsertOnline() {
        return doUpsertOnline;
    }

    public void setDoUpsertOnline(boolean doUpsertOnline) {
        this.doUpsertOnline = doUpsertOnline;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setConfigValues(String callSign, String displayName, String email, String language){
        this.callSign = callSign;
        this.displayName = displayName;
        this.email = email;
        this.language = language;
    }

    private String setDeviceId(Context context) {
        String tDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return tDeviceId;
    }

    public Player getPlayer() {
        return new Player(this.deviceId, this.callSign, this.displayName, this.email, this.language);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "player=" + player +
                ", deviceId='" + deviceId + '\'' +
                ", callSign='" + callSign + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", language='" + language + '\'' +
                ", isRegistered=" + isRegistered +
                ", doUpsertOnline=" + doUpsertOnline +
                '}';
    }
}
