package com.next.up.code.myskripsi.core.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.next.up.code.myskripsi.core.data.api.response.item.SkripsiItem;

import java.util.List;

public class SkripsiResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<SkripsiItem> skripsiList;

    public String getMessage() {
        return message;
    }

    public List<SkripsiItem> getSkripsiList() {
        return skripsiList;
    }
}

