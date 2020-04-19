package com.remindme.services.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.remindme.models.Token;

public class TokenResponse extends Response {

    @SerializedName("data")
    @Expose
    private Token data;

    public Token getData() {
        return data;
    }

    public void setData(Token data) {
        this.data = data;
    }

}
