package com.remindme.models;

import java.io.Serializable;

public class CategoryList implements Serializable {

    private String id;
    private String name;
    private String color;
    private int image;

    public CategoryList(String id, String name, String color, int image) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
