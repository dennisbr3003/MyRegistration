package com.dennisbrink.mt.global.myregistration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FormFragment extends Fragment {

    Button btnRegisterNow;
    EditText etDisplayName, etCallSign, etEmailAddress, etLanguage;
    Configuration config;
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


        btnRegisterNow = v.findViewById(R.id.btnRegisterNow);

        etDisplayName = v.findViewById(R.id.etDisplayName);
        etCallSign = v.findViewById(R.id.etCallSign);
        etEmailAddress = v.findViewById(R.id.etEmailAddress);
        etLanguage = v.findViewById(R.id.etLanguage);

        Log.d("DB1", "fragment" + config.toString());

        etDisplayName.setText(config.getPlayer().getDisplayName());
        etCallSign.setText(config.getPlayer().getCallSign());
        etEmailAddress.setText(config.getPlayer().getEmail());
        etLanguage.setText(config.getPlayer().getLanguage());

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DB1", "clicked the register-now (= send) button, it will switch the fragment");
                try {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, ResultFragment.class, null)
                            //.addToBackStack(null) // Name can be null
                            .commit();
                }catch (Exception e) {
                    Log.d("DB1", e.getMessage());
                }

            }
        });

        return v;

    }
}