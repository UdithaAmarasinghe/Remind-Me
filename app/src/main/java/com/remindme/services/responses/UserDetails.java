package com.remindme.services.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.remindme.models.User;

public class UserDetails extends Response{

    @SerializedName("data")
    @Expose
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

}
