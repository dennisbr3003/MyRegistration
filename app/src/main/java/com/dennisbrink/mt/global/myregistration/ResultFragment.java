package com.dennisbrink.mt.global.myregistration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultFragment extends Fragment {

    boolean onlineRegistration;

    TextView tvResult, tvMsg;
    ImageView ivRegisterResult;

    public ResultFragment() {
        // Required empty public constructor
    }
// TODO countdowntimer (ContDownTimer) see Ferdi The Fly sow we can auto "go back" use circular progress bar
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_result, container, false);

        tvResult = v.findViewById(R.id.tvResult);
        tvMsg = v.findViewById(R.id.tvMsg);
        ivRegisterResult = v.findViewById(R.id.ivRegisterResult);

        Intent i = getActivity().getIntent();
        onlineRegistration = i.getBooleanExtra("ONLINE_REGISTRATION", false);

        if(onlineRegistration) {
            tvResult.setText("Registration successful");
            ivRegisterResult.setImageResource(R.drawable.checkok);
        } else {
            tvResult.setText("Registration failure");
            ivRegisterResult.setImageResource(R.drawable.error);
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(i.getStringExtra("MSG"));
        }

        return v;
    }
}