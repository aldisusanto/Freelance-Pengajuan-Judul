package com.next.up.code.myskripsi.ui.lecturer.check.detail;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.R;
import com.next.up.code.myskripsi.core.data.api.ApiRepository;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.response.item.MessageItem;
import com.next.up.code.myskripsi.ui.lecturer.home.LectureHomeActivity;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class DetailThesisActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> resultLauncher;
    private String selectedPath;
    private TextView tvJudul, tvNim, tvNama, tvAgo, tvFilename, tvTitle, tvImageName;
    private Button btnAccept, btnReject, btnDelete, btnPending;
    private ImageView btnBack, btnClose;

    private LoadingView loadingView;

    private ApiRepository apiRepository;

    private EditText edtRevision;

    private CardView btnViewFile, btnPickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_thesis);
        apiRepository = new ApiRepository();
        tvJudul = findViewById(R.id.tv_thesis_title);
        tvNama = findViewById(R.id.tv_name);
        tvNim = findViewById(R.id.tv_nim);
        tvAgo = findViewById(R.id.tv_time_ago);
        tvFilename = findViewById(R.id.tv_filename);
        tvImageName = findViewById(R.id.tv_image_name);
        btnBack = findViewById(R.id.btn_back);
        btnClose = findViewById(R.id.btn_close_file);
        btnReject = findViewById(R.id.btn_reject);
        btnDelete = findViewById(R.id.btn_delete);
        btnPending = findViewById(R.id.btn_pending);
        btnAccept = findViewById(R.id.btn_accept);
        tvTitle = findViewById(R.id.title);
        loadingView = findViewById(R.id.load_delete);
        edtRevision = findViewById(R.id.edt_revision);
        btnViewFile = findViewById(R.id.btn_view_file);
        btnPickImage = findViewById(R.id.btn_pick_image);
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
                btnPickImage.setVisibility(View.GONE);
                tvImageName.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.VISIBLE);
                tvImageName.setText(fileName);
                selectedPath = saveFileToLocalStorage(uri);

            }
        });

        setupUI();

        setupMainButton();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Menggunakan "image/*" untuk mencakup semua format gambar
        resultLauncher.launch(intent);
    }

    private String saveFileToLocalStorage(Uri sourceUri) {
        File destDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Image");
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        String destFileName = "selected_file.png";
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


    private void setupUI() {
        tvTitle.setText("Detail Pengajuan");
        String judul = getIntent().getStringExtra("judul");
        String nama_mahasiswa = getIntent().getStringExtra("nama_mahasiswa");
        String status = getIntent().getStringExtra("status");
        String nama_file = getIntent().getStringExtra("nama_file");
        String nim = getIntent().getStringExtra("nim");
        String revisi_gambar = getIntent().getStringExtra("revisi_gambar");
        String ago = getIntent().getStringExtra("ago");
        String revisi = getIntent().getStringExtra("revisi");

        if (!Objects.equals(status, "Pending")) {
            btnDelete.setVisibility(View.VISIBLE);
        }
        tvJudul.setText(judul);
        tvNama.setText(nama_mahasiswa);
        tvAgo.setText(ago);
        tvNim.setText(nim);
        tvFilename.setText(nama_file);
        tvImageName.setText(revisi_gambar);
        int colorAccent = ContextCompat.getColor(this, R.color.colorAccent);
        tvImageName.setTextColor(colorAccent);

        if (Objects.equals(status, "Diterima")) {
            edtRevision.setText(revisi);
            btnReject.setVisibility(View.GONE);
            btnAccept.setVisibility(View.GONE);
            btnPickImage.setVisibility(View.GONE);
        } else if (Objects.equals(status, "Ditolak")) {
            edtRevision.setText(revisi);
            btnReject.setVisibility(View.GONE);
            btnAccept.setVisibility(View.GONE);
            btnPickImage.setVisibility(View.GONE);
            tvImageName.setVisibility(View.VISIBLE);
        } else if (Objects.equals(status, "Perlu Revisi")) {
            edtRevision.setText(revisi);
            btnReject.setVisibility(View.GONE);
            btnAccept.setVisibility(View.GONE);
            btnPickImage.setVisibility(View.GONE);
            tvImageName.setVisibility(View.VISIBLE);
        } else {
            btnAccept.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
            tvImageName.setVisibility(View.GONE);
        }
    }

    private void setupMainButton() {
        String id = getIntent().getStringExtra("id");
        String revisi_gambar = getIntent().getStringExtra("revisi_gambar");
        if (revisi_gambar.isEmpty()) {
            tvImageName.setVisibility(View.GONE);
        } else {
            tvImageName.setVisibility(View.VISIBLE);
        }
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnPickImage.setOnClickListener(v -> {
            selectImage();
        });
        btnClose.setOnClickListener(v -> {
            btnPickImage.setVisibility(View.VISIBLE);
            tvImageName.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
            selectedPath = null;
        });
        btnAccept.setOnClickListener(v -> {
            if (edtRevision.getText().toString().isEmpty()) {
                showErrorDialog("Isi revisi!");
            } else {
                if (selectedPath == null) {
                    updateStatus(id, edtRevision.getText().toString(), "Diterima", null);
                } else {
                    File selectedFile = new File(selectedPath);
                    updateStatus(id, edtRevision.getText().toString(), "Diterima", selectedFile);
                }
            }
        });
        btnReject.setOnClickListener(v -> {
            if (edtRevision.getText().toString().isEmpty()) {
                showErrorDialog("Isi revisi!");
            } else {
                if (selectedPath == null) {
                    updateStatus(id, edtRevision.getText().toString(), "Ditolak", null);
                } else {
                    File selectedFile = new File(selectedPath);
                    updateStatus(id, edtRevision.getText().toString(), "Ditolak", selectedFile);
                }
            }
        });

        btnPending.setOnClickListener(v -> {
            if (edtRevision.getText().toString().isEmpty()) {
                showErrorDialog("Isi revisi!");
            } else {
                if (selectedPath == null) {
                    updateStatus(id, edtRevision.getText().toString(), "Perlu Revisi", null);
                } else {
                    File selectedFile = new File(selectedPath);
                    updateStatus(id, edtRevision.getText().toString(), "Perlu Revisi", selectedFile);
                }
            }
        });

        btnViewFile.setOnClickListener(v -> {
            String nama_file = getIntent().getStringExtra("nama_file");
            Intent intent = new Intent(DetailThesisActivity.this, WebViewActivity.class);
            intent.putExtra("nama_file", nama_file);
            startActivity(intent);
        });

        tvImageName.setOnClickListener(v -> {
            String path = "images/" + revisi_gambar;
            Intent intent = new Intent(DetailThesisActivity.this, ImageViewActivity.class);
            intent.putExtra("pathImage", path);
            startActivity(intent);
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
    }


    private void updateStatus(String skripsiId, String revisi, String status, File file) {
        loadingView.setVisibility(View.VISIBLE);
        apiRepository.updateStatus(skripsiId, status, revisi, file, new ApiResponseCallback<MessageItem>() {
            @Override
            public void onSuccess(MessageItem responseData) {

                loadingView.setVisibility(View.GONE);
                Intent intent = new Intent(DetailThesisActivity.this, LectureHomeActivity.class);
                startActivity(intent);
                DetailThesisActivity.this.finishAffinity();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle login error

                loadingView.setVisibility(View.GONE);
                showErrorDialog(errorMessage);

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
                Intent intent = new Intent(DetailThesisActivity.this, LectureHomeActivity.class);
                startActivity(intent);
                DetailThesisActivity.this.finishAffinity();

            }

            @Override
            public void onError(String errorMessage) {
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    private void showErrorDialog(String message) {
        new AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.ERROR)
                .setTitle("Kesalahan")
                .setMessage(message)
                .show();
    }

    private void showSuccess(String message) {
        new AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.SUCCESS)
                .setTitle("Berhasil")
                .setMessage(message)
                .show();
    }

}