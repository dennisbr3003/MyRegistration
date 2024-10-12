package com.dennisbrink.mt.global.myregistration;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FormFragment extends Fragment implements AdapterView.OnItemSelectedListener, IRegistrationConstants {
    WebClient webClient;
    Button btnRegisterNow;
    EditText etDisplayName, etCallSign, etEmailAddress;
    CheckBox cbUpsertOnline;
    TextView tvOnlineState;
    Spinner spin;
    GameProfile gameProfile;
    ArrayList<LanguageSpinnerItem> languageSpinnerItemArrayList;

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

        Intent i = requireActivity().getIntent();
        gameProfile = (GameProfile) i.getSerializableExtra("CONFIG");

        webClient = new WebClient(getActivity());
        webClient.initWebClient();

        btnRegisterNow = v.findViewById(R.id.btnRegisterNow);
        cbUpsertOnline = v.findViewById(R.id.cbUpsertOnline);
        tvOnlineState = v.findViewById(R.id.tvOnlineState);
        etDisplayName = v.findViewById(R.id.etDisplayName);
        etCallSign = v.findViewById(R.id.etCallSign);
        etEmailAddress = v.findViewById(R.id.etEmailAddress);

//        FrameLayout fl = v.findViewById(R.id.frameLayoutProfile);
//        fl.setBackground(AppCompatResources.getDrawable(requireActivity(), R.drawable.math15));

        Spinner spin = v.findViewById(R.id.spinLanguage);
        spin.setOnItemSelectedListener(this);

        // fill spinner item array list
        createLanguageItemList();

        // attach adapter to spinner
        CustomAdapter customAdapter = new CustomAdapter(requireActivity().getApplicationContext(), languageSpinnerItemArrayList);
        spin.setAdapter(customAdapter);

        // set spinner to saved item
        AtomicInteger idx = new AtomicInteger(0);
        languageSpinnerItemArrayList.forEach((languageSpinnerItem) -> {
            if(languageSpinnerItem.getIsoCode().equals(gameProfile.getPlayer().getLanguage())){
                spin.setSelection(idx.get(), true);
            }
            idx.getAndIncrement();
        });

        etDisplayName.setText(gameProfile.getPlayer().getDisplayName());
        etCallSign.setText(gameProfile.getPlayer().getCallSign());
        etEmailAddress.setText(gameProfile.getPlayer().getEmail());

        cbUpsertOnline.setChecked(gameProfile.isDoUpsertOnline());
        if(gameProfile.isRegistered())tvOnlineState.setText("Curently you are registered online and you compete in the global competition");
        else tvOnlineState.setText("Curently you are NOT registered online and you DO NOT compete in the global competition");

        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                cbUpsertOnline.isChecked()
        );

        FileHelper.writeData(gameProfile, requireActivity());

        if(!cbUpsertOnline.isChecked()){
            sendRegistrationSuccess();
            return;
        }

        try {
            webClient.savePlayer(gameProfile.getPlayer());
        } catch (JsonProcessingException e) {
            sendRegistrationFailure( e.getMessage());
        }

    }

    private void createLanguageItemList() {
        languageSpinnerItemArrayList = new ArrayList<>();
        languageSpinnerItemArrayList.add(new LanguageSpinnerItem(R.drawable.nl, "Nederlands", "NL"));
        languageSpinnerItemArrayList.add(new LanguageSpinnerItem(R.drawable.de, "Deutsch", "DE"));
        languageSpinnerItemArrayList.add(new LanguageSpinnerItem(R.drawable.en, "English", "EN"));
        languageSpinnerItemArrayList.add(new LanguageSpinnerItem(R.drawable.fr, "Français", "FR"));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gameProfile.setLanguage(languageSpinnerItemArrayList.get(i).getIsoCode());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendRegistrationSuccess() {
        Log.d("DB1", "trying to send registration success");
        Intent i = new Intent();
        i.setAction(LOCAL_REGISTRATION_SUCCESS);
        requireActivity().sendBroadcast(i);
    }

    private void sendRegistrationFailure(String msg) {
        Log.d("DB1", "trying to send registration failure");
        Intent i = new Intent();
        i.setAction(LOCAL_REGISTRATION_FAILURE);
        i.putExtra("MSG", msg);
        requireActivity().sendBroadcast(i);
    }

}