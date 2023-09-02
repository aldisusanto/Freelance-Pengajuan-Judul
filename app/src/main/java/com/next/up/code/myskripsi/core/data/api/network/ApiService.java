package com.next.up.code.myskripsi.core.data.api.network;

import com.next.up.code.myskripsi.core.data.api.response.SkripsiResponse;
import com.next.up.code.myskripsi.core.data.api.response.item.LoginItem;
import com.next.up.code.myskripsi.core.data.api.response.item.MessageItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login.php")
    Call<LoginItem> login(@Body RequestBody requestBody);

    @Multipart
    @POST("upload.php")
    Call<MessageItem> upload(
            @Part("status") RequestBody status,
            @Part("judul") RequestBody judul,
            @Part("mahasiswa_id") RequestBody mahasiswaId,
            @Part MultipartBody.Part filePart
    );

    @Multipart
    @POST("edit-skripsi.php")
    Call<MessageItem> editSkripsi(
            @Part("skripsi_id") RequestBody skripsiId,
            @Part("status") RequestBody status,
            @Part("judul") RequestBody judul,
            @Part("mahasiswa_id") RequestBody mahasiswaId,
            @Part MultipartBody.Part filePart
    );

    @GET("get-skripsi.php")
    Call<SkripsiResponse> getSkripsiList(@Query("mahasiswa_id") int mahasiswaId);

    @GET("get-all-skripsi.php")
    Call<SkripsiResponse> getAllSkripsiList();

    @GET("get-all-history.php")
    Call<SkripsiResponse> getAllHistory();

    @POST("delete-skripsi.php")
    Call<MessageItem> delete(@Body RequestBody requestBody);

    @Multipart
    @POST("update-skripsi.php")
    Call<MessageItem> updateStatus(
            @Part("skripsi_id") RequestBody skripsiId,
            @Part("status") RequestBody status,
            @Part("revisi") RequestBody revisi,
            @Part MultipartBody.Part filePart);


}
