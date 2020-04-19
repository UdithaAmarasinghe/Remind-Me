package com.remindme.services.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.remindme.models.PushReminderList;

import java.util.ArrayList;
import java.util.List;

public class PushRemindersResponse extends Response {

    @SerializedName("data")
    @Expose
    private ArrayList<PushReminderList> data = null;

    public ArrayList<PushReminderList> getData() {
        return data;
    }

    public void setData(ArrayList<PushReminderList> data) {
        this.data = data;
    }
}
