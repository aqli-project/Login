package com.example.login;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchCurrenciesTask extends AsyncTask<Void, Void, Map<String, String>> {
    private static final String API_URL = "https://openexchangerates.org/api/currencies.json";
    private OnCurrenciesFetchedListener listener;

    public FetchCurrenciesTask(OnCurrenciesFetchedListener listener) {
        this.listener = listener;
    }

    @Override
    protected Map<String, String> doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String jsonData = response.body().string();

            Type type = new TypeToken<Map<String, String>>() {}.getType();
            return new Gson().fromJson(jsonData, type);

        } catch (IOException e) {
            Log.e("FetchCurrenciesTask", "Error fetching currencies", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Map<String, String> currencies) {
        if (listener != null) {
            listener.onCurrenciesFetched(currencies);
        }
    }

    public interface OnCurrenciesFetchedListener {
        void onCurrenciesFetched(Map<String, String> currencies);
    }
}
