package com.example.myoutfit;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Item {
    String image;
    String name;
    int season;
    Float levelTaste;
    int typeOfClothe;

    public Item(String image, String name, int season, Float levelTaste, int partOfBody) {
        this.image = image;
        this.name = name;
        this.season = season;
        this.levelTaste = levelTaste;
        this.typeOfClothe = partOfBody;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getSeason() {
        return season;
    }

    public Float getLevelTaste() {
        return levelTaste;
    }

    public int getTypeOfClothe() {
        return typeOfClothe;
    }


}
