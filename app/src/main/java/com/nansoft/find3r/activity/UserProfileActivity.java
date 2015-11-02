package com.nansoft.find3r.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComplexRecyclerViewAdapter;
import com.nansoft.find3r.adapters.NoticiaCompletaAdapter;
import com.nansoft.find3r.fragments.UserProfileFragment;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.NoticiaCompleta;
import com.nansoft.find3r.models.User;
import com.nansoft.find3r.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends CustomAppCompatActivity {

    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom customClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView mRecyclerView;

    String ID_USUARIO_AUX = "";

    Usuario objUsuario;

    ArrayList<Object> itemsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_layout);
        //loadFragment(new UserProfileFragment());



        try {
            ID_USUARIO_AUX = getIntent().getExtras().getString("ID_USUARIO","").trim();
        }
        catch(Exception e)
        {

        }


        /**LAYOUT DE ERROR*****/
        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_posts_user));
        /*********************/

        // Lookup the recyclerview in activity layout
        mRecyclerView = (RecyclerView) findViewById(R.id.lstvPublicacionesUsuario);



        // Create adapter passing in the sample user data

        /********************* FLOATING ACTION BUTTON ************/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAgregarNoticiaPerfil);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        /*********************/

        /************ SWIPE ********************/
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlPerfilUsuario);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarUsuario();

            }
        });
        /*******************************/


        customClient = new MobileServiceCustom(this);




        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        // cargamos la información de usuario
        cargarUsuario();



    }



    public void cargarUsuario() {

        try{
            // cargamos el token
            customClient.loadUserTokenCache(customClient.mClient);

        }
        catch (Exception e)
        {
            Toast.makeText(this, "er " + e.toString(), Toast.LENGTH_SHORT).show();

        }

        itemsCollection = new ArrayList<>();

        imgvSad.setVisibility(View.GONE);
        txtvSad.setVisibility(View.GONE);

        if (ID_USUARIO_AUX.isEmpty())
        {
            itemsCollection.add(MobileServiceCustom.USUARIO_LOGUEADO);
            cargarNoticiasUsuario();

        }
        else
        {
            objUsuario = new Usuario();
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


                        objUsuario = mUsuarioTable.lookUp(ID_USUARIO_AUX).get();

                        return true;
                    } catch (Exception exception) {

                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean success)
                {
                    itemsCollection.add(objUsuario);

                    cargarNoticiasUsuario();
                }
            }.execute();


        }

    }

    private void cargarNoticiasUsuario()
    {
        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();

        if (ID_USUARIO_AUX.isEmpty())
        {
            parameters.add(new Pair<String, String>("id",MobileServiceCustom.USUARIO_LOGUEADO.getId()));
        }
        else
        {
            parameters.add(new Pair<String, String>("id",ID_USUARIO_AUX));
        }

        ListenableFuture<JsonElement> lst = customClient.mClient.invokeApi("news_user", "GET", parameters);

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
                    final NoticiaCompleta[] myTypes = objGson.fromJson(jsonArray, NoticiaCompleta[].class);



                    for (NoticiaCompleta item : myTypes) {
                        itemsCollection.add(item);
                    }

                    estadoAdapter(false);
                } else {
                    estadoAdapter(true);

                }


            }
        });
    }

    private void estadoAdapter(boolean pEstadoError)
    {
        mRecyclerView.setAdapter(new ComplexRecyclerViewAdapter(itemsCollection,UserProfileActivity.this));
        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UserProfileActivity.this));
        // That's all!
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


    private void llamar(String pNumero)
    {
        try {
            Intent Llamada = new Intent(Intent.ACTION_CALL);
            Llamada.setData(Uri.parse("tel:" + pNumero));
            startActivity(Llamada);
        } catch (ActivityNotFoundException activityException) {

        }
    }

    public void AbrirCorreo(Context pContexto)
    {
        String [] correoDestino = {MobileServiceCustom.USUARIO_LOGUEADO.getEmail()};
        try {
            Intent Correo = new Intent(Intent.ACTION_SEND);
            Correo.setData(Uri.parse("mailto:"));
            Correo.putExtra(Intent.EXTRA_EMAIL,correoDestino);
            Correo.putExtra(Intent.EXTRA_CC, "");
            Correo.putExtra(Intent.EXTRA_SUBJECT, "");
            Correo.putExtra(Intent.EXTRA_TEXT, "");
            Correo.setType("message/rfc822");
            startActivity(Intent.createChooser(Correo, "Email "));
        } catch (ActivityNotFoundException activityException) {

            Toast.makeText(pContexto, "Error verifique que tenga una aplicación de correo", Toast.LENGTH_SHORT).show();

        }
    }

    private ArrayList<Object> getSampleArrayList() {
        ArrayList<Object> items = new ArrayList<>();

        Usuario objUsuario = new Usuario();
        objUsuario.setId("533047340181225");
        objUsuario.setUrlimagen("http://graph.facebook.com/1209577962402395/picture?type=large");
        objUsuario.setCover_picture("https://fbcdn-photos-b-a.akamaihd.net/hphotos-ak-xfa1/v/t1.0-0/p480x480/11903819_1190609607632564_2484786140909343007_n.jpg?oh=3eba07bc184fed185a6fd84acfbf8d56&oe=5686C69F&__gda__=1455575316_9232ddcaad08825a655ccadec5e45f75");
        objUsuario.setNombre("Carlos Castro Brenes");
        items.add(objUsuario);

        NoticiaCompleta objNoticia = new NoticiaCompleta();

        objNoticia.setCantidadComentarios(8);
        objNoticia.setNombreUsuario("Paola Hidalgo");
        objNoticia.setUrlImagenUsuario("http://graph.facebook.com/533047340181225/picture?type=large");
        objNoticia.setDescripcion("可见在中国");
        objNoticia.setFechadesaparicion("2015-10-16T13:10:00.000Z");
        objNoticia.setId("169AAF52-0716-4627-8F1C-D16F21389070");
        objNoticia.setIdCategoria("1");
        objNoticia.setIdestado("0");
        objNoticia.setIdusuario("533047340181225");
        objNoticia.setNombre("Konichiwaaa");
        objNoticia.setUrlImagen("https://purisinfo.blob.core.windows.net/img/20151016_131648.jpg");

        for (int i = 0; i < 100; i++) {
            items.add(objNoticia);
        }


        return items;
    }

}
