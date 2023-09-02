package com.next.up.code.myskripsi;

import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREFERENCES_NAME;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_ID_USER;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_LOGIN_STATUS;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_NAMA_USER;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_NIM;
import static com.next.up.code.myskripsi.core.conf.BaseSharedPreferences.PREF_ROLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.core.data.api.ApiRepository;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.response.item.LoginItem;
import com.next.up.code.myskripsi.ui.lecturer.home.LectureHomeActivity;
import com.next.up.code.myskripsi.ui.student.home.StudentHomeActivity;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextInputEditText etNim, etPassword;
    private ApiRepository apiRepository;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LoadingView loadingView;

    private TextView tvForgotPassword;
    private Spinner spinnerLevel;
    private AutoCompleteTextView acLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiRepository = new ApiRepository();
        btnLogin = findViewById(R.id.btn_login);
        etNim = findViewById(R.id.edt_nis_nip);
        etPassword = findViewById(R.id.edt_password);
        loadingView = findViewById(R.id.load_view_login);
        spinnerLevel = findViewById(R.id.spinner_level);
        acLevel = findViewById(R.id.ac_level);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        preferencesSetup();
        cekStatusLogin();
        setAction();
        setLevel();
        tvForgotPassword.setOnClickListener(v -> {
            forgotPassword();
        });
    }

    private void forgotPassword() {
        // URL yang ingin Anda buka
        String url = "https://siaka.undipa.ac.id/index.php?page=lostpass";

        // Buat Uri dari URL
        Uri webpage = Uri.parse(url);

        // Buat Intent untuk membuka browser web
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        // Periksa apakah ada aplikasi browser yang dapat menangani intent ini
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void preferencesSetup() {
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void savePreferencesValue(String idUser, String role, String nama, String nim) {
        editor.putString(PREF_ID_USER, idUser);
        editor.putString(PREF_NIM, nim);
        editor.putString(PREF_NAMA_USER, nama);
        editor.putString(PREF_ROLE, role);
        editor.putBoolean(PREF_LOGIN_STATUS, true);
        editor.apply();
    }


    private void setAction() {
        btnLogin.setOnClickListener(view -> {
            if (!Objects.requireNonNull(etNim.getText()).toString().isEmpty() || !Objects.requireNonNull(etPassword.getText()).toString().isEmpty()) {
                cekLogin(etNim.getText().toString(), Objects.requireNonNull(etPassword.getText()).toString(), acLevel.getText().toString().toLowerCase());
                loadingView.setVisibility(View.VISIBLE);
            } else {
                showErrorDialog("Semua form harus di isi!");
            }
        });

    }

    private void cekStatusLogin() {
        try {
            if (sharedPreferences.getBoolean(PREF_LOGIN_STATUS, false)) {
                if (sharedPreferences.getString(PREF_ROLE, "").equals("mahasiswa")) {
                    startActivity(new Intent(this, StudentHomeActivity.class));
                    MainActivity.this.finish();
                } else {
                    startActivity(new Intent(this, LectureHomeActivity.class));
                    MainActivity.this.finish();
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setLevel() {
        ArrayList<String> arrayString = new ArrayList<>();
        arrayString.add("Select Level");
        arrayString.add("Mahasiswa");
        arrayString.add("Kaprodi");
        ArrayAdapter<String> dropAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, arrayString);
        spinnerLevel.setAdapter(dropAdapter);
        acLevel.setOnClickListener(view -> spinnerLevel.performClick());

        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String levelName = arrayString.get(position);
                    acLevel.setText(levelName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not yet implemented
            }
        });
    }


    private void cekLogin(String nim, String password, String role) {
        String requestBodyJson = "{\"nim\":\"" + nim + "\",\"password\":\"" + password + "\",\"role\":\"" + role + "\"}";
        apiRepository.login(requestBodyJson, new ApiResponseCallback<LoginItem>() {
            @Override
            public void onSuccess(LoginItem responseData) {
                Integer userId = responseData.getData().getId();
                String userName = responseData.getData().getNamaMahasiswa();
                String userRole = responseData.getData().getRole();
                String nim = responseData.getData().getNim();


                if (Objects.equals(userRole, "mahasiswa")) {
                    Intent intent = new Intent(MainActivity.this, StudentHomeActivity.class);
                    startActivity(intent);
                } else if (Objects.equals(userRole, "kaprodi")) {
                    Intent intent = new Intent(MainActivity.this, LectureHomeActivity.class);
                    startActivity(intent);
                } else {
                    showErrorDialog("Invalid Level");
                }

                savePreferencesValue(userId.toString(), userRole, userName, nim);

            }

            @Override
            public void onError(String errorMessage) {
                // Handle login error

                loadingView.setVisibility(View.GONE);
                showErrorDialog(errorMessage);

            }
        });
    }

    private void showErrorDialog(String message) {
        new AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.ERROR)
                .setTitle("Kesalahan")
                .setMessage(message)
                .show();
    }

}

