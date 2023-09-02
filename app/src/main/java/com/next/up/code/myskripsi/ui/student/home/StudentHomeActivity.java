package com.next.up.code.myskripsi.ui.student.home;

import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREFERENCES_NAME;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_ID_USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.MainActivity;
import com.next.up.code.myskripsi.R;
import com.next.up.code.myskripsi.core.data.api.ApiRepository;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.response.SkripsiResponse;
import com.next.up.code.myskripsi.core.data.api.response.item.SkripsiItem;
import com.next.up.code.myskripsi.ui.student.form.FormActivity;
import com.next.up.code.myskripsi.ui.student.status.StatusActivity;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.List;

public class StudentHomeActivity extends AppCompatActivity {
    private ApiRepository apiRepository;

    private SharedPreferences sharedPreferences;

    private String idUser;
    private LoadingView loadingView;
    private ImageView btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        apiRepository = new ApiRepository();
        Button btnMenuSubmit = findViewById(R.id.menu_submit);
        Button btnMenuStatus = findViewById(R.id.menu_status);
        loadingView = findViewById(R.id.load_menu);
        btnLogout = findViewById(R.id.btn_logout);
        preferencesSetup();
        btnMenuSubmit.setOnClickListener(v -> {
            loadingView.setVisibility(View.VISIBLE);
            getSkripsiList();
        });

        btnMenuStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatusActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Pemberitahuan")
                    .setMessage("Apakah Anda yakin ingin logout?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        sharedPreferences.edit().clear().commit();
                        Intent intent = new Intent(StudentHomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });

    }

    private void preferencesSetup() {
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        idUser = sharedPreferences.getString(PREF_ID_USER, "");
    }

    private void getSkripsiList() {
        apiRepository.getSkripsiList(Integer.parseInt(idUser), new ApiResponseCallback<SkripsiResponse>() {
            @Override
            public void onSuccess(SkripsiResponse data) {
                // Berhasil mendapatkan daftar skripsi, akses data menggunakan variabel 'data'
                List<SkripsiItem> skripsiList = data.getSkripsiList();
                skripsiList.removeIf(skripsi -> "Ditolak".equals(skripsi.getStatus()));
                if (skripsiList.size() > 0) {

                    loadingView.setVisibility(View.GONE);
                    showAlert("Anda sudah melakukan pengajuan, cek status secara berkala!");
                } else {

                    loadingView.setVisibility(View.GONE);
                    Intent intent = new Intent(StudentHomeActivity.this, FormActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(String errorMessage) {
                loadingView.setVisibility(View.GONE);
                Log.e("YourActivity", "Get skripsi list failed. Error: " + errorMessage);
            }
        });
    }

    private void showAlert(String message) {
        new AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.INFO)
                .setTitle("Pemberitahuan")
                .setMessage(message)
                .show();
    }

}