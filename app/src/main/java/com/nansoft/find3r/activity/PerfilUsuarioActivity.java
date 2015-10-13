package com.nansoft.find3r.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    ListView listview;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_layout);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_posts_user));

        LayoutInflater inflater = getLayoutInflater();

        View headerListView = inflater.inflate(R.layout.perfil_usuario_header,null);

        listview = (ListView) findViewById(R.id.lstvPublicacionesUsuario);
        listview.addHeaderView(headerListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAgregarNoticiaPerfil);
        fab.attachToListView(listview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
            }
        });

        //now you must initialize your list view
        adapter = new NoticiaCompletaAdapter(this, R.layout.noticia_item);

        listview.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlPerfilUsuario);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);


        ID_USUARIO = getIntent().getExtras().getString("id");


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarUsuario();
            }
        });


        txtvNombreUsuario = (TextView) headerListView.findViewById(R.id.txtvNombreUsuario);
        imgvPerfilUsuario = (ImageView) headerListView.findViewById(R.id.imgvPerfilUsuario);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

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

                    adapter.clear();


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
                                // recorremos cada elemento del array
                                for (JsonElement element : jsonArray) {

                                    // se deserializa cada objeto JSON
                                    final NoticiaCompleta objNoticia = objGson.fromJson(element, NoticiaCompleta.class);

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {


                                            adapter.add(objNoticia);
                                            adapter.notifyDataSetChanged();


                                        }
                                    });
                                }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
