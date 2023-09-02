package com.next.up.code.myskripsi.ui.lecturer.check.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.mingle.widget.LoadingView;
import com.next.up.code.myskripsi.R;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.web_view);
        loadingView = findViewById(R.id.load_view_pdf_reader);

        String fileName = getIntent().getStringExtra("nama_file");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new Callback());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false); // Menyembunyikan tombol zoom

        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + "https://mobileapi12.000webhostapp.com/uploads/" + fileName);

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingView.setVisibility(view.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            new AestheticDialog.Builder(WebViewActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                    .setTitle("Kesalahan")
                    .setMessage("Sepertinya koneksi jaringan internet kamu buruk")
                    .show();
        }
    }
}