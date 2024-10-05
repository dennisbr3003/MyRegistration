package com.dennisbrink.mt.global.myregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class RegisterActivity extends AppCompatActivity implements IRegisterActivityListener, IRegistrationConstants {
    String deviceId;
    GameProfile config;
    Receiver receiver = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, FormFragment.class, null)
                .commit();
    }

    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ONLINE_REGISTRATION_SUCCESS); // only register this activity for these events for the receiver tio handle
        intentFilter.addAction(ONLINE_REGISTRATION_FAILURE);
        return intentFilter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null){
            Log.d("DB1", "RegisterActivity.onPause(): Unregistering receiver");
            this.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver == null) {
            Log.d("DB1", "RegisterActivity.onResume(): Registering receiver");
            receiver = new Receiver();
            receiver.setRegisterActivityListener(this);
        }
        this.registerReceiver(receiver, getFilter(), RECEIVER_EXPORTED);
    }

    @Override
    public void onlineRegistrationSuccess() {
        Log.d("DB1", "Registration success, stop spinner, switch fragment to result + success");

        Intent i = getIntent();
        i.putExtra("ONLINE_REGISTRATION", true);

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ResultFragment.class, null)
                .commit();
    }

    @Override
    public void onlineRegistrationFailure(String msg) {
        Log.d("DB1", "Registration failure, stop spinner, switch fragment to result + failure + error");

        // getting data from the intent so it will be available for the activity
        Intent i = getIntent();
        i.putExtra("ONLINE_REGISTRATION", false);
        i.putExtra("MSG", msg);

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ResultFragment.class, null)
                .commit();
    }
}