package com.next.up.code.myskripsi.core.data.api.response.item;

import com.google.gson.annotations.SerializedName;

public class SkripsiItem {
    @SerializedName("id")
    private String id;

    @SerializedName("judul")
    private String judul;

    @SerializedName("status")
    private String status;

    @SerializedName("nama_file")
    private String namaFile;

    @SerializedName("revisi")
    private String revisi;
    @SerializedName("revisi_gambar")
    private String revisi_gambar;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("nama")
    private String namaMahasiswa;

    @SerializedName("nim")
    private String nim;

    @SerializedName("created_time_ago")
    private String createdTimeAgo;

    public SkripsiItem(String id, String judul, String status, String namaFile, String namaMahasiswa, String nim, String createdTimeAgo, String revisi, String revisi_gambar) {
        this.id = id;
        this.judul = judul;
        this.status = status;
        this.namaFile = namaFile;
        this.namaMahasiswa = namaMahasiswa;
        this.nim = nim;
        this.createdTimeAgo = createdTimeAgo;
        this.revisi = revisi;
        this.revisi_gambar = revisi_gambar;
    }

    public String getRevisi_gambar() {
        return revisi_gambar;
    }

    // Buat getter dan setter sesuai kebutuhan

    public String getId() {
        return id;
    }

    public String getNim() {
        return nim;
    }

    public String getStatus() {
        return status;
    }

    public String getJudul() {
        return judul;
    }

    public String getCreatedTimeAgo() {
        return createdTimeAgo;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }

    public String getRevisi() {
        return revisi;
    }
    // Metode getter dan setter lainnya
}

