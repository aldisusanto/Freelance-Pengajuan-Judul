package com.next.up.code.myskripsi.core.data.api.response;

import com.google.gson.annotations.SerializedName;

public class ResponseData<T> {
    private String message;
    private boolean success;

    @SerializedName("data")
    private T item;

    public ResponseData(String message, boolean success, T item) {
        this.message = message;
        this.success = success;
        this.item = item;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getItem() {
        return item;
    }
}
