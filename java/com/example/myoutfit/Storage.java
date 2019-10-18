package com.example.myoutfit;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Storage {
    private SharedPreferences preferences;
    private Gson gson;

    Storage(Context context) {
        gson = new Gson();
        preferences = context.getSharedPreferences("Clothes", Context.MODE_PRIVATE);
    }

    public void save(ItemsClothes items) {
        String json = toJson(items);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("data", json);
        editor.commit();
    }

    public ItemsClothes load() {
        String json = preferences.getString("data", "");
        return fromJson(json);
    }

    public String toJson(ItemsClothes items) {
        return gson.toJson(items);
    }

    public ItemsClothes fromJson(String json) {
        return gson.fromJson(json, ItemsClothes.class);
    }

}
