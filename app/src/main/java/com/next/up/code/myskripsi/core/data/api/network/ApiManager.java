package com.next.up.code.myskripsi.core.data.api.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static ApiManager instance;
    private final ApiService apiService;

    private ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mobileapi12.000webhostapp.com/") // Ganti dengan base URL Anda
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
