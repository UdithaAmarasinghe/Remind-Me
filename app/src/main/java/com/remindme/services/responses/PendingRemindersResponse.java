package com.remindme.services.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.remindme.models.PendingList;

import java.util.ArrayList;
import java.util.List;

public class PendingRemindersResponse extends Response {

    @SerializedName("data")
    @Expose
    private ArrayList<PendingList> data = null;

    public ArrayList<PendingList> getData() {
        return data;
    }

    public void setData(ArrayList<PendingList> data) {
        this.data = data;
    }

}
