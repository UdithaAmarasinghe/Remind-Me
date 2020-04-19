package com.remindme.services.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.remindme.models.ItemList;

import java.util.ArrayList;
import java.util.List;

public class ItemListResponse extends Response {

    @SerializedName("data")
    @Expose
    private ArrayList<ItemList> data;

    public ArrayList<ItemList> getData() {
        return data;
    }

    public void setData(ArrayList<ItemList> data) {
        this.data = data;
    }
}
