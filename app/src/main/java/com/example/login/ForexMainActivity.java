package com.example.login;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

public class ForexMainActivity extends AppCompatActivity {
    private SwipeRefreshLayout _swipeRefreshLayout1;
    private RecyclerView _recyclerView1;
    private TextView _timestampTextView;
    private Map<String, String> currencyNames = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forex_main);

        initSwipeRefreshLayout();
        _recyclerView1 = findViewById(R.id.recyclerView1);
        _timestampTextView = findViewById(R.id.timestampTextView);

        long timestamp = 1719457200L;
        setTimestamp(timestamp);

        fetchCurrencyNames();
        bindRecyclerView();

    }
    private void fetchCurrencyNames() {
        String url = "https://openexchangerates.org/api/currencies.json";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    currencyNames.clear();
                    for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                        String key = it.next();
                        String value = jsonObject.getString(key);
                        currencyNames.put(key, value);
                    }
                    bindRecyclerView();
                } catch (JSONException e) {
                    Toast.makeText(ForexMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    Toast.makeText(ForexMainActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForexMainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindRecyclerView() {
        String Url = "https://openexchangerates.org/api/latest.json?app_id=94decff3326c4b76b5a8731bb6371ba3";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();


        asyncHttpClient.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                JSONObject root;

                try {
                    root = new JSONObject(jsonString);
                }catch (JSONException e){
                    Toast.makeText(ForexMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject rates;

                long timestamp = System.currentTimeMillis() / 1000;
                setTimestamp(timestamp);

                try {
                    rates = root.getJSONObject("rates");
                }catch (JSONException e){
                    Toast.makeText(ForexMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ForexMainActivity.this);
                ForexAdapter adapter = new ForexAdapter(rates, currencyNames);
                _recyclerView1.setLayoutManager(layoutManager);
                _recyclerView1.setAdapter(adapter);

                _swipeRefreshLayout1.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ForexMainActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTimestamp(long timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateTime = format.format(new Date(timestamp * 1000));

        _timestampTextView.setText("Tanggal dan Waktu (UTC): " + dateTime);
    }

    private void initSwipeRefreshLayout() {
        _swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout1);

        _swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindRecyclerView();
            }
        });
    }
}