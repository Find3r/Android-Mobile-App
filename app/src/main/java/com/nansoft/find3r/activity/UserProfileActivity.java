package com.nansoft.find3r.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
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
import com.nansoft.find3r.adapters.MyFragmentPagerAdapter;
import com.nansoft.find3r.fragments.FragmentSwipe;
import com.nansoft.find3r.fragments.NewsFragment;
import com.nansoft.find3r.fragments.ProfileFragment;
import com.nansoft.find3r.helpers.CircularImageView;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.NoticiaCompleta;
import com.nansoft.find3r.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends CustomAppCompatActivity
{

    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom customClient;

    static SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView mRecyclerView;

    String ID_USUARIO_AUX = "";

    Usuario objUsuario;

    ArrayList<Object> itemsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.usuario_layout);


        try {
            ID_USUARIO_AUX = getIntent().getExtras().getString("ID_USUARIO","").trim();
        }
        catch(Exception e)
        {

        }


        /////////LAYOUT DE ERROR////////////
        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_posts_user));
        ///////////////////////

        // Lookup the recyclerview in activity layout
        mRecyclerView = (RecyclerView) findViewById(R.id.lstvPublicacionesUsuario);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // Create adapter passing in the sample user data

        ///////////// FLOATING ACTION BUTTON ////////////
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
        ///////////////


        /////////////// SWIPE ///////
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlPerfilUsuario);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarUsuario();

            }
        });
        //////////////


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

        parameters.add(new Pair<String, String>("id",MobileServiceCustom.USUARIO_LOGUEADO.getId()));
        parameters.add(new Pair<String, String>("idUserVisited",ID_USUARIO_AUX));

        ListenableFuture<JsonElement> lst = customClient.mClient.invokeApi("all_news_user", "GET", parameters);

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
        mRecyclerView.setAdapter(new ComplexRecyclerViewAdapter(itemsCollection, UserProfileActivity.this));
        // Set layout manager to position the items

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
        MyProfileActivity.loadUserInformation();
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

}
