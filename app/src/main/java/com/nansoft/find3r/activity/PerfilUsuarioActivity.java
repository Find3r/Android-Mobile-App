package com.nansoft.find3r.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.melnykov.fab.FloatingActionButton;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.NoticiaCompletaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Categoria;
import com.nansoft.find3r.models.NoticiaCompleta;
import com.nansoft.find3r.models.Usuario;

import java.util.ArrayList;
import java.util.List;



public class PerfilUsuarioActivity extends AppCompatActivity
{


    NoticiaCompletaAdapter adapter;

    TextView txtvNombreUsuario;
    ImageView imgvPerfilUsuario;
    ImageView imgvCelular;
    ImageView imgvTelefono;
    ImageView imgvEmail;

    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom customClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    String ID_USUARIO = " ";
    Usuario objUsuario;

    public static RecyclerView mRecyclerView;
    private NoticiaCompletaAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RecyclerViewHeader headerRecyclerView;

    ImageView imgvCover;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // Set up action bar.
        ActionBar bar = getSupportActionBar();
        bar.show();
        bar.setDisplayHomeAsUpEnabled(true);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_posts_user));

        LayoutInflater inflater = getLayoutInflater();


        //now you must initialize your list view
        mRecyclerView = (RecyclerView) findViewById(R.id.lstvPublicacionesUsuario);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        headerRecyclerView = (RecyclerViewHeader) findViewById(R.id.header);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAgregarNoticiaPerfil);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
            }
        });


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlPerfilUsuario);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);


        ID_USUARIO = getIntent().getExtras().getString("id");

        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //cargarUsuario();
            }
        });


        txtvNombreUsuario = (TextView) headerRecyclerView.findViewById(R.id.txtvNombreUsuario);
        imgvPerfilUsuario = (ImageView) headerRecyclerView.findViewById(R.id.imgvPerfilUsuario);
        imgvCover = (ImageView) headerRecyclerView.findViewById(R.id.imgvCoverPicture);



        customClient = new MobileServiceCustom(this);




        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        objUsuario = new Usuario();
        // cargamos la información de usuario
        cargarUsuario();

    }

    public void cargarUsuario() {

        imgvSad.setVisibility(View.GONE);
        txtvSad.setVisibility(View.GONE);

        try {

            new AsyncTask<Void, Void, Boolean>() {


                MobileServiceTable<Usuario> mUsuarioTable;

                @Override
                protected void onPreExecute()
                {

                    mUsuarioTable = customClient.mClient.getTable("usuario", Usuario.class);

                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    try {


                        objUsuario = mUsuarioTable.lookUp(ID_USUARIO).get();

                        return true;
                    } catch (Exception exception) {

                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean success)
                {

                    txtvNombreUsuario.setText(objUsuario.getNombre());

                    Glide.with(getApplicationContext())
                            .load(objUsuario.getUrlimagen().trim())
                            .asBitmap()
                            .fitCenter()
                            .placeholder(R.drawable.picture_default)
                            .error(R.drawable.error_image)
                            .into(imgvPerfilUsuario);


                    Glide.with(getApplicationContext())
                            .load(objUsuario.getCover_picture().trim())
                            .asBitmap()
                            .fitCenter()
                            .placeholder(R.drawable.picture_default)
                            .error(R.drawable.error_image)
                            .into(imgvCover);




                    List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                    parameters.add(new Pair<String, String>("id",objUsuario.getId()));

                    MobileServiceCustom mobileServiceCustom = new MobileServiceCustom(getApplicationContext());

                    ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("news_user", "GET", parameters);

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
                                final NoticiaCompleta[] myTypes = objGson.fromJson(jsonArray,NoticiaCompleta[].class);

                                headerRecyclerView.attachTo(mRecyclerView,true);
                                mAdapter = new NoticiaCompletaAdapter(myTypes,PerfilUsuarioActivity.this);


                                mRecyclerView.setAdapter(mAdapter);

                                estadoAdapter(false);
                            } else {
                                estadoAdapter(true);

                            }


                        }
                    });



                }
            }.execute();


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
            txtvSad.setText(getResources().getString(R.string.no_posts_user));

        }
        else
        {
            imgvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
