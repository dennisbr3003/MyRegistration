package com.dennisbrink.mt.global.myregistration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormFragment extends Fragment {
    // public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    WebClient webClient;
    Button btnRegisterNow;
    EditText etDisplayName, etCallSign, etEmailAddress, etLanguage;
    CheckBox cbUpsertOnline;
    TextView tvOnlineState;
    GameProfile gameProfile;
    public FormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form, container, false);

        Intent i = getActivity().getIntent();
        gameProfile = (GameProfile) i.getSerializableExtra("CONFIG");

        webClient = new WebClient(getActivity());
        webClient.initWebClient();

        btnRegisterNow = v.findViewById(R.id.btnRegisterNow);
        cbUpsertOnline = v.findViewById(R.id.cbUpsertOnline);
        tvOnlineState = v.findViewById(R.id.tvOnlineState);
        etDisplayName = v.findViewById(R.id.etDisplayName);
        etCallSign = v.findViewById(R.id.etCallSign);
        etEmailAddress = v.findViewById(R.id.etEmailAddress);
        etLanguage = v.findViewById(R.id.etLanguage);

        etDisplayName.setText(gameProfile.getPlayer().getDisplayName());
        etCallSign.setText(gameProfile.getPlayer().getCallSign());
        etEmailAddress.setText(gameProfile.getPlayer().getEmail());
        etLanguage.setText(gameProfile.getPlayer().getLanguage());

        cbUpsertOnline.setChecked(gameProfile.isDoUpsertOnline());
        if(gameProfile.isRegistered())tvOnlineState.setText("Curently you are registered online and you compete in the global competition");
        else tvOnlineState.setText("Curently you are NOT registered online and you DO NOT compete in the global competition");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DB1", "clicked the register-now (= send) button, it will switch the fragment");

                // hier moet okhttp angezwengeld worden
                saveRegistration();

            }
        });

        return v;

    }

    private void saveRegistration() {

        gameProfile.setDoUpsertOnline(cbUpsertOnline.isChecked()); // save the checkbox value

        gameProfile.setConfigValues(
                etCallSign.getText().toString(),
                etDisplayName.getText().toString(),
                etEmailAddress.getText().toString(),
                etLanguage.getText().toString()
        );

        // save values to file;
        // activity extends context so this is a context that can be passed like this
        FileHelper.writeData(gameProfile, getActivity());

        if(!cbUpsertOnline.isChecked()){
            return;
        }

        try {
            webClient.savePlayer(gameProfile.getPlayer());
        } catch (JsonProcessingException e) {
            Log.d("DB1", "FormFragment.saveRegistration().JsonProcessingException " + e.getMessage());
        }


    }
}