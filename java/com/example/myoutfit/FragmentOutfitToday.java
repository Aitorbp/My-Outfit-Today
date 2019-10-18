package com.example.myoutfit;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class FragmentOutfitToday extends Fragment {
    Item partUpFinal;
    Item partMiddleFinal;
    Item partFloorFinal;
    String up;
    String middle;
    String floor;
    ImageView imagePartUp;
    ImageView imagePartMiddle;
    ImageView imagePartFloor;
    ArrayList<Item> elementItemArray = new ArrayList<>();
    ArrayList<Item> partUpRamdon= new ArrayList<Item>();
    ArrayList<Item> partMiddleRamdon = new ArrayList<Item>();
    ArrayList<Item> partFloorRamdon= new ArrayList<Item>();
    ArrayList<Item> partUp = new ArrayList<Item>();
    ArrayList<Item> partMiddle = new ArrayList<Item>();
    ArrayList<Item> partFloor = new ArrayList<Item>();
    ArrayList<Item> filterWithTemp = new ArrayList<Item>();
    float currentTemperature;
    Button updateClothe;
    ImageView imagenWeather;
    TextView weatherTemp;
    TextView nameCity;
    String currentCity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_outfit_today, container, false);
        imagePartUp = (ImageView) RootView.findViewById(R.id.partUp);
        imagePartMiddle = (ImageView) RootView.findViewById(R.id.partMiddle);
        imagePartFloor = (ImageView) RootView.findViewById(R.id.partFloor);
        updateClothe= (Button)RootView.findViewById(R.id.updateClothe);
        imagenWeather= (ImageView) RootView.findViewById(R.id.weather);
        weatherTemp = (TextView) RootView.findViewById(R.id.weatherTemp);
        nameCity = (TextView) RootView.findViewById(R.id.nameCity);


        final MainActivity mainActivity  = (MainActivity) getActivity();

        setImageWeather(currentTemperature,  imagenWeather);
        weatherTemp.setText(String.valueOf(currentTemperature));
        nameCity.setText(currentCity);
        /**
         * //1. Primero filtramos por temperatura
         * */
        filterTemperature(currentTemperature, elementItemArray);


        /***
         * //Segundo filtramos por la parte de cuerpo
         */
        filterByPartBody(filterWithTemp, partUp, partMiddle, partFloor);
        
        validationUp(partUp, elementItemArray);
        validationFloor(partFloor,elementItemArray);
        validationMiddle(partMiddle,elementItemArray);

        /**
         * 2. Aquí abajo iría le método del ramdon, el random hay que hacerlo por cada parte y tener en cuenta las estrellas*/


        partUpFinal= ramdonClothe(partUp, partUpRamdon);
        partMiddleFinal= ramdonClothe(partMiddle, partMiddleRamdon);
        partFloorFinal= ramdonClothe(partFloor, partFloorRamdon);

        if(partUpFinal == null){
            mainActivity.changeSecreen(R.id.nav_addArticle);
            Toast.makeText(getContext(), "You should add a up item", Toast.LENGTH_SHORT).show();
        }
        else if(partMiddleFinal == null){
            mainActivity.changeSecreen(R.id.nav_addArticle);
            Toast.makeText(getContext(), "You should add a  middle item", Toast.LENGTH_SHORT).show();
        }
        else if(partFloorFinal==null){
            mainActivity.changeSecreen(R.id.nav_addArticle);
            Toast.makeText(getContext(), "You should add a  floor item", Toast.LENGTH_SHORT).show();
        }
        else{
            up = partUpFinal.getImage();
            middle = partMiddleFinal.getImage();
            floor = partFloorFinal.getImage();

            imagePartUp.setImageURI(Uri.parse(up));
            imagePartMiddle.setImageURI(Uri.parse(middle));
            imagePartFloor.setImageURI(Uri.parse(floor));
        }

        updateClothe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeSecreen(R.id.nav_outfitToday);
            }
        });

        return RootView;
    }

    /***
     * 1. Filter: Método para filtrar por temperatura o lo que es lo mismo estación del año
     * */

    public ArrayList<Item> filterTemperature(float temperature, ArrayList<Item> elementItemArray){
        for (int i = 0; i <elementItemArray.size() ; i++) {
            Log.d("222222222222222", String.valueOf(elementItemArray.get(i).getName()));
            Log.d("222222222222222", String.valueOf(elementItemArray.get(i).getLevelTaste()));
            if(elementItemArray.get(i).getSeason() ==0 && temperature <=20){ //Temperatura de invierno
                filterWithTemp.add(elementItemArray.get(i));

            }
            else if (elementItemArray.get(i).getSeason() ==1 &&temperature >=21){ //Temperatura de Otoño
                filterWithTemp.add(elementItemArray.get(i));
            }
        }
        return  filterWithTemp;
    }

    /**
     *Filtro 2: Metodo para separar cada ropa ropa por su parte
     * */
    public void filterByPartBody(ArrayList<Item> elementItemArray,
                                 ArrayList<Item> partUp, ArrayList<Item> partMiddle, ArrayList<Item> partFloor){

        for (int i = 0; i < elementItemArray.size(); i++) {

            if(elementItemArray.get(i).getTypeOfClothe() ==0 || //If de la parte alta del cuerpo
                    elementItemArray.get(i).getTypeOfClothe() ==4 ||
                    elementItemArray.get(i).getTypeOfClothe() == 5 ||
                    elementItemArray.get(i).getTypeOfClothe() == 6 ||
                    elementItemArray.get(i).getTypeOfClothe() == 7){

                partUp.add(elementItemArray.get(i));
            }

            else if(elementItemArray.get(i).getTypeOfClothe()== 1|| // If de la parte media del cuerpo
                    elementItemArray.get(i).getTypeOfClothe() ==2){

                partMiddle.add(elementItemArray.get(i));

            }
            else if(elementItemArray.get(i).getTypeOfClothe() ==3){ // If de la parte baja del cuerpo

                partFloor.add(elementItemArray.get(i));

            }

        }
    }

    /****
     *En este método sacamos un random de la ropa, pero sacando más posibilidades del número con 5 estrellas
     *
     */
    public Item ramdonClothe(ArrayList<Item> elementItemArray, ArrayList<Item> itemRamdonize){

        Item part = null;
        Random rnd = new Random();
        int low = 1;
        int high = 100;
        int result = rnd.nextInt(high-low) + low;


        for (int i = 0; i < elementItemArray.size() ; i++) {

            if(result >= 40){
                if(elementItemArray.get(i).getLevelTaste() == 5.0f || elementItemArray.get(i).getLevelTaste() ==4.0f){
                    itemRamdonize.add(elementItemArray.get(i)); // tenemos el array con la probabilidad. ahora volveos a hacer un ramdon para sacar un Item en concreto
                    Random rndFinal = new Random();
                    part = itemRamdonize.get(rndFinal.nextInt(itemRamdonize.size()));
                }
                else{
                    itemRamdonize.add(elementItemArray.get(i));
                    Random rndFinal = new Random();
                    part = itemRamdonize.get(rndFinal.nextInt(itemRamdonize.size()));
                }
            }
            else if(result <= 39){
                if(elementItemArray.get(i).getLevelTaste()== 3.0f ||
                        elementItemArray.get(i).getLevelTaste() ==2.0f || elementItemArray.get(i).getLevelTaste() == 1.0f){
                    itemRamdonize.add(elementItemArray.get(i));
                    Random rndFinal = new Random();
                    part = itemRamdonize.get(rndFinal.nextInt(itemRamdonize.size()));
                }
                else{
                    itemRamdonize.add(elementItemArray.get(i));
                    Random rndFinal = new Random();
                    part = itemRamdonize.get(rndFinal.nextInt(itemRamdonize.size()));
                }
            }

        }
        return part;
    }

    public void validationUp(ArrayList<Item> partUp, ArrayList<Item> elementItemArray) {
        if (partUp.isEmpty()) {
            for (int j = 0; j < elementItemArray.size(); j++) {
                if (elementItemArray.get(j).getTypeOfClothe() == 0 || //If de la parte alta del cuerpo
                        elementItemArray.get(j).getTypeOfClothe() == 4 ||
                        elementItemArray.get(j).getTypeOfClothe() == 5 ||
                        elementItemArray.get(j).getTypeOfClothe() == 6 ||
                        elementItemArray.get(j).getTypeOfClothe() == 7) {
                    Toast.makeText(getContext(), "You should add an item for this season in part up of the body", Toast.LENGTH_LONG).show();
                    partUp.add(elementItemArray.get(j));
                }
            }
        }
    }

    public void validationMiddle(ArrayList<Item> partMiddle, ArrayList<Item> elementItemArray){
            if (partMiddle.isEmpty()){
                for (int j = 0; j < elementItemArray.size() ; j++) {
                    if(elementItemArray.get(j).getTypeOfClothe()== 1|| // If de la parte media del cuerpo
                            elementItemArray.get(j).getTypeOfClothe() ==2){
                        Toast.makeText(getContext(), "You should add an item for this season in part middle of the body", Toast.LENGTH_LONG).show();
                        partMiddle.add(elementItemArray.get(j));
                    }
                }
            }
        }

    public void validationFloor(ArrayList<Item> partFloor, ArrayList<Item> elementItemArray){
        if (partFloor.isEmpty()){
            for (int j = 0; j < elementItemArray.size() ; j++) {
                if(elementItemArray.get(j).getTypeOfClothe() == 3){
                    Toast.makeText(getContext(), "You should add an item for this season in part floor of the body", Toast.LENGTH_LONG).show();
                    partFloor.add(elementItemArray.get(j));
                }
            }
        }


    }

    public void setImageWeather(float currentTemperature,  ImageView imagenWeather){

        if(currentTemperature>= 19){
            imagenWeather.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.sunny));
        }
        else{
            imagenWeather.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.snowflake));
        }
    }


    public void sendItem(ArrayList<Item> items) {
        if(elementItemArray!=null){
            elementItemArray =items;
        }
    }

    public void sendTemperature(float temperature) {
        currentTemperature =temperature;
    }

    public void sendCityName(String locationCurrent) {
        currentCity =locationCurrent;
    }
}
