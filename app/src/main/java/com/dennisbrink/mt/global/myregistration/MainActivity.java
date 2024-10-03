package com.dennisbrink.mt.global.myregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/*
     this would be the activity where you can choose the game type
     If you are not registered you will find a button that opens the next activity
     in that activity there will be a fragment where you can enter some data for
     the registration and there will be a register and a decline button. There
     will be a second fragment that will act a a result fragment. Thee will also
     be a loader for the registration. Te result will be sent to a receiver to
     which this activity will subscribe
*/
public class MainActivity extends AppCompatActivity {
    Configuration config;
    Button btnGoToRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = FileHelper.readData(MainActivity.this);

        btnGoToRegistration = findViewById(R.id.btnGoToRegistration);
        btnGoToRegistration.setOnClickListener(view -> {
            Log.d("DB1", "clicked the register button");
            startRegsitration(config);
        });

        if(config.isRegistered()){
            Log.d("DB1", "toon GEEN button om te registreren");
            btnGoToRegistration.setVisibility(View.INVISIBLE);
        } else {
            Log.d("DB1", "toon MT logo");
        }

        Log.d("DB1", config.toString());
    }

    private void startRegsitration(Configuration config) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class); // from --> to
        i.putExtra("CONFIG", config); // send this parameter
        startActivity(i); // run it
        // finish(); // close this activity
    }

}