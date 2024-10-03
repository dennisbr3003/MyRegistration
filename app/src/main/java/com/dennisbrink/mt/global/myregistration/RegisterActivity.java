package com.dennisbrink.mt.global.myregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class RegisterActivity extends AppCompatActivity {
    String deviceId;
    Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // getting data from the intent
        Intent i = getIntent();
        //deviceId = i.getStringExtra("DEVICE_ID");  // get parameter value

        config = (Configuration) i.getSerializableExtra("CONFIG");
        Log.d("DB1", "GameActivity.class: (onCreate) config " + config.toString());

//        Bundle bundle = new Bundle();
//        bundle.putString("username", username);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, FormFragment.class, null)
                //.addToBackStack(null) // Name can be null
                .commit();
    }
}