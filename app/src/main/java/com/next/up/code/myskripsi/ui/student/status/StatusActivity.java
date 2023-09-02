package com.next.up.code.myskripsi.ui.student.status;

import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREFERENCES_NAME;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_ID_USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.R;
import com.next.up.code.myskripsi.adapter.PengajuanAdapter;
import com.next.up.code.myskripsi.core.data.api.ApiRepository;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.response.SkripsiResponse;
import com.next.up.code.myskripsi.core.data.api.response.item.SkripsiItem;
import com.next.up.code.myskripsi.ui.student.status.detail.DetailStatusActivity;

import java.util.List;

public class StatusActivity extends AppCompatActivity {
    private RecyclerView rvPengajuan;
    private ImageView btnBack;
    private TextView tvTitle;
    private ApiRepository apiRepository;

    private SharedPreferences sharedPreferences;

    private String idUser;
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        apiRepository = new ApiRepository();
        rvPengajuan = findViewById(R.id.rv_pengajuan);
        loadingView = findViewById(R.id.load_skripsi);
        tvTitle = findViewById(R.id.title);
        btnBack = findViewById(R.id.btn_back);

        preferencesSetup();
        setupUI();
        getSkripsiList();
    }

    private void preferencesSetup() {
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        idUser = sharedPreferences.getString(PREF_ID_USER, "");
    }

    private void setupUI() {
        tvTitle.setText("Status Pengajuan");
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getSkripsiList() {
        loadingView.setVisibility(View.VISIBLE);
        PengajuanAdapter pengajuanAdapter = new PengajuanAdapter();
        apiRepository.getSkripsiList(Integer.parseInt(idUser), new ApiResponseCallback<SkripsiResponse>() {
            @Override
            public void onSuccess(SkripsiResponse data) {
                loadingView.setVisibility(View.GONE);
                List<SkripsiItem> skripsiList = data.getSkripsiList();
                pengajuanAdapter.setData(skripsiList);
                rvPengajuan.setAdapter(pengajuanAdapter);
                rvPengajuan.setLayoutManager(new LinearLayoutManager(StatusActivity.this, LinearLayoutManager.VERTICAL, false));
                rvPengajuan.setHasFixedSize(true);

                pengajuanAdapter.setOnItemClickCallback(skripsiItem -> {
                    Intent intent = new Intent(StatusActivity.this, DetailStatusActivity.class);
                    intent.putExtra("id", skripsiItem.getId());
                    intent.putExtra("judul", skripsiItem.getJudul());
                    intent.putExtra("status", skripsiItem.getStatus());
                    intent.putExtra("nama_file", skripsiItem.getNamaFile());
                    intent.putExtra("nama_mahasiswa", skripsiItem.getNamaMahasiswa());
                    intent.putExtra("nim", skripsiItem.getNim());
                    intent.putExtra("ago", skripsiItem.getCreatedTimeAgo());
                    intent.putExtra("revisi", skripsiItem.getRevisi());
                    intent.putExtra("revisi_gambar", skripsiItem.getRevisi_gambar());
                    startActivity(intent);
                });

            }

            @Override
            public void onError(String errorMessage) {
                loadingView.setVisibility(View.GONE);
                Log.e("YourActivity", "Get skripsi list failed. Error: " + errorMessage);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSkripsiList();
    }
}