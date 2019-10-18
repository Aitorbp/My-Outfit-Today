package com.example.myoutfit;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterImageGallery extends ArrayAdapter {

    Context context;
    int item_Layaut;
    ArrayList<Item> data;

    public AdapterImageGallery(@NonNull Context context, int item_Layaut,   ArrayList data) {
        super(context, item_Layaut, data);
        this.context = context;
        this.item_Layaut = item_Layaut;
        this.data = data;
    }
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(item_Layaut, parent, false);
        }

//        String season = data.get(position).getSeason();
//        TextView elementSeason = convertView.findViewById(R.id.seasonsItem);
//        elementSeason.setText(season);
//
//        String typeClothes = data.get(position).getTypeOfClothe();
//        TextView elementTypeClothes = convertView.findViewById(R.id.seasonsItem);
//        elementTypeClothes.setText(typeClothes);

        float tasteLevel = data.get(position).getLevelTaste();
        RatingBar elementRatingBar =convertView.findViewById(R.id.ratingBarItem);
        elementRatingBar.setRating(tasteLevel);

        String image = data.get(position).getImage();
        Uri imageGallery = Uri.parse(image);
        ImageView elementImage = convertView.findViewById(R.id.imageItem);
        elementImage.setImageURI(imageGallery);

        String name = data.get(position).getName();
        TextView elementName = convertView.findViewById(R.id.nameItem);
        elementName.setText(name);

        return convertView;
    }



}
