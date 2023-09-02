package com.next.up.code.myskripsi.core.data.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.next.up.code.myskripsi.core.data.api.network.ApiManager;
import com.next.up.code.myskripsi.core.data.api.network.ApiResponseCallback;
import com.next.up.code.myskripsi.core.data.api.network.ApiService;
import com.next.up.code.myskripsi.core.data.api.response.SkripsiResponse;
import com.next.up.code.myskripsi.core.data.api.response.item.LoginItem;
import com.next.up.code.myskripsi.core.data.api.response.item.MessageItem;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private final ApiService apiService;

    public ApiRepository() {
        apiService = ApiManager.getInstance().getApiService();
    }

    public void login(String requestBodyJson, ApiResponseCallback<LoginItem> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);

        Call<LoginItem> call = apiService.login(requestBody);

        // Tambahkan log sebelum melakukan enqueue
        Log.d("ApiRepository", "Login API Request: " + call.request().toString());

        call.enqueue(new Callback<LoginItem>() {
            @Override
            public void onResponse(Call<LoginItem> call, Response<LoginItem> response) {
                Log.d("ApiRepository", "Login API Response: " + response.toString());

                if (response.isSuccessful()) {
                    LoginItem responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else {
                    // Handle unsuccessful response
                    callback.onError("Login failed");
                }
            }

            @Override
            public void onFailure(Call<LoginItem> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Login API Request Failed: " + t.getMessage());
            }
        });
    }

    public void uploadSkripsi(String statusValue, String judulValue, String mahasiswaIdValue, File file, ApiResponseCallback<MessageItem> callback) {

        RequestBody statusBody = RequestBody.create(MediaType.parse("text/plain"), statusValue);
        RequestBody judulBody = RequestBody.create(MediaType.parse("text/plain"), judulValue);
        RequestBody mahasiswaIdBody = RequestBody.create(MediaType.parse("text/plain"), mahasiswaIdValue);
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);

        Call<MessageItem> call = apiService.upload(statusBody, judulBody, mahasiswaIdBody, filePart);

        call.enqueue(new Callback<MessageItem>() {
            @Override
            public void onResponse(@NonNull Call<MessageItem> call, Response<MessageItem> response) {
                Log.d("ApiRepository", "Upload API Response: " + response.toString());

                if (response.isSuccessful()) {
                    MessageItem responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else if (response.code() == 400) {
                    Log.d("ApiRepository", "Upload API Failed - Status 400: " + response);
                    callback.onError("Upload failed - Status 400");
                } else if (response.code() == 404) {
                    Log.d("ApiRepository", "Upload API Failed - Status 404: " + response);
                    callback.onError("Upload failed - Status 404");
                } else {
                    // Handle other unsuccessful responses
                    callback.onError("Upload failed - Other status");
                }
            }

            @Override
            public void onFailure(Call<MessageItem> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Upload API Request Failed: " + t.getMessage());
            }
        });
    }

    public void editSkripsi(String skripsiIdValue, String statusValue, String judulValue, String mahasiswaIdValue, File file, ApiResponseCallback<MessageItem> callback) {

        RequestBody skripsiIdBody = RequestBody.create(MediaType.parse("text/plain"), skripsiIdValue);
        RequestBody statusBody = RequestBody.create(MediaType.parse("text/plain"), statusValue);
        RequestBody judulBody = RequestBody.create(MediaType.parse("text/plain"), judulValue);
        RequestBody mahasiswaIdBody = RequestBody.create(MediaType.parse("text/plain"), mahasiswaIdValue);
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);

        Call<MessageItem> call = apiService.editSkripsi(skripsiIdBody, statusBody, judulBody, mahasiswaIdBody, filePart);

        call.enqueue(new Callback<MessageItem>() {
            @Override
            public void onResponse(@NonNull Call<MessageItem> call, Response<MessageItem> response) {
                Log.d("ApiRepository", "Upload API Response: " + response.toString());

                if (response.isSuccessful()) {
                    MessageItem responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else if (response.code() == 400) {
                    Log.d("ApiRepository", "Upload API Failed - Status 400: " + response);
                    callback.onError("Upload failed - Status 400");
                } else if (response.code() == 404) {
                    Log.d("ApiRepository", "Upload API Failed - Status 404: " + response);
                    callback.onError("Upload failed - Status 404");
                } else {
                    // Handle other unsuccessful responses
                    callback.onError("Upload failed - Other status");
                }
            }

            @Override
            public void onFailure(Call<MessageItem> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Upload API Request Failed: " + t.getMessage());
            }
        });
    }

    public void getSkripsiList(int mahasiswaId, ApiResponseCallback<SkripsiResponse> callback) {
        Call<SkripsiResponse> call = apiService.getSkripsiList(mahasiswaId);

        call.enqueue(new Callback<SkripsiResponse>() {
            @Override
            public void onResponse(Call<SkripsiResponse> call, Response<SkripsiResponse> response) {
                Log.d("ApiRepository", "Get Skripsi List API Response: " + response.toString());

                if (response.isSuccessful()) {
                    SkripsiResponse responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else if (response.code() == 400) {
                    Log.d("ApiRepository", "Get Skripsi List API Failed - Status 400: " + response);
                    callback.onError("Get Skripsi List failed - Status 400");
                } else if (response.code() == 404) {
                    Log.d("ApiRepository", "Get Skripsi List API Failed - Status 404: " + response);
                    callback.onError("Get Skripsi List failed - Status 404");
                } else {
                    // Handle other unsuccessful responses
                    callback.onError("Get Skripsi List failed - Other status");
                }
            }

            @Override
            public void onFailure(Call<SkripsiResponse> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Get Skripsi List API Request Failed: " + t.getMessage());
            }
        });
    }

    public void getAllSkripsiList(ApiResponseCallback<SkripsiResponse> callback) {
        Call<SkripsiResponse> call = apiService.getAllSkripsiList();

        call.enqueue(new Callback<SkripsiResponse>() {
            @Override
            public void onResponse(Call<SkripsiResponse> call, Response<SkripsiResponse> response) {
                Log.d("ApiRepository", "Get Skripsi List API Response: " + response.toString());

                if (response.isSuccessful()) {
                    SkripsiResponse responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else if (response.code() == 400) {
                    Log.d("ApiRepository", "Get Skripsi List API Failed - Status 400: " + response);
                    callback.onError("Get Skripsi List failed - Status 400");
                } else if (response.code() == 404) {
                    Log.d("ApiRepository", "Get Skripsi List API Failed - Status 404: " + response);
                    callback.onError("Get Skripsi List failed - Status 404");
                } else {
                    // Handle other unsuccessful responses
                    callback.onError("Get Skripsi List failed - Other status");
                }
            }

            @Override
            public void onFailure(Call<SkripsiResponse> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Get Skripsi List API Request Failed: " + t.getMessage());
            }
        });
    }

    public void getAllHistory(ApiResponseCallback<SkripsiResponse> callback) {
        Call<SkripsiResponse> call = apiService.getAllHistory();

        call.enqueue(new Callback<SkripsiResponse>() {
            @Override
            public void onResponse(Call<SkripsiResponse> call, Response<SkripsiResponse> response) {
                Log.d("ApiRepository", "Get Skripsi List API Response: " + response.toString());

                if (response.isSuccessful()) {
                    SkripsiResponse responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else if (response.code() == 400) {
                    Log.d("ApiRepository", "Get Skripsi List API Failed - Status 400: " + response);
                    callback.onError("Get Skripsi List failed - Status 400");
                } else if (response.code() == 404) {
                    Log.d("ApiRepository", "Get Skripsi List API Failed - Status 404: " + response);
                    callback.onError("Get Skripsi List failed - Status 404");
                } else {
                    // Handle other unsuccessful responses
                    callback.onError("Get Skripsi List failed - Other status");
                }
            }

            @Override
            public void onFailure(Call<SkripsiResponse> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Get Skripsi List API Request Failed: " + t.getMessage());
            }
        });
    }

    public void delete(String requestBodyJson, ApiResponseCallback<MessageItem> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);

        Call<MessageItem> call = apiService.delete(requestBody);

        // Tambahkan log sebelum melakukan enqueue
        Log.d("ApiRepository", "Login API Request: " + call.request().toString());

        call.enqueue(new Callback<MessageItem>() {
            @Override
            public void onResponse(Call<MessageItem> call, Response<MessageItem> response) {
                Log.d("ApiRepository", "Login API Response: " + response.toString());

                if (response.isSuccessful()) {
                    MessageItem responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else {
                    // Handle unsuccessful response
                    callback.onError("Login failed");
                }
            }

            @Override
            public void onFailure(Call<MessageItem> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Login API Request Failed: " + t.getMessage());
            }
        });
    }

    public void updateStatus(String skripsiIdValue, String statusValue, String revisiValue, File file, ApiResponseCallback<MessageItem> callback) {

        RequestBody skripsiIdBody = RequestBody.create(MediaType.parse("text/plain"), skripsiIdValue);
        RequestBody statusBody = RequestBody.create(MediaType.parse("text/plain"), statusValue);
        RequestBody revisiBody = RequestBody.create(MediaType.parse("text/plain"), revisiValue);

        MultipartBody.Part filePart = null;
        if (file != null) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/image"), file);
            filePart = MultipartBody.Part.createFormData("revisi_gambar", file.getName(), fileRequestBody);
        }

        Call<MessageItem> call = apiService.updateStatus(skripsiIdBody, statusBody, revisiBody, filePart);

        call.enqueue(new Callback<MessageItem>() {
            @Override
            public void onResponse(@NonNull Call<MessageItem> call, Response<MessageItem> response) {
                Log.d("ApiRepository", "Upload API Response: " + response.toString());

                if (response.isSuccessful()) {
                    MessageItem responseData = response.body();

                    if (responseData != null) {
                        callback.onSuccess(responseData);
                    }
                } else if (response.code() == 400) {
                    Log.d("ApiRepository", "Upload API Failed - Status 400: " + response);
                    callback.onError("Upload failed - Status 400");
                } else if (response.code() == 404) {
                    Log.d("ApiRepository", "Upload API Failed - Status 404: " + response);
                    callback.onError("Upload failed - Status 404");
                } else {
                    // Handle other unsuccessful responses
                    callback.onError("Upload failed - Other status");
                }
            }

            @Override
            public void onFailure(Call<MessageItem> call, Throwable t) {
                // Handle request failure
                callback.onError("Request failed");

                // Tambahkan log jika permintaan gagal
                Log.e("ApiRepository", "Upload API Request Failed: " + t.getMessage());
            }
        });
    }


}
