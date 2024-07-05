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

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class CuacaMainActivity extends AppCompatActivity {

    private RecyclerView _recyclerView2;
    private CuacaRootModel _rootModel2;
    private SwipeRefreshLayout _swipeRefreshLayout2;
    private TextView _totalTextView;
    private TextView _textViewCityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuaca_main);

        _recyclerView2 = findViewById(R.id.recyclerView2);
        _totalTextView = findViewById(R.id.totalTextView);
        _textViewCityInfo = findViewById(R.id.textViewcityInfo);
        _swipeRefreshLayout2 = findViewById(R.id.swipeRefreshLayout2);

        bindRecyclerView1();
        initSwipeRefreshLayout();
    }
    private void initSwipeRefreshLayout()
    {
        _swipeRefreshLayout2 = findViewById(R.id.swipeRefreshLayout2);
        _swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindRecyclerView1();
                _swipeRefreshLayout2.setRefreshing(false);
            }
        });
    }
    private void bindRecyclerView1(){
        String url = "https://api.openweathermap.org/data/2.5/forecast?id=1630789&appid=5e8ba9b2d5f28a8b6d76a218e617da7b";
        AsyncHttpClient ahc = new AsyncHttpClient();

        ahc.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                _rootModel2 = gson.fromJson(new String(responseBody), CuacaRootModel.class);

                iniCityInfo();

                RecyclerView.LayoutManager lm = new LinearLayoutManager(CuacaMainActivity.this);
                CuacaAdapter ca = new CuacaAdapter(CuacaMainActivity.this, _rootModel2);

                _recyclerView2.setLayoutManager(lm);
                _recyclerView2.setAdapter(ca);
                _totalTextView.setText("Total Record : " + ca.getItemCount());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void iniCityInfo() {
        CuacaCityModel cm = _rootModel2.getCityModel();
        long sunrise = cm.getSunrise();
        long sunset = cm.getSunset();
        String cityName = cm.getName();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String sunriseTime = sdf.format(new Date(sunrise * 1000));
        String sunsetTime = sdf.format(new Date(sunset * 1000));

        String cityInfo = "Kota:" + cityName + " \n" +
                "Matahari Terbit:" + sunriseTime + "(lokal)\n " +
                "Matahari Terbenam:" + sunsetTime + "(lokal)";
        _textViewCityInfo.setText(cityInfo);
    }
}