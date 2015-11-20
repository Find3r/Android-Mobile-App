package com.nansoft.find3r.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.nansoft.find3r.models.NoticiaCompleta;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

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

    MobileServiceCustom mobileServiceCustom;

    int PROVINCIA_SELECCIONADA = 0;
    int TIPO_REPORTE_SELECCIONADO = 0;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";


    TextView txtvInfoFecha;
    TextView txtvInfoHora;

    int DIA_ACTUAL;
    int MES_ACTUAL;
    int ANIO_ACTUAL;

    int DIA_SELECCIONADO;
    int MES_SELECCIONADO;
    int ANIO_SELECCIONADO;
    int MINUTO_SELECCIONADO;
    int HORA_SELECCIONADO;

    Spinner spnrProvincia;
    Spinner spinnerTipoReporte;
    Spinner spnrEstadoReporte;
    ImageView imgvNoticiaPreview;
    EditText edtNombreNoticia;
    EditText edtDescripcionNoticia;


    NoticiaCompleta objNoticiaCompleta;

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
        imgvNoticiaPreview = (ImageView) findViewById(R.id.imgvNoticiaPreview);
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

        imgvNoticiaPreview = (ImageView) findViewById(R.id.imgvNoticiaPreview);

        edtNombreNoticia = (EditText) findViewById(R.id.edtNombreNoticia);

        edtDescripcionNoticia = (EditText) findViewById(R.id.edtDescripion);


        spnrProvincia = (Spinner)findViewById(R.id.spnrProvincias);
        spinnerTipoReporte = (Spinner)findViewById(R.id.spnrTipoReporte);
        spnrEstadoReporte = (Spinner) findViewById(R.id.spnrEstadoReporte);

        // Cargamos el spinner con el array de strings
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AgregarNoticia.this,
                R.array.tipo_reporte_array, android.R.layout.simple_dropdown_item_1line);

        spinnerTipoReporte.setAdapter(adapter);

        // Cargamos el spinner con el array de strings
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(AgregarNoticia.this,
                R.array.provincias_array, android.R.layout.simple_dropdown_item_1line);

        spnrProvincia.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(AgregarNoticia.this,
                R.array.estado_reporte, android.R.layout.simple_dropdown_item_1line);

        spnrEstadoReporte.setAdapter(adapter2);


        spnrProvincia.setSelection(0);
        spinnerTipoReporte.setSelection(0);
        spnrEstadoReporte.setSelection(0);

        spinnerTipoReporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TIPO_REPORTE_SELECCIONADO = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnrProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        DIA_ACTUAL = DIA_SELECCIONADO;
        MES_ACTUAL = MES_SELECCIONADO + 1;
        ANIO_ACTUAL = ANIO_SELECCIONADO;

        // establecemos el texto en los text view que informan al usuario del valor seleccionado
        txtvInfoFecha.setText(customDate(DIA_SELECCIONADO,MES_SELECCIONADO,ANIO_SELECCIONADO));
        txtvInfoHora.setText(customHour(HORA_SELECCIONADO,MINUTO_SELECCIONADO));

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
        datePickerDialog.setYearRange(1985, calendar.get(Calendar.YEAR)+1);
        findViewById(R.id.btnFecha).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

        //cargarTipoReportes();

        // se instenta obtener el objeto que se envía en caso que se acceda a editar una noticia
        try {


            objNoticiaCompleta = (NoticiaCompleta) getIntent().getParcelableExtra("obj");



            cargarDatos();

        } catch (Exception exception) {
            // si ocurre un error al cargar el objeto es porque no se envío el objeto entonces deshabilitamos
            spnrEstadoReporte.setEnabled(false);
        }
    }

    private void cargarDatos()
    {

        //imgView.setBackgroundResource(R.drawable.emptyimage);
        Glide.with(this)
                .load(objNoticiaCompleta.getUrlImagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(imgvNoticiaPreview);

        txtvInfoFecha.setText(objNoticiaCompleta.getFechadesaparicion());


        edtNombreNoticia.setText(objNoticiaCompleta.nombre);
        edtDescripcionNoticia.setText(objNoticiaCompleta.descripcion);

        spinnerTipoReporte.setSelection(Integer.parseInt(objNoticiaCompleta.idCategoria) - 1);
        spnrProvincia.setSelection(Integer.parseInt(objNoticiaCompleta.idProvincia) - 1);

        spnrEstadoReporte.setSelection(Integer.parseInt(objNoticiaCompleta.idestado));

        // establecemos la fecha y hora
        txtvInfoFecha.setText(objNoticiaCompleta.getFechadesaparicion());
        txtvInfoHora.setText(objNoticiaCompleta.getHoraDesaparicion());

        Button btnAgregar = (Button) findViewById(R.id.btnAgregarReporte);
        btnAgregar.setText("Actualizar");
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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

                // seteamos la imagen con el bitmap
                imgvNoticiaPreview.setImageBitmap(bitmap);


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

        imgvNoticiaPreview.setImageResource(R.drawable.emptyimage);


        edtNombreNoticia.setText(" ");

        edtDescripcionNoticia.setText(" ");
        PATH_IMAGEN = "";

        spnrProvincia.setSelection(0);


    }

    public void EnviarReporte()
    {
        Noticia objNoticia = new Noticia();
        objNoticia.urlimagen = "";
        objNoticia.idestado = "0";
        if(objNoticiaCompleta != null)
        {
            objNoticia = objNoticiaCompleta;
            objNoticia.idestado = String.valueOf(spnrEstadoReporte.getSelectedItemPosition());
        }

        String nombreNoticia = edtNombreNoticia.getText().toString();
        String descripcion = edtDescripcionNoticia.getText().toString().trim();

        if(PATH_IMAGEN.trim().equals("") && objNoticia.getUrlImagen().trim().isEmpty())
        {
            Snackbar.make(imgvNoticiaPreview, "Debe adjuntar una imagen", Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!

        }
        else
        {
            if(descripcion.isEmpty() || nombreNoticia.isEmpty())
            {
                Snackbar.make(imgvNoticiaPreview, "Debe completar todos los datos", Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!

            }
            else
            {

                // se verifica que no sea una fecha futura
                if(checkDate())
                {

                    try
                    {

                        int idProvincia = Integer.parseInt(String.valueOf(PROVINCIA_SELECCIONADA)) + 1;
                        int idTipoReporte = Integer.parseInt(String.valueOf(TIPO_REPORTE_SELECCIONADO)) + 1;


                        String fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(ANIO_SELECCIONADO - 1900, MES_SELECCIONADO, DIA_SELECCIONADO, HORA_SELECCIONADO, MINUTO_SELECCIONADO));

                        objNoticia.idusuario = MobileServiceCustom.USUARIO_LOGUEADO.getId();
                        objNoticia.nombre = nombreNoticia;
                        objNoticia.descripcion = descripcion;
                        objNoticia.idCategoria = String.valueOf(idTipoReporte);
                        objNoticia.idProvincia = String.valueOf(idProvincia);
                        objNoticia.fechadesaparicion = fecha;

                        // se verifica si el path inicia con https, eso quiere decir que no se ha seleccionado una nueva imagen y debe quedar
                        // con la que está
                        if(!PATH_IMAGEN.trim().isEmpty())
                        {
                            EXTENSION_IMAGEN =  "." + PATH_IMAGEN.substring(PATH_IMAGEN.lastIndexOf(".")+1);
                            NOMBRE_IMAGEN = CustomDate.getNewDate() + EXTENSION_IMAGEN;

                            objNoticia.urlimagen = "https://purisinfo.blob.core.windows.net/img/" + NOMBRE_IMAGEN;
                        }



                        UploadReportAsyncTask objTarea = new UploadReportAsyncTask(objNoticia);
                        objTarea.execute();


                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "error location " + e.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Snackbar.make(imgvNoticiaPreview, "Verifique la fecha seleccionada", Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!
                }

            }

        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        DIA_SELECCIONADO = day;
        MES_SELECCIONADO = month + 1;
        ANIO_SELECCIONADO = year;


        // se verifica que haya seleccionado una fecha que sea menor o igual a la de hoy
        if(checkDate())
        {
            txtvInfoFecha.setText(customDate(DIA_SELECCIONADO, MES_SELECCIONADO, ANIO_SELECCIONADO));
        }
        else
        {
            Snackbar.make(imgvNoticiaPreview, "Verifique la fecha seleccionada", Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!
        }


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {


        HORA_SELECCIONADO = hourOfDay;
        MINUTO_SELECCIONADO = minute;

        txtvInfoHora.setText(customHour(HORA_SELECCIONADO,MINUTO_SELECCIONADO));

    }

    private String customDate(int pDay,int pMonth, int pYear)
    {
        String fecha = "Sin definir";

        if (pDay < 10)
        {
            fecha = "0" + pDay;
        }
        else
        {
            fecha = String.valueOf(pDay);
        }

        fecha += "/";

        if (pMonth < 10)
        {
            fecha += "0" + pMonth;
        }
        else
        {
            fecha += String.valueOf(pMonth);
        }

        fecha += "/" + pYear;

        return fecha;
    }

    private String customHour(int pHour,int pMinute)
    {
        String hora = "Sin definir";

        if(pHour < 10)
        {
            hora = "0" + pHour;
        }
        else
        {
            hora = String.valueOf(pHour);
        }

        hora += ":";

        if(pMinute < 10)
        {
            hora += "0" + pMinute;
        }
        else
        {
            hora += String.valueOf(pMinute);
        }

        return hora;
    }

    private boolean checkDate()
    {
        // se verifica que haya seleccionado una fecha que sea menor o igual a la de hoy
        if(DIA_SELECCIONADO <= DIA_ACTUAL && MES_SELECCIONADO <= MES_ACTUAL)
        {
            return true;

        }
        else
        {
            return false;
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
            progressDialog.setCancelable(false);
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mNoticiaTable = mobileServiceCustom.mClient.getTable("Noticia", Noticia.class);



        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try{

                // se verifica si es un nuevo registro
                if(!PATH_IMAGEN.trim().isEmpty())
                {



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

                }

                // se verifica si el id está vacío, eso indica que es un nuevo registro
                if(objNoticiaCompleta == null)
                {
                    mNoticiaTable.insert(objNoticia).get();
                }
                else
                {
                    mNoticiaTable.update(objNoticia);
                }

                return true;
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

            String mensajeSnack = "";
            if(result)
            {
                if(objNoticiaCompleta == null)
                {
                    mensajeSnack =  "Noticia agregada exitosamente";
                }
                else
                {
                    mensajeSnack =  "Noticia actualizada exitosamente";
                }

                resetActivity();
            }
            else
            {
                mensajeSnack = "Error al agregar la noticia";
            }

            Snackbar.make(imgvNoticiaPreview, mensajeSnack, Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!

            progressDialog.dismiss();
        }

    }

}
