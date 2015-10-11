package com.nansoft.find3r.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nansoft.find3r.R;
import com.nansoft.find3r.helpers.CustomFilePicker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AgregarNoticia extends AppCompatActivity {

    private int REQUEST = 1;

    static int TIPO_SELECCION = 2;
    static String PATH_IMAGEN = "";
    static Location mLastLocation ;
    static String NOMBRE_IMAGEN = "";
    static String EXTENSION_IMAGEN = "";
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_noticia);

        // Set up action bar.
        ActionBar bar = getSupportActionBar();
        bar.show();
        bar.setDisplayHomeAsUpEnabled(true);

        ImageView imgvNoticiaPreview = (ImageView) findViewById(R.id.imgvNoticiaPreview);
        imgvNoticiaPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                // Solo imágenes
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                // Muestra las aplicaciones disponibles
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // se ejecuta cuando finaliza la acción de elegir un elemento
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        InputStream stream = null;
        Bitmap bitmap  = null;

        if (requestCode == REQUEST && resultCode == Activity.RESULT_OK && TIPO_SELECCION == 2) {
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }

                // obtenemos la uri de la imagen seleccionada
                final Uri imageUri = data.getData();

                // obtenemos la url de la imagen
                String urlImagen = CustomFilePicker.getPath(this, imageUri);

                PATH_IMAGEN = urlImagen;

                // creamos un bitmap de la url que enviamos
                bitmap = CustomFilePicker.loadPrescaledBitmap(urlImagen);

                ImageView imageView = (ImageView) findViewById(R.id.imgvNoticiaPreview);

                imageView.setBackgroundColor(getResources().getColor(R.color.crema));
                // seteamos la imagen con el bitmap
                imageView.setImageBitmap(bitmap);


            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show();

            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(), "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show();

                    }
            }
        }

    }
}
