package com.next.up.code.myskripsi.ui.student.status.detail;

import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREFERENCES_NAME;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_ID_USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.R;
import com.next.up.code.myskripsi.core.data.api.ApiRepository;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.response.item.MessageItem;
import com.next.up.code.myskripsi.ui.lecturer.check.detail.DetailThesisActivity;
import com.next.up.code.myskripsi.ui.lecturer.check.detail.ImageViewActivity;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class DetailStatusActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> resultLauncher;

    private TextView tvJudul, tvNim, tvNama, tvAgo, tvRevisi, tvFilename, tvTitle, tvNewFilename, tvHeaderUpload;
    private Button tvStatus, btnDelete, btnSend;
    private ImageView btnBack, btnCloseFile;

    private LoadingView loadingView;
    private FrameLayout flUpload;
    private SharedPreferences sharedPreferences;

    private String idUser;
    private String selectedPath;
    private ApiRepository apiRepository;
    private CardView viewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_status);

        apiRepository = new ApiRepository();
        tvJudul = findViewById(R.id.tv_thesis_title);
        tvNama = findViewById(R.id.tv_name);
        tvNim = findViewById(R.id.tv_nim);
        tvAgo = findViewById(R.id.tv_time_ago);
        tvFilename = findViewById(R.id.tv_filename);
        tvRevisi = findViewById(R.id.tv_revision);
        tvStatus = findViewById(R.id.tv_status);
        btnBack = findViewById(R.id.btn_back);
        btnDelete = findViewById(R.id.btn_delete);
        btnSend = findViewById(R.id.btn_revisi);
        tvTitle = findViewById(R.id.title);
        loadingView = findViewById(R.id.load_delete);
        flUpload = findViewById(R.id.fl_upload);
        btnCloseFile = findViewById(R.id.btn_close_file);
        tvNewFilename = findViewById(R.id.tv_new_filename);
        tvHeaderUpload = findViewById(R.id.tv_header_upload);
        viewImage = findViewById(R.id.view_image);
        setupUI();
        setupMainButton();
        preferencesSetup();
    }

    private void preferencesSetup() {
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        idUser = sharedPreferences.getString(PREF_ID_USER, "");
    }

    private void setupUI() {
        tvTitle.setText("Detail Pengajuan");
        String judul = getIntent().getStringExtra("judul");
        String nama_mahasiswa = getIntent().getStringExtra("nama_mahasiswa");
        String status = getIntent().getStringExtra("status");
        String nama_file = getIntent().getStringExtra("nama_file");
        String revisi = getIntent().getStringExtra("revisi");
        String nim = getIntent().getStringExtra("nim");
        String revisi_gambar = getIntent().getStringExtra("revisi_gambar");
        String ago = getIntent().getStringExtra("ago");

        tvJudul.setText(judul);
        tvNama.setText(nama_mahasiswa);
        tvAgo.setText(ago);
        tvStatus.setText(status);
        tvNim.setText(nim);
        tvFilename.setText(nama_file);
        if (revisi == null) {
            tvRevisi.setText("Belum ada revisi");
        } else {
            tvRevisi.setText(revisi);
        }

        if (Objects.equals(status, "Perlu Revisi")) {
            btnDelete.setVisibility(View.GONE);
            tvFilename.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
            flUpload.setVisibility(View.VISIBLE);
            tvHeaderUpload.setVisibility(View.VISIBLE);
            viewImage.setVisibility(View.VISIBLE);
            resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (data != null) {
                    Uri uri = data.getData();
                    String fileName = null;
                    if (uri != null) {
                        // Jika URI menggunakan content:// scheme (umumnya untuk berbagi file)
                        if (uri.getScheme().equals("content")) {
                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                            try {
                                if (cursor != null && cursor.moveToFirst()) {
                                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                    if (displayNameIndex != -1) {
                                        fileName = cursor.getString(displayNameIndex);
                                    }
                                }
                            } finally {
                                cursor.close();
                            }
                        }
                        // Jika URI menggunakan file:// scheme (umumnya untuk file dalam penyimpanan lokal)
                        else if (uri.getScheme().equals("file")) {
                            fileName = uri.getLastPathSegment();
                        }
                    }
                    flUpload.setVisibility(View.GONE);
                    tvNewFilename.setVisibility(View.VISIBLE);
                    btnCloseFile.setVisibility(View.VISIBLE);
                    tvNewFilename.setText(fileName);
                    selectedPath = saveFileToLocalStorage(uri);

                }
            });

        } else {
            tvFilename.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
            viewImage.setVisibility(View.GONE);
        }

        if (Objects.equals(status, "Diterima")) {
            btnDelete.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
            viewImage.setVisibility(View.VISIBLE);
        } else if (Objects.equals(status, "Ditolak")) {
            btnDelete.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
            viewImage.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setupMainButton() {
        String id = getIntent().getStringExtra("id");
        String judul = getIntent().getStringExtra("judul");
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Pemberitahuan")
                    .setMessage("Apakah Anda yakin ingin hapus data?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        delete(id);
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });
        flUpload.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(DetailStatusActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DetailStatusActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                selectPDF();
            }
        });
        btnCloseFile.setOnClickListener(v -> {
            flUpload.setVisibility(View.VISIBLE);
            tvNewFilename.setVisibility(View.GONE);
            btnCloseFile.setVisibility(View.GONE);
            selectedPath = null;
        });

        viewImage.setOnClickListener(v-> {
            String revisi_gambar = getIntent().getStringExtra("revisi_gambar");
            String path = "images/" + revisi_gambar;
            Intent intent = new Intent(DetailStatusActivity.this, ImageViewActivity.class);
            intent.putExtra("pathImage", path);
            startActivity(intent);
        });
        btnSend.setOnClickListener(v -> {
            if (selectedPath != null) {
                File selectedFile = new File(selectedPath);
                edit(id, "Pending", judul, idUser, selectedFile);
                loadingView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void delete(String id) {
        loadingView.setVisibility(View.VISIBLE);
        Log.d("id skripsi", id);
        String requestBodyJson = "{\"skripsi_id\":\"" + id + "\"}";

        apiRepository.delete(requestBodyJson, new ApiResponseCallback<MessageItem>() {
            @Override
            public void onSuccess(MessageItem responseData) {
                loadingView.setVisibility(View.GONE);
                showSuccess(responseData.getMessage());
                onBackPressed();

            }

            @Override
            public void onError(String errorMessage) {
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    private String saveFileToLocalStorage(Uri sourceUri) {
        File destDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Upload");
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        String destFileName = "selected_file.pdf";
        File destFile = new File(destDirectory, destFileName);

        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            OutputStream outputStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            return destFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void selectPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        resultLauncher.launch(intent);
    }

    private void edit(String skripsiIdValue, String statusValue, String judulValue, String mahasiswaIdValue, File file) {
        apiRepository.editSkripsi(skripsiIdValue, statusValue, judulValue, mahasiswaIdValue, file, new ApiResponseCallback<MessageItem>() {
            @Override
            public void onSuccess(MessageItem response) {
                loadingView.setVisibility(View.GONE);
                Toast.makeText(DetailStatusActivity.this, "File revisi dan data berhasil di unggah", Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Error", errorMessage);
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPDF();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccess(String message) {
        new AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.SUCCESS)
                .setTitle("Berhasil")
                .setMessage(message)
                .show();
    }
}