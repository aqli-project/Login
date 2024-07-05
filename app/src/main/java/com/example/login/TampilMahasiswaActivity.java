package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TampilMahasiswaActivity extends AppCompatActivity {
    private FloatingActionButton _addButton, _refreshButton;
    private RecyclerView _recyclerView1;
    private List<MahasiswaModel> mahasiswaModelList;
    private MahasiswaAdapter ma;
    private TextView _textMahasiswaCount, _textSearch;
    private ImageButton _btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_mahasiswa);

        _recyclerView1 = findViewById(R.id.recyclerView1);
        _textMahasiswaCount = findViewById(R.id.textMahasiswaCount);

        loadRecyclerView();
        initAddButton();
        initRefreshButton();
        initSearch();
    }
    private void initSearch() {
        _textSearch = findViewById(R.id.textSearch);

        _textSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String filterText = _textSearch.getText().toString();
                if (!filterText.isEmpty()) {
                    filter(filterText);
                } else {
                    loadRecyclerView();
                }
                return false;
            }
        });
    }


    private void filter(String text) {
        List<MahasiswaModel> filteredList = new ArrayList<>();

        for (MahasiswaModel item: mahasiswaModelList){
            if (item.getNama().toLowerCase().contains(text.toLowerCase()));{
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(TampilMahasiswaActivity.this, "No Data Found...", Toast.LENGTH_SHORT).show();
        }else{
            ma.filter(filteredList);
        }
    }


    private void loadRecyclerView(){
        AsyncHttpClient ahc = new AsyncHttpClient();
        String Url = "https://stmikpontianak.net/011100862/tampilMahasiswa.php";

        ahc.get(Url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson g = new Gson();

                mahasiswaModelList = g.fromJson(new String(responseBody), new TypeToken<List<MahasiswaModel>>(){}.getType());

                RecyclerView.LayoutManager lm = new LinearLayoutManager(TampilMahasiswaActivity.this);
                _recyclerView1.setLayoutManager(lm);

                ma = new MahasiswaAdapter(mahasiswaModelList);
                _recyclerView1.setAdapter(ma);

                String mahasiswaCount = "Total Mahasiswa : " + ma.getItemCount();
                _textMahasiswaCount.setText(mahasiswaCount);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initAddButton(){
        _addButton = findViewById(R.id.addButton);
        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TambahMahasiswaActivity.class);
                startActivity(intent);

                loadRecyclerView();
            }
        });
    }

    private void initRefreshButton() {
        _refreshButton = findViewById(R.id.refreshButton);
        _refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecyclerView();
            }
        });
    }
}