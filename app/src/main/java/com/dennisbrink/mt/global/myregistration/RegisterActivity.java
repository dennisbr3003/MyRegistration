package com.dennisbrink.mt.global.myregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements IRegisterActivityListener, IRegistrationConstants {
    String deviceId;
    GameProfile config;
    Receiver receiver = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupLogo();

        Intent i = RegisterActivity.this.getIntent();
        config = (GameProfile) i.getSerializableExtra("CONFIG");
        Log.d("DB1", "RegisterActivity.onCreate(): " + config.toString());

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, FormFragment.class, null)
                .commit();
    }

    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ONLINE_REGISTRATION_SUCCESS); // only register this activity for these events for the receiver tio handle
        intentFilter.addAction(ONLINE_REGISTRATION_FAILURE);
        intentFilter.addAction(LOCAL_REGISTRATION_SUCCESS);
        intentFilter.addAction(LOCAL_REGISTRATION_FAILURE);
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
        try {
            if (receiver == null) {
                Log.d("DB1", "RegisterActivity.onResume(): Registering receiver");
                receiver = new Receiver();
                receiver.setRegisterActivityListener(this);
            }
            this.registerReceiver(receiver, getFilter(), RECEIVER_EXPORTED);
        } catch (Exception e) {
            Log.d("DB1", "RegisterActivity.onResume() error: " + e.getMessage());
        }
    }

    @Override
    public void onlineRegistrationSuccess() {
        Log.d("DB1", "Registration success, stop spinner, switch fragment to result + success");

        Intent i = getIntent();
        i.putExtra("ONLINE_REGISTRATION", true);

        // TODO set GameProfile.registered to true
        config.setRegistered(true);
        FileHelper.writeData(config, RegisterActivity.this);

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

    @Override
    public void localRegistrationSuccess() {
        Log.d("DB1", "Registration success, stop spinner, switch fragment to result + success");

        Intent i = getIntent();
        i.putExtra("ONLINE_REGISTRATION", true);

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ResultFragment.class, null)
                .commit();
    }

    @Override
    public void localRegistrationFailure(String msg) {
        Log.d("DB1", "Registration failure, stop spinner, switch fragment to result + failure + error");

        // getting data from the intent so it will be available for the activity
        Intent i = getIntent();
        i.putExtra("ONLINE_REGISTRATION", false);
        i.putExtra("MSG", msg);

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ResultFragment.class, null)
                .commit();
    }

    private void setupLogo(){

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater infl = LayoutInflater.from(this);
        View v = infl.inflate(R.layout.actionbar, null);

        TextView tvActionBarMainTitle = v.findViewById(R.id.tvActionBarMainTitle);
        tvActionBarMainTitle.setText("Game Profile");
        tvActionBarMainTitle.setTextColor(getColor(R.color.white));
        TextView tvActionBarSubTitle = v.findViewById(R.id.tvActionBarSubTitle);
        tvActionBarSubTitle.setText("Setup your game profile...");
        tvActionBarSubTitle.setTextColor(getColor(R.color.white));
        ImageView ivActionBarActionIcon = v.findViewById(R.id.ivActionBarActionIcon);

        ivActionBarActionIcon.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        // the get the custom layout to use full width
        this.getSupportActionBar().setCustomView(v, new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));



    }

}