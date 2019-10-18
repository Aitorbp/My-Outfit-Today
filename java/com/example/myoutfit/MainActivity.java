package com.example.myoutfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.myoutfit.data.datamodel.WeatherModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    ItemsClothes itemsClothes;
    Toolbar toolbar;
    NavigationView navigationView;
    Storage storage;
    Retrofit retrofit;
    WeatherAPI weatherAPI; //Acceso a los métodos de la api del tiempo
    WeatherModel weatherModel = new WeatherModel(); //Objeto con los modelos de datos que recibe la API del tiempo
    String keyWeather= "9aa4f8a0b09c4034b44183812191306"; //Clave para la API Apixu
    String locationCurrent; //Variable para saber la temperatura que le pasamos por parametro
    float temp;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




        //Con este código cargamos los datos
        storage = new Storage(this);
        itemsClothes = storage.load();
        if (itemsClothes == null) {
            itemsClothes = new ItemsClothes();
        }


        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            return;
        }


/***
 * Utilizamos retrofit para acceder a la API, ponemos la url base
 * y creamos la clase. Utilizamos también HttpLoginInterceptor para conocer comos e cuentra la
 * petición y que movimientos hace.
 */
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptor.getLevel();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.apixu.com/v1/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        weatherAPI =retrofit.create(WeatherAPI.class);

        fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    locationCurrent = getAddress(latitude, longitude);
                    Log.d("tag", String.valueOf(locationCurrent));
                    weatherGET(keyWeather, locationCurrent);
                    Log.d("````", String.valueOf(locationCurrent));
                }
            }
        });

        //Abrimos la app con esta pantalla
        changeSecreen(R.id.nav_myCloset);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      int screen =  menuItem.getItemId();
        changeSecreen(screen);
        return true;
    }

    public void changeSecreen(int screen){
        switch (screen) {
            case R.id.nav_outfitToday:
                FragmentOutfitToday fragmentOutfitToday = new FragmentOutfitToday();
                addFragment(fragmentOutfitToday);
                recieveCity(locationCurrent, fragmentOutfitToday);
                recieveTemperature(temp, fragmentOutfitToday);
                recieveDataInFragmentOutFit(itemsClothes.objectItems, fragmentOutfitToday);
                storage.save(itemsClothes);

                break;

            case R.id.nav_myCloset:
                FragmentMyCloset fragmentMyCloset = new FragmentMyCloset();
                addFragment(fragmentMyCloset);
                recieveDataInFragmentCloset(itemsClothes.objectItems, fragmentMyCloset);
                storage.save(itemsClothes);
                break;

            case R.id.nav_addArticle:
                addFragment(new FragmentAddArticle());
                break;

        }
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
        drawerLayout.closeDrawer(Gravity.START); //para cerrar el menu lateral cuando pulsemos un boton
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /****
     * Método para pasar los dattos al FragmentMyCloset Y AL FragmentOutfit
     */
    public void sendItem(Item item) {
        if (itemsClothes == null) {
            itemsClothes = new ItemsClothes();
        }
        if (item != null) {
            itemsClothes.objectItems.add(item);
        }
    }
    public void recieveDataInFragmentCloset(ArrayList<Item> items, FragmentMyCloset fragment) {

        if (items != null)
            fragment.sendItem(items);
    }
    /****
     * Método para pasar los dattos al FragmentOutfit
     */
    public void recieveDataInFragmentOutFit(ArrayList<Item> items, FragmentOutfitToday fragment) {

        if (items != null)
            fragment.sendItem(items);
    }



    @Override
    protected void onPause() {
        super.onPause();
            storage.save(itemsClothes);
     }

    /**
     * Creamos el método get para hacer la llamada al servidor del WEATHER*/

    void weatherGET(String key, final String location){
        Call<WeatherModel> call = weatherAPI.getWeather(key, location);
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                weatherModel = response.body();
                temp = (float) weatherModel.getCurrent().temp_c;
                Log.d("000000", String.valueOf(weatherModel.getCurrent().temp_c));
            }
            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {

            }
        });
    }

    /**
     * Método para enviar solo la temperatura*/

    public void recieveTemperature(float temperature, FragmentOutfitToday fragment){
        fragment.sendTemperature(temperature);
    }

    /**
     * Método para enviar el texto de la ciudad**/
    public void recieveCity(String locationCurrent, FragmentOutfitToday fragment){
        fragment.sendCityName(locationCurrent);
    }

/**
 * Permiso para la localización*/
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }

/****
 * Transforma la latitud y longitud en una ciudad
 */
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }



}
