package com.next.up.code.myskripsi.ui.student.form;

import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREFERENCES_NAME;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_ID_USER;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_NAMA_USER;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_NIM;

import android.app.DownloadManager;
import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.R;
import com.next.up.code.myskripsi.core.data.api.ApiRepository;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.response.item.MessageItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FormActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> resultLauncher;

    private SharedPreferences sharedPreferences;
    private String idUser, userNim, userName;
    private ImageView btnBack, btnClose;
    private TextInputEditText edtName, edtNim, edtTitle;
    private Button btnDownload, btnSubmit;
    private FrameLayout fmLayout;
    private TextView tvFilename;
    private String selectedPath;
    private LoadingView loadingView;

    private ApiRepository apiRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        apiRepository = new ApiRepository();
        btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.title);
        tvFilename = findViewById(R.id.tv_filename);
        btnClose = findViewById(R.id.btn_close_file);
        edtName = findViewById(R.id.edt_name);
        edtNim = findViewById(R.id.edt_nim);
        edtTitle = findViewById(R.id.edt_title);
        btnDownload = findViewById(R.id.btn_download_form);
        btnSubmit = findViewById(R.id.btn_submit_form);
        fmLayout = findViewById(R.id.fl_upload);
        loadingView = findViewById(R.id.load_form);
        tvTitle.setText(R.string.text_form);
        setupMainButton();
        preferencesSetup();
        setupEditText();
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
                fmLayout.setVisibility(View.GONE);
                tvFilename.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.VISIBLE);
                tvFilename.setText(fileName);
                selectedPath = saveFileToLocalStorage(uri);

            }
        });

    }

    private void preferencesSetup() {
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        idUser = sharedPreferences.getString(PREF_ID_USER, "");
        userName = sharedPreferences.getString(PREF_NAMA_USER, "");
        userNim = sharedPreferences.getString(PREF_NIM, "");

    }

    private void setupEditText() {
        edtName.setText(userName);
        edtNim.setText(userNim);
    }


    private void setupMainButton() {
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnDownload.setOnClickListener(v -> {
            String fileUrl = "https://mobileapi12.000webhostapp.com/uploads/form-pengajuan.docx";
            downloadForm(fileUrl);
        });

        fmLayout.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(FormActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FormActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                selectPDF();
            }
        });

        btnClose.setOnClickListener(v -> {
            fmLayout.setVisibility(View.VISIBLE);
            tvFilename.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
            selectedPath = null;
        });

        btnSubmit.setOnClickListener(v -> {
            if (selectedPath != null) {

                String statusValue = "Pending";
                String judulValue = edtTitle.getText().toString();
                String mahasiswaIdValue = idUser; // Replace with appropriate student ID
                if (judulValue.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    File selectedFile = new File(selectedPath);
                    upload(statusValue, judulValue, mahasiswaIdValue, selectedFile);
                    loadingView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void downloadForm(String url) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri documentURI = Uri.parse(url);

        DownloadManager.Request getDocument = new DownloadManager.Request(documentURI);
        String fileName = "FR-Pengajuan-Judul-Skripsi-TA-2022-2023";
        String fullFilename = fileName + ".docx";
        getDocument.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fullFilename);
        getDocument.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(getDocument);
        Toast.makeText(this, "Form pengajuan di download", Toast.LENGTH_LONG).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPDF();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    private void upload(String statusValue, String judulValue, String mahasiswaIdValue, File file) {
        apiRepository.uploadSkripsi(statusValue, judulValue, mahasiswaIdValue, file, new ApiResponseCallback<MessageItem>() {
            @Override
            public void onSuccess(MessageItem response) {
                loadingView.setVisibility(View.GONE);
                Toast.makeText(FormActivity.this, "File dan data berhasil di unggah", Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Error", errorMessage);
                loadingView.setVisibility(View.GONE);
            }
        });
    }


}


