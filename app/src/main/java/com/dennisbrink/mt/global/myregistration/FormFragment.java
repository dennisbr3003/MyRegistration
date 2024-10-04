package com.dennisbrink.mt.global.myregistration;

import android.content.Intent;
import android.content.res.AssetManager;
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
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Button btnRegisterNow;
    EditText etDisplayName, etCallSign, etEmailAddress, etLanguage;
    CheckBox cbUpsertOnline;
    TextView tvOnlineState;
    Configuration config;
    String apikey;
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
        config = (Configuration) i.getSerializableExtra("CONFIG");
        apikey = i.getStringExtra("APIKEY");

        btnRegisterNow = v.findViewById(R.id.btnRegisterNow);
        cbUpsertOnline = v.findViewById(R.id.cbUpsertOnline);
        tvOnlineState = v.findViewById(R.id.tvOnlineState);
        etDisplayName = v.findViewById(R.id.etDisplayName);
        etCallSign = v.findViewById(R.id.etCallSign);
        etEmailAddress = v.findViewById(R.id.etEmailAddress);
        etLanguage = v.findViewById(R.id.etLanguage);

        Log.d("DB1", "fragment" + config.toString());
        Log.d("DB1", "APIKEY " + apikey);

        etDisplayName.setText(config.getPlayer().getDisplayName());
        etCallSign.setText(config.getPlayer().getCallSign());
        etEmailAddress.setText(config.getPlayer().getEmail());
        etLanguage.setText(config.getPlayer().getLanguage());

        cbUpsertOnline.setChecked(config.isDoUpsertOnline());
        if(config.isRegistered())tvOnlineState.setText("Curently you are registered online and you compete in the global competition");
        else tvOnlineState.setText("Curently you are NOT registered online and you DO NOT compete in the global competition");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DB1", "clicked the register-now (= send) button, it will switch the fragment");

                // hier moet okhttp angezwengeld worden
                saveRegistration();

                try {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, ResultFragment.class, null)
                            //.addToBackStack(null) // Name can be null, but the fragment would still be on the stack (yet nameless)
                            .commit();
                }catch (Exception e) {
                    Log.d("DB1", e.getMessage());
                }

            }
        });

        return v;

    }

    private void saveRegistration() {

        config.setDoUpsertOnline(cbUpsertOnline.isChecked()); // save the checkbox vaue

        config.setConfigValues(
                etCallSign.getText().toString(),
                etDisplayName.getText().toString(),
                etEmailAddress.getText().toString(),
                etLanguage.getText().toString()
        );

        if(!cbUpsertOnline.isChecked()){
            FileHelper.writeData(config, getActivity()); // activity extends context so this is a context
            return;
        }

        // dit moet helemaal ergens anders naar toe zodat ik het onder cbUpsertOnline kan hangen
        OkHttpClient client = new OkHttpClient();
        String sBody;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            sBody = objectMapper.writeValueAsString(config.getPlayer());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(sBody, JSON);

        Log.d("DB1", "pojo to json" + body);


        Request request = new Request.Builder()
                .header("token", apikey)
                .post(body)
                .url("http://192.168.178.39:3200/player")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("DB1", "call is a failure" + e.getMessage());
                // param naar next fragment met tur, switch should also be here + spinner
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("DB1", "call may be a success" + response.body());
                config.setRegistered(true);
                FileHelper.writeData(config, getActivity());
                // param naar next fragment met false, switch should also be here + spinner
            }
        });

    }
}