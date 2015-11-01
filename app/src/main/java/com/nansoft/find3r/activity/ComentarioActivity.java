package com.nansoft.find3r.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.melnykov.fab.FloatingActionButton;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.MyTime;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComentarioAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Comentario;
import com.nansoft.find3r.models.ComentarioCompleto;

import java.util.ArrayList;
import java.util.List;

public class ComentarioActivity extends AppCompatActivity {

    String ID_NOTICIA = "";
    public static SwipeRefreshLayout mSwipeRefreshLayout;

    ImageView imgvSad;
    TextView txtvSad;
    MobileServiceCustom mobileServiceCustom ;

    public static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_comments));

        mobileServiceCustom = new MobileServiceCustom(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.lstvComentarios);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ID_NOTICIA = getIntent().getExtras().getString("idNoticia");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlComentarios);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarComentarios();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAgregarComentario);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Alert Dialog Code Start*/
                AlertDialog.Builder alert = new AlertDialog.Builder(ComentarioActivity.this);
                alert.setTitle(MobileServiceCustom.USUARIO_LOGUEADO.getNombre()); //Set Alert dialog title here
                alert.setMessage("Ingrese su comentario"); //Message here

                // Set an EditText view to get user input
                final EditText input = new EditText(ComentarioActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        String srt = input.getEditableText().toString();

                        if (srt.isEmpty()) {
                            Snackbar.make(mRecyclerView, "Debe ingresar datos en el comentario", Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!
                        } else {

                            agregarComentario(new Comentario("", srt, MobileServiceCustom.USUARIO_LOGUEADO.getId(), MyTime.getFecha(), MyTime.getHora(), ID_NOTICIA));
                        }

                    } // End of onClick(DialogInterface dialog, int whichButton)
                }); //End of alert.setPositiveButton
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                /* Alert Dialog Code End*/

            }
        });

        /*
        Button btnAgregarComentario = (Button) findViewById(R.id.btnChekComment);

        btnAgregarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText edtDescripcionComentario = (EditText) findViewById(R.id.edtDescripcionComentario_add_comment);

                if (edtDescripcionComentario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar datos en el comentario", Toast.LENGTH_SHORT).show();
                } else {
                    agregarComentario(new Comentario("", edtDescripcionComentario.getText().toString(), MobileServiceCustom.USUARIO_LOGUEADO.getId(), MyTime.getFecha(), MyTime.getHora(), ID_NOTICIA), edtDescripcionComentario);
                }
            }
        });
        */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });



        cargarComentarios();
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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void cargarComentarios() {

        imgvSad.setVisibility(View.INVISIBLE);
        txtvSad.setVisibility(View.INVISIBLE);

        /*
        CircularImageView imgvPerfilUsuario = (CircularImageView) findViewById(R.id.imgvLogoUsuario_add_comment);
        TextView txtvNombreUsuario = (TextView) findViewById(R.id.txtvNombreUsuario_add_comment);

        txtvNombreUsuario.setText(MobileServiceCustom.USUARIO_LOGUEADO.getNombre());


        Glide.with(this)
                .load(MobileServiceCustom.USUARIO_LOGUEADO.getUrlImagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(imgvPerfilUsuario);
        */

        try {



            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",ID_NOTICIA));

            ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("postcomments", "GET", parameters);

            Futures.addCallback(lst, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exc) {

                    estadoAdapter(true);
                }

                @Override
                public void onSuccess(JsonElement result) {

                    // se verifica si el resultado es un array Json
                    if (result.isJsonArray()) {
                        // obtenemos el resultado como un JsonArray
                        JsonArray jsonArray = result.getAsJsonArray();
                        Gson objGson = new Gson();


                        // se deserializa el array
                        final ComentarioCompleto[] myTypes = objGson.fromJson(jsonArray,ComentarioCompleto[].class);

                        mAdapter = new ComentarioAdapter(myTypes,ComentarioActivity.this);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                    if(mAdapter.getItemCount() == 0)
                    {
                        estadoAdapter(true);
                    }
                    else
                    {
                        estadoAdapter(false);
                    }

                    mSwipeRefreshLayout.setRefreshing(false);


                }
            });
            mSwipeRefreshLayout.setEnabled(true);
        }
        catch (Exception e )
        {
            estadoAdapter(true);
        }

    }

    private void estadoAdapter(boolean pEstadoError)
    {
        mSwipeRefreshLayout.setRefreshing(false);

        if(pEstadoError)
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setText(getResources().getString(R.string.no_comments));

        }
        else
        {
            imgvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
        }

    }

    public void agregarComentario(final Comentario objComentario,final EditText edtCheckComment)
    {

        new AsyncTask<Void, Void, Boolean>()
        {

            MobileServiceClient mClient;
            MobileServiceTable<Comentario> mComentarioTable;

            @Override
            protected void onPreExecute() {


                mComentarioTable = mobileServiceCustom.mClient.getTable("comentario",Comentario.class);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mComentarioTable.insert(objComentario).get();



                    return true;
                } catch (Exception exception) {

                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if(success)
                {
                    Toast.makeText(getApplicationContext(),"Comentario agregado",Toast.LENGTH_SHORT).show();
                    edtCheckComment.setText("");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Ha ocurrido un error al intentar agregar el comentario, intenta de nuevo",Toast.LENGTH_SHORT).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
                cargarComentarios();
            }
        }.execute();

    }

    public void agregarComentario(final Comentario objComentario)
    {

        new AsyncTask<Void, Void, Boolean>()
        {

            MobileServiceClient mClient;
            MobileServiceTable<Comentario> mComentarioTable;
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {


                mComentarioTable = mobileServiceCustom.mClient.getTable("comentario",Comentario.class);

                progressDialog = new ProgressDialog(ComentarioActivity.this);
                progressDialog.setMessage("Agregando comentario...");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mComentarioTable.insert(objComentario).get();



                        return true;
                } catch (Exception exception) {

                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean success) {

                progressDialog.hide();
                String mensajeSnack = "";
                if(success)
                {
                    mensajeSnack = "Comentario agregado";

                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                    });

                    cargarComentarios();


                }
                else
                {
                    mensajeSnack = "Ha ocurrido un error al intentar agregar el comentario, intenta de nuevo";
                }

                Snackbar.make(mRecyclerView, mensajeSnack, Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!

                mSwipeRefreshLayout.setRefreshing(false);

            }
        }.execute();

    }
}
