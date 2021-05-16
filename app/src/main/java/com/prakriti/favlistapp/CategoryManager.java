package com.prakriti.favlistapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class CategoryManager {
// save data to Shared Preferences (SP)

    private Context context;

    public CategoryManager(Context context) {
        this.context = context;
    }

    // save categories to SP
    public void saveCategory(Category category) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

            // to convert arraylist to set
        HashSet itemsHashSet = new HashSet(category.getItems());

            // saving key - value pairs using SP
            // category and items inside it -> string set (no duplicate values)
        editor.putStringSet(category.getName(), itemsHashSet);
        editor.apply();
    }

    public ArrayList<Category> retrieveCategories() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            // ? refers to data of any type
        Map<String, ?> data = sharedPreferences.getAll();

        ArrayList<Category> categories = new ArrayList<>();
        for(Map.Entry<String, ?> entry : data.entrySet()) {
                // getValue is cast to HashSet (bcoz HashSet was passed in saveCategory() method) and converted to ArrayList
            categories.add(new Category(entry.getKey(), new ArrayList<String>((HashSet) entry.getValue())));
        }
        return categories;
    }
}