package com.dennisbrink.mt.global.myregistration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver implements IRegistrationConstants {

    private IRegisterActivityListener registerActivityListener;

    public void setRegisterActivityListener(IRegisterActivityListener registerActivityListener){
        this.registerActivityListener = registerActivityListener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DB1", "Receiver.onReceive() reached with action " + intent.getAction());

        if(intent.getAction().equals(ONLINE_REGISTRATION_FAILURE)) {
            if (registerActivityListener != null) {
                registerActivityListener.onlineRegistrationFailure(intent.getStringExtra("MSG"));
            }
        }

        if(intent.getAction().equals(ONLINE_REGISTRATION_SUCCESS)) {
            if (registerActivityListener != null) {
                registerActivityListener.onlineRegistrationSuccess();
            }
        }

        if(intent.getAction().equals(LOCAL_REGISTRATION_FAILURE)) {
            if (registerActivityListener != null) {
                registerActivityListener.localRegistrationFailure(intent.getStringExtra("MSG"));
            }
        }

        if(intent.getAction().equals(LOCAL_REGISTRATION_SUCCESS)) {
            if (registerActivityListener != null) {
                registerActivityListener.localRegistrationSuccess();
            }
        }

    }

}
