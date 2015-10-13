package com.nansoft.find3r.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.helpers.CustomDate;
import com.nansoft.find3r.helpers.CustomFilePicker;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Noticia;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgregarNoticia extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private int REQUEST = 1;

    static int TIPO_SELECCION = 2;
    static String PATH_IMAGEN = "";
    static String NOMBRE_IMAGEN = "";
    static String EXTENSION_IMAGEN = "";
    String mCurrentPhotoPath;

    MobileServiceCustom mobileServiceCustom;

    int PROVINCIA_SELECCIONADA = 0;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";


    TextView txtvInfoFecha;
    TextView txtvInfoHora;

    int DIA_SELECCIONADO;
    int MES_SELECCIONADO;
    int ANIO_SELECCIONADO;
    int MINUTO_SELECCIONADO;
    int HORA_SELECCIONADO;

    BetterSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_noticia);

        // Set up action bar.
        ActionBar bar = getSupportActionBar();
        bar.show();
        bar.setDisplayHomeAsUpEnabled(true);

        txtvInfoFecha = (TextView) findViewById(R.id.txtvInfoFecha);
        txtvInfoHora  = (TextView) findViewById(R.id.txtvInfoHora);

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



        spinner = (BetterSpinner)findViewById(R.id.spnrProvincias);

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


        // obtenemos la fecha y hora actual
        final Calendar calendar = Calendar.getInstance();

        // establecemos los valores en las variables globales
        DIA_SELECCIONADO = calendar.get(Calendar.DAY_OF_MONTH);
        MES_SELECCIONADO = calendar.get(Calendar.MONTH);
        ANIO_SELECCIONADO = calendar.get(Calendar.YEAR);
        MINUTO_SELECCIONADO = calendar.get(Calendar.MINUTE);
        HORA_SELECCIONADO = calendar.get(Calendar.HOUR_OF_DAY);

        // establecemos el texto en los text view que informan al usuario del valor seleccionado
        txtvInfoFecha.setText(DIA_SELECCIONADO + "/" + MES_SELECCIONADO + "/" + ANIO_SELECCIONADO);
        txtvInfoHora.setText(HORA_SELECCIONADO + ":" + MINUTO_SELECCIONADO);


        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        findViewById(R.id.btnFecha).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setYearRange(1985, calendar.get(Calendar.YEAR));
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        findViewById(R.id.btnHora).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        mobileServiceCustom = new MobileServiceCustom(this);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement
        switch(item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
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
        //imgView.setBackgroundResource(R.drawable.emptyimage);
        imgView.setImageResource(R.drawable.emptyimage);

        EditText edt = (EditText) findViewById(R.id.edtDescripion);
        edt.setText(" ");

        EditText edtNombreReporte = (EditText) findViewById(R.id.edtNombreNoticia);
        edtNombreReporte.setText(" ");
        PATH_IMAGEN = "";

        spinner.setSelection(0);


    }

    public void EnviarReporte()
    {

        Noticia objNoticia = new Noticia();


        EditText editText = (EditText) findViewById(R.id.edtDescripion);
        EditText edtNombreReporte = (EditText) findViewById(R.id.edtNombreNoticia);
        String nombreNoticia = edtNombreReporte.getText().toString();
        String descripcion = editText.getText().toString().trim();

        if(PATH_IMAGEN.trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Debe adjuntar una imagen",Toast.LENGTH_SHORT).show();

        }
        else
        {
            if(descripcion.isEmpty() || nombreNoticia.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Debe completar todos los datos",Toast.LENGTH_SHORT).show();
            }
            else
            {
                try{




                    int idProvincia = Integer.parseInt(String.valueOf(PROVINCIA_SELECCIONADA)) + 1;


                    String fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(ANIO_SELECCIONADO - 1900, MES_SELECCIONADO, DIA_SELECCIONADO, HORA_SELECCIONADO, MINUTO_SELECCIONADO));


                    objNoticia.setIdusuario(MobileServiceCustom.USUARIO_LOGUEADO.getId());
                    objNoticia.setNombre(nombreNoticia);
                    objNoticia.setDescripcion(descripcion);
                    objNoticia.setIdestado("0");

                    objNoticia.setIdProvincia(String.valueOf(idProvincia));

                    objNoticia.setFechadesaparicion(fecha);



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

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        txtvInfoFecha.setText(day + "/" + month + "/" + year);

        DIA_SELECCIONADO = day;
        MES_SELECCIONADO = month;
        ANIO_SELECCIONADO = year;


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        txtvInfoHora.setText(hourOfDay + ":" + minute);


        HORA_SELECCIONADO = hourOfDay;
        MINUTO_SELECCIONADO = minute;
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
            progressDialog.setCancelable(false);
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
                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Archivo no encontrado!",Toast.LENGTH_SHORT).show();
                    }
                });
                */
                return false;
            }
            catch (StorageException storageException) {
                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Contenedor no encontrado!",Toast.LENGTH_SHORT).show();
                    }
                });
                */
                return false;
            }
            catch (final Exception e) {
                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Exception " + e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                */
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
                resetActivity();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error al agregar la noticia",
                        Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

    }

}
