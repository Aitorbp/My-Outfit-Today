package com.example.myoutfit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Random;

public class FragmentMyCloset extends Fragment {


    AdapterImageGallery adapterImageGallery;
    GridView gridView;
    ArrayList<Item> elementItemArray = new ArrayList<>();
    MainActivity mainActivity  = (MainActivity) getActivity();
    int positionSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_my_closet, container, false);

        gridView = (GridView) RootView.findViewById((R.id.gridView));
        adapterImageGallery= new AdapterImageGallery(getActivity().getApplicationContext(), R.layout.item_layout, elementItemArray);
        gridView.setAdapter(adapterImageGallery);

        /****
         * Código para borrar un item
         */

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                elementItemArray.remove(position);

                adapterImageGallery.notifyDataSetChanged();

                return true;
            }
        });


        return RootView;
    }

    public void sendItem(ArrayList<Item> items) {
        if(items !=null){
            elementItemArray = items;
        }
    }

}
