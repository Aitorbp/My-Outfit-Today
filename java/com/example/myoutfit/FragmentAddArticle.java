package com.example.myoutfit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class FragmentAddArticle extends Fragment {


 ImageView imageView;
 EditText nameItem;
 Spinner spinnerSeasons, spinnerPartBody;
 RatingBar ratingBar;
 Button buttonSend;
 MainActivity mainActivity;
 Item item;
 String nameClothe;
 int season;
 int partBody;
 Dialog dialogGalleryPhoto;
 final int CODIGO_PETICION_GALLERY = 1;
 final int CODIGO_PETICION_CAMERA = 2;
 Bitmap imageGallery, imageCamera, imageChoose, imageSend;
 InputStream stream;
    View contextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_add_article, container, false);

        imageView = (ImageView) RootView.findViewById(R.id.imageCameraGalery);
        nameItem = (EditText) RootView.findViewById(R.id.nameItem);
        spinnerSeasons = (Spinner) RootView.findViewById(R.id.spinnerSeasons);
        spinnerPartBody= (Spinner) RootView.findViewById(R.id.spinnerPartBody);
        ratingBar = (RatingBar) RootView.findViewById(R.id.ratingBar);
        buttonSend = (Button)RootView.findViewById(R.id.sendItemCloset);
        dialogGalleryPhoto = new Dialog(getContext());




        /**
         * Enviamos los datos a través del botón
         * */
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Cogemos el nombre de la prenda*/
                nameClothe = nameItem.getText().toString();

                /**
                 * Validaciones para cuando pulsemos el boton de enviar y no de error***/
                if(imageSend == null){
                    Snackbar.make(buttonSend,R.string.item_fill_image, Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(nameClothe.isEmpty()){
                    Snackbar.make(buttonSend,R.string.item_fill_name, Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                MainActivity mainActivity  = (MainActivity) getActivity();
                mainActivity.sendItem(item = new Item(saveImage(imageSend), nameClothe, season, ratingBar.getRating(), partBody));


                Toast.makeText(getContext(), "Datos guardados", Toast.LENGTH_SHORT).show();

                mainActivity.changeSecreen(R.id.nav_myCloset);

            }
        });


        /**
         *Ponemos un botón para dar la elección de buscar una foto de la galería o
         * de la cámara de fotos
         * */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuPopUpGalleryPhoto(v);

            }
        });

        /**
         * Cogemos el dato del spinner de temporada
         * */
        spinnerSeasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                season = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Cogemos el dato del spinner de partes del cuerpo
         * */
        spinnerPartBody.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                partBody = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return RootView;
    }

    /***
     * Método del fragment dialogo para elegir entre una opcion u otra
     * */

    public void showMenuPopUpGalleryPhoto(View v) {
        TextView textClose;
        ImageView imageView;
        dialogGalleryPhoto.setContentView(R.layout.popup_gallery_photo);
        textClose = (TextView) dialogGalleryPhoto.findViewById(R.id.closePopUp);
        imageView = (ImageView) dialogGalleryPhoto.findViewById(R.id.imageCameraGalery);



        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGalleryPhoto.dismiss();
            }
        });
        dialogGalleryPhoto.show();
        IntoToGallery();
        IntoToCamera();
    }

    /***
     * Boton para acceder a la galeria y coger una foto
     */
    public void IntoToGallery(){
        Button gallery;

        gallery =(Button) dialogGalleryPhoto.findViewById(R.id.gallery);

            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, CODIGO_PETICION_GALLERY);
                    dialogGalleryPhoto.dismiss();
                }
            });
    }
    /***
     * Boton para acceder a la camara y coger una foto
     */
    public void IntoToCamera(){
        Button camera;
        camera = (Button) dialogGalleryPhoto.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CODIGO_PETICION_CAMERA);
                dialogGalleryPhoto.dismiss();
            }
        });
    }

    /***
     * Código para poner la foto en el imageview
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODIGO_PETICION_CAMERA && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap image = (Bitmap) bundle.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            imageCamera = BitmapFactory.decodeByteArray(byteArray, 0,
                    byteArray.length);
            imageView.setImageBitmap(imageCamera);
            Log.d("iiiiii", String.valueOf(imageView));

        }

        if (requestCode == CODIGO_PETICION_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                stream = getActivity().getContentResolver().openInputStream(uri);
                imageGallery = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(imageGallery);
                Log.d("tttt", String.valueOf(imageView));

            } catch (FileNotFoundException e) {
                Toast.makeText(getActivity(),"Imagen no encontrada", Toast.LENGTH_SHORT);
            }
        }

        imageSend = ChooseParameters(imageCamera, imageGallery, imageChoose);
    }


    /***
     * Metodo pra que te elija la foto de la galeria o de la camara
     */
    public Bitmap ChooseParameters(Bitmap imageCamera, Bitmap imageGallery, Bitmap imageChoose){
        if(imageCamera!= null && imageGallery ==null){
            imageChoose = imageCamera;
        }
        if(imageCamera== null && imageGallery !=null){
            imageChoose = imageGallery;
        }

        return imageChoose;
    }

    /***
     *Método para transformar el bitmap en un string, queremos que sea string para poder pasarselo al sendItem. Más adelante en el adapter se cambiará a URI
     * para guardar el dato
     */

    public String trasnformBitmapToString(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imagen = stream.toByteArray();
        String imagenString = new String(imagen);
        return imagenString;
    }


    //Guardar la imagen como uri en el almacenamiento externo
    protected String saveImage(Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), image, "Titulo", null);
        // Uri uriLoaded = Uri.parse(path);

        return path;
    }






}
