package com.nansoft.find3r.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.nansoft.find3r.R;
import com.nansoft.find3r.helpers.CustomDate;
import com.nansoft.find3r.helpers.CustomFilePicker;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Noticia;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarNoticia extends AppCompatActivity {

    private int REQUEST = 1;

    static int TIPO_SELECCION = 2;
    static String PATH_IMAGEN = "";
    static Location mLastLocation ;
    static String NOMBRE_IMAGEN = "";
    static String EXTENSION_IMAGEN = "";
    String mCurrentPhotoPath;

    MobileServiceCustom mobileServiceCustom;

    int PROVINCIA_SELECCIONADA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_noticia);

        // Set up action bar.
        ActionBar bar = getSupportActionBar();
        bar.show();
        bar.setDisplayHomeAsUpEnabled(true);

        // onClick listener de image view
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

        Button btnAgregarNoticia = (Button) findViewById(R.id.btnAgregarReporte);
        btnAgregarNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarReporte();
            }
        });



        BetterSpinner spinner = (BetterSpinner)findViewById(R.id.spnrProvincias);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AgregarNoticia.this,
                R.array.provincias_array, android.R.layout.simple_dropdown_item_1line);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PROVINCIA_SELECCIONADA = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mobileServiceCustom = new MobileServiceCustom(this);

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


    private void resetActivity()
    {
        ImageView imgView = (ImageView) findViewById(R.id.imgvNoticiaPreview);
        imgView.setBackgroundResource(R.drawable.emptyimage);


        EditText edt = (EditText) findViewById(R.id.edtDescripion);
        edt.setText(" ");

        PATH_IMAGEN = "";
    }

    public void EnviarReporte()
    {

        Noticia objNoticia = new Noticia();


        EditText editText = (EditText) findViewById(R.id.edtDescripion);
        String descripcion = editText.getText().toString().trim();

        if(PATH_IMAGEN.trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Debe adjuntar una imagen",Toast.LENGTH_SHORT).show();

        }
        else
        {
            if(descripcion.equals(""))
            {
                Toast.makeText(getApplicationContext(),"Debe ingresar una descripción ya que ésto nos ayuda en la ubicación del lugar",Toast.LENGTH_SHORT).show();
            }
            else
            {
                try{




                    int idProvincia = Integer.parseInt(String.valueOf(PROVINCIA_SELECCIONADA)) + 1;

                    String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String fecha = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

                    objNoticia.setDescripcion(descripcion);
                    //objNoticia.setIdProvincia(String.valueOf(idProvincia));

                    //objNoticia.setFechadesaparicion(fecha);



                    EXTENSION_IMAGEN =  "." + PATH_IMAGEN.substring(PATH_IMAGEN.lastIndexOf(".")+1);
                    NOMBRE_IMAGEN = CustomDate.getNewDate() + EXTENSION_IMAGEN;


                    objNoticia.setUrlImagen("https://purisinfo.blob.core.windows.net/img/" + NOMBRE_IMAGEN);


                    //objReporte.setTipoReporte(ID_TIPO_REPORTE);
                    UploadReportAsyncTask objTarea = new UploadReportAsyncTask(objNoticia);
                    objTarea.execute();


                }catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "error location " + e.toString(), Toast.LENGTH_SHORT).show();

                }





            }

        }
    }


    class UploadReportAsyncTask extends AsyncTask<Void, Integer, Boolean>
    {
        public static final String storageConnectionString =
                "DefaultEndpointsProtocol=http;"
                        + "AccountName=purisinfo;"
                        + "AccountKey=CRhvp0pVt8C9mc+nYudKd89RSLH1DKFhg4HOrTCUQqVbLeBLjakCkJeGkvRvG7M2uPCzcOtWhr+fgcHsWJR8bg==";

        String NOMBRE_CONTENEDOR = "img";
        Noticia objNoticia;
        MobileServiceTable<Noticia> mNoticiaTable;
        ProgressDialog progressDialog;

        public UploadReportAsyncTask(Noticia pobjNoticia)
        {
            objNoticia = pobjNoticia;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog = new ProgressDialog(AgregarNoticia.this);
            progressDialog.setMessage("Agregando registro...");
            progressDialog.show();
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mNoticiaTable = mobileServiceCustom.mClient.getTable("Noticia", Noticia.class);



        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try{
                // Setup the cloud storage account.
                CloudStorageAccount account = CloudStorageAccount
                        .parse(storageConnectionString);

                // Create a blob service client
                CloudBlobClient blobClient = account.createCloudBlobClient();

                // Get a reference to a container
                // The container name must be lower case
                // Append a random UUID to the end of the container name so that
                // this sample can be run more than once in quick succession.
                CloudBlobContainer container = blobClient.getContainerReference(NOMBRE_CONTENEDOR);

                // Create the container if it does not exist
                container.createIfNotExists();

                // Make the container public
                // Create a permissions object
                BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

                // Include public access in the permissions object
                containerPermissions
                        .setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

                // Set the permissions on the container
                container.uploadPermissions(containerPermissions);

                // Get a reference to a blob in the container
                CloudBlockBlob blob = container.getBlockBlobReference(NOMBRE_IMAGEN);

                // Upload an image file.
                File sourceFile = new File(PATH_IMAGEN);
                /*
                // verificamos si es una fotografía capturada
                if(EXTENSION_IMAGEN.toUpperCase().equals(".JPG") || EXTENSION_IMAGEN.toUpperCase().equals(".JPEG"))
                {
                    Bitmap bmp = BitmapFactory.decodeFile(PATH_IMAGEN);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                    InputStream in = new ByteArrayInputStream(bos.toByteArray());
                    blob.upload(in, sourceFile.length());
                }
                else
                {

                    blob.upload(new FileInputStream(sourceFile), sourceFile.length());
                }
                */
                blob.upload(new FileInputStream(sourceFile), sourceFile.length());

                mNoticiaTable.insert(objNoticia).get();

            /*
            // Download the image file.
            File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
            blob.downloadToFile(destinationFile.getAbsolutePath()); */
            }
            catch (FileNotFoundException fileNotFoundException) {
                //Toast.makeText(MainActivity.this, "Archivo no encontrado!",Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Archivo no encontrado!",Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            catch (StorageException storageException) {
               /* Toast.makeText(MainActivity.this, "Contenedor no encontrado! " + storageException.toString() + "\ncode : " +"Contenedor no encontrado! " + storageException.getErrorCode(),
                        Toast.LENGTH_SHORT).show();*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Contenedor no encontrado!",Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            catch (final Exception e) {
                /*Toast.makeText(MainActivity.this, "Error " + e.toString(),
                        Toast.LENGTH_SHORT).show();*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Exception " + e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                Toast.makeText(getApplicationContext(), "Noticia agregada exitosamente",
                        Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error al agregar la imagen",
                        Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

    }

}
