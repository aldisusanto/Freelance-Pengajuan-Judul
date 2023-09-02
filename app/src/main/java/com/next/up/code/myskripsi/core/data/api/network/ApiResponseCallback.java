package com.next.up.code.myskripsi.core.data.api.network;

public interface ApiResponseCallback<T> {
    void onSuccess(T data);
    void onError(String errorMessage);
}
