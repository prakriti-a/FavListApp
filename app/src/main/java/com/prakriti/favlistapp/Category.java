package com.prakriti.favlistapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
// implements Serializable to pass the category as a parameter to an intent object

    private String name;
    private ArrayList<String> items = new ArrayList<>();

    public Category(String name, ArrayList<String> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getItems() {
        return items;
    }

}
