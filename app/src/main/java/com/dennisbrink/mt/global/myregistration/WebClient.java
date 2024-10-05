package com.dennisbrink.mt.global.myregistration;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebClient implements IRegistrationConstants {

    private boolean isOperational;
    private String apiKey, apiUrl;
    private Context context;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public WebClient(Context context) {
        this.context = context;
    }

    public void initWebClient() {
        try {
            getApiConfiguration();
        } catch (IOException e) {
            this.isOperational = false;
            Log.d("DB1", "Error WebClient.WebClient(): " + e.getMessage());
        }
        this.isOperational = true;
    }

    private void getApiConfiguration() throws IOException {

        String sCharacter = "";
        InputStream inputStream = null;
        ApiConfiguration apiConfiguration;

        AssetManager assetManager = this.context.getAssets();
        inputStream = assetManager.open("datasource.config.json");

        StringBuilder str = new StringBuilder();

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((sCharacter = bufferedReader.readLine()) != null) {
                str.append(sCharacter);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        apiConfiguration = mapper.readValue(str.toString(), ApiConfiguration.class);

        this.apiKey = apiConfiguration.getKey();
        this.apiUrl = apiConfiguration.getUrl();
    }

    public void savePlayer(Player player) throws JsonProcessingException {

        OkHttpClient okHttpClient = new OkHttpClient();
        String sBody;

        ObjectMapper objectMapper = new ObjectMapper();
        sBody = objectMapper.writeValueAsString(player);

        RequestBody body = RequestBody.create(sBody, JSON);

        Request request = new Request.Builder()
                .header("token", this.apiKey)
                .post(body)
                .url(this.apiUrl)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("DB1", "Error: " + e.getMessage());
                sendRegistrationFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("DB1", "call may be a success " + response.body());
                sendRegistrationSuccess();
            }
        });
    }

    private void sendRegistrationFailure(String msg) {
        Log.d("DB1", "trying to send registration failure");
        Intent i = new Intent();
        i.setAction(ONLINE_REGISTRATION_FAILURE);
        i.putExtra("MSG", msg);
        this.context.sendBroadcast(i);
    }

    private void sendRegistrationSuccess() {
        Log.d("DB1", "trying to send registration success");
        Intent i = new Intent();
        i.setAction(ONLINE_REGISTRATION_SUCCESS);
        this.context.sendBroadcast(i);
    }

    @Override
    public String toString() {
        return "WebClient{" +
                "isOperational=" + isOperational +
                ", apiKey='" + apiKey + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                '}';
    }
}
