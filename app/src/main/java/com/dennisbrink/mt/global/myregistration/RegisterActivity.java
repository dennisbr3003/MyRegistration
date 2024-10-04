package com.dennisbrink.mt.global.myregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        String line;
        InputStream in = null;

        try {
            AssetManager am = getAssets();
            in = am.open("datasource.config.json");
        } catch (IOException e) {
            Log.d("DB1", "AppConfig.loadWindSpeeds(): error (IOException) : " + e.getLocalizedMessage());
        } catch(Exception ee){
            Log.d("DB1", "AppConfig.loadWindSpeeds(): error (Exception) : " + ee.getLocalizedMessage());
        }

        StringBuilder str = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while((line = br.readLine()) != null){
                str.append(line.replaceAll("\\s", ""));
            }
        } catch(Exception e) {
            Log.d("DB1", "AppConfig.getApiKey(): error (Exception) : " + e.getLocalizedMessage() + e.getMessage());
        }

        Log.d("DB1", "AppConfig.getApiKey(): string : " + str);
        ApiInfo apiInfo;

        ObjectMapper mapper = new ObjectMapper();
        try {
            apiInfo = mapper.readValue(str.toString(), ApiInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Log.d("DB1", "AppConfig.getApiKey(): string : " + apiInfo.getKey());

        i.putExtra("APIKEY", apiInfo.getKey());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, FormFragment.class, null)
                //.addToBackStack(null) // Name can be null
                .commit();
    }
}