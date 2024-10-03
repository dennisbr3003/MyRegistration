package com.dennisbrink.mt.global.myregistration;

import android.content.Context;
import android.provider.Settings;

import java.io.Serializable;

public class Configuration implements Serializable {

    private String deviceId, callSign, displayName, email, language;
    private boolean isRegistered;
    Configuration config;

    public Configuration(Context context) {
        this.isRegistered = false;
        this.deviceId = setDeviceId(context);
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

    public void setPlayer(String callSign, String displayName, String email, String language){
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
        return new Player(this.deviceId, this.displayName, this.callSign, this.email, this.language);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "deviceId='" + deviceId + '\'' +
                ", callSign='" + callSign + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", language='" + language + '\'' +
                ", isRegistered=" + isRegistered +
                '}';
    }

    class Player {

        private String deviceId, callSign, displayName, email, language;

        public Player() {
        }

        public Player(String deviceId, String callSign, String displayName, String email, String language) {
            this.deviceId = deviceId==null?"":deviceId;
            this.callSign = callSign==null?"":callSign;
            this.displayName = displayName==null?"":displayName;
            this.email = email==null?"":email;
            this.language = language==null||language==""?"EN":language;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getCallSign() {
            return callSign;
        }

        public void setCallSign(String callSign) {
            this.callSign = callSign;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "deviceId='" + deviceId + '\'' +
                    ", callSign='" + callSign + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", email='" + email + '\'' +
                    ", language='" + language + '\'' +
                    '}';
        }
    }

}
