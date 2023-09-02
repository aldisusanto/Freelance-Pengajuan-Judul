package com.next.up.code.myskripsi.ui.lecturer.check;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.R;
import com.next.up.code.myskripsi.adapter.PendingAdapter;
import com.next.up.code.myskripsi.core.data.api.ApiRepository;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.response.SkripsiResponse;
import com.next.up.code.myskripsi.core.data.api.response.item.SkripsiItem;
import com.next.up.code.myskripsi.ui.lecturer.check.detail.DetailThesisActivity;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.List;

public class CheckThesisActivity extends AppCompatActivity {

    private LoadingView loadingView;
    private RecyclerView rvPengajuan;
    private ImageView btnBack;
    private TextView tvTitle;
    private ApiRepository apiRepository;

    private List<SkripsiItem> skripsiItemList;
    private SearchView svThesis;
    private PendingAdapter pendingAdapter;
    private List<SkripsiItem> filteredSkripsiItemList; // New list for filtered items


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_thesis);
        apiRepository = new ApiRepository();
        rvPengajuan = findViewById(R.id.rv_pengajuan);
        loadingView = findViewById(R.id.load_skripsi);
        tvTitle = findViewById(R.id.title);
        btnBack = findViewById(R.id.btn_back);
        svThesis = findViewById(R.id.sv_thesis);

        setupUI();
        setupSearch();

    }

    private void setupUI() {
        tvTitle.setText("Cek Pengajuan Tugas Akhir");
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getSkripsiList() {
        skripsiItemList = new ArrayList<>(); // Initialize the list here

        loadingView.setVisibility(View.VISIBLE);
        apiRepository.getAllSkripsiList(new ApiResponseCallback<SkripsiResponse>() {
            @Override
            public void onSuccess(SkripsiResponse data) {
                loadingView.setVisibility(View.GONE);
                List<SkripsiItem> skripsiList = data.getSkripsiList();

                if (skripsiList.isEmpty()) {
                    showInfo("Belum ada pengajuan");
                } else {
                    for (SkripsiItem skripsiItem : skripsiList) {
                        skripsiItemList.add(new SkripsiItem(
                                skripsiItem.getId(),
                                skripsiItem.getJudul(),
                                skripsiItem.getStatus(),
                                skripsiItem.getNamaFile(),
                                skripsiItem.getNamaMahasiswa(),
                                skripsiItem.getNim(),
                                skripsiItem.getCreatedTimeAgo(),
                                skripsiItem.getRevisi(),
                                skripsiItem.getRevisi_gambar()
                        ));
                    }
                    setupRecycleView(skripsiItemList);
                }


            }

            @Override
            public void onError(String errorMessage) {
                loadingView.setVisibility(View.GONE);
                Log.e("YourActivity", "Get skripsi list failed. Error: " + errorMessage);
            }
        });
    }

    private void setupSearch() {
        svThesis.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {
                    searchSkripsi(newText);
                }
                return true;
            }
        });
    }

    private void searchSkripsi(final String keyword) {
        filteredSkripsiItemList = new ArrayList<>(); // Initialize the filtered list

        for (SkripsiItem skripsiItem : skripsiItemList) {
            if (skripsiItem.getJudul().toLowerCase().contains(keyword.toLowerCase())) {
                filteredSkripsiItemList.add(new SkripsiItem(
                        skripsiItem.getId(),
                        skripsiItem.getJudul(),
                        skripsiItem.getStatus(),
                        skripsiItem.getNamaFile(),
                        skripsiItem.getNamaMahasiswa(),
                        skripsiItem.getNim(),
                        skripsiItem.getCreatedTimeAgo(),
                        skripsiItem.getRevisi(),
                        skripsiItem.getRevisi_gambar()
                ));
            }
        }

        if (filteredSkripsiItemList.isEmpty()) {
            setupRecycleView(filteredSkripsiItemList);
        } else {
            setupRecycleView(filteredSkripsiItemList);
        }
    }

    private void setupRecycleView(List<SkripsiItem> skripsiItems) {
        pendingAdapter = new PendingAdapter();
        pendingAdapter.setData(skripsiItems);
        rvPengajuan.setAdapter(pendingAdapter);
        rvPengajuan.setLayoutManager(new LinearLayoutManager(CheckThesisActivity.this, LinearLayoutManager.VERTICAL, false));
        rvPengajuan.setHasFixedSize(true);


        pendingAdapter.setOnItemClickCallback(skripsiItem -> {
            Intent intent = new Intent(CheckThesisActivity.this, DetailThesisActivity.class);
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

    private void showInfo(String message) {
        new AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.INFO).setTitle("Informasi").setMessage(message).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSkripsiList();
    }
}