package com.next.up.code.myskripsi.ui.lecturer.home;

import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREFERENCES_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.next.up.code.myskripsi.MainActivity;
import com.next.up.code.myskripsi.R;
import com.next.up.code.myskripsi.ui.lecturer.check.CheckThesisActivity;
import com.next.up.code.myskripsi.ui.lecturer.history.HistoryActivity;

public class LectureHomeActivity extends AppCompatActivity {
    private Button btnMenuCheck, btnHistory;
    private ImageView btnLogout;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_home);
        btnMenuCheck = findViewById(R.id.menu_check_thesis);
        btnHistory = findViewById(R.id.menu_history);
        btnLogout = findViewById(R.id.btn_logout);
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        btnMenuCheck.setOnClickListener(v -> {
            Intent intent = new Intent(LectureHomeActivity.this, CheckThesisActivity.class);
            startActivity(intent);
        });
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(LectureHomeActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Pemberitahuan")
                    .setMessage("Apakah Anda yakin ingin logout?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        sharedPreferences.edit().clear().commit();
                        Intent intent = new Intent(LectureHomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });

    }

    @Override
    public void onBackPressed() {
        // Jika Anda ingin menampilkan dialog konfirmasi sebelum keluar
        new AlertDialog.Builder(this)
                .setTitle("Keluar dari Aplikasi")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}