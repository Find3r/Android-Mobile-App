package com.nansoft.find3r.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComplexRecyclerViewAdapter;
import com.nansoft.find3r.adapters.NoticiaCompletaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.NoticiaCompleta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NoticiasCategoriaActivity extends CustomAppCompatActivity {

    String idCategoria = "";
    SwipeRefreshLayout mSwipeRefreshLayout;
    NoticiaCompletaAdapter adapter;
    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom mobileServiceCustom;

    public static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    boolean ESTADO_BUSQUEDA = false;

    ArrayList<Object> itemsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));

        mobileServiceCustom = new MobileServiceCustom(this);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        idCategoria = getIntent().getExtras().getString("idCategoria");
        String nombreCategoria = getIntent().getExtras().getString("nombreCategoria");
        setTitle(nombreCategoria);

        //now you must initialize your list view
        mRecyclerView = (RecyclerView) findViewById(R.id.lstvNoticias);

        itemsCollection = new ArrayList<Object>();
        mAdapter = new ComplexRecyclerViewAdapter(itemsCollection, NoticiasCategoriaActivity.this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAgregarNoticia);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlNoticias);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarNoticias();
            }
        });


        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });


        cargarNoticias();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.search_menu, menu);

        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.actionview_search));
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });

                if (!s.trim().isEmpty()) {

                    cargarNoticias(s);
                    ESTADO_BUSQUEDA = true;
                }
                else
                {
                    cargarNoticias();
                    ESTADO_BUSQUEDA = false;
                }

                mAdapter.notifyDataSetChanged();

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }



    public void cargarNoticias() {
        imgvSad.setVisibility(View.GONE);
        txtvSad.setVisibility(View.GONE);

        itemsCollection = new ArrayList<>();

        try {

            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",idCategoria));
            ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("news_category", "GET", parameters);

            Futures.addCallback(lst, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exc) {

                    estadoAdapter(false);

                }

                @Override
                public void onSuccess(JsonElement result) {

                    // se verifica si el resultado es un array Json
                    if (result.isJsonArray()) {
                        // obtenemos el resultado como un JsonArray
                        JsonArray jsonArray = result.getAsJsonArray();
                        Gson objGson = new Gson();

                        // se deserializa el array
                        Type collectionType = new TypeToken<List<NoticiaCompleta>>(){}.getType();

                        itemsCollection = objGson.fromJson(jsonArray, collectionType);

                        estadoAdapter(true);
                    } else {
                        estadoAdapter(false);
                    }


                }
            });

        }
        catch (Exception e )
        {
            estadoAdapter(false);
        }

    }

    public void cargarNoticias(String searchTerm) {

        itemsCollection = new ArrayList<>();
        try {


            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("searchTerm",searchTerm));
            parameters.add(new Pair<String, String>("idCategory",idCategoria));
            ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("search", "GET", parameters);

            Futures.addCallback(lst, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exc) {

                    estadoAdapter(false);

                }

                @Override
                public void onSuccess(JsonElement result) {

                    // se verifica si el resultado es un array Json
                    if (result.isJsonArray()) {
                        // obtenemos el resultado como un JsonArray
                        JsonArray jsonArray = result.getAsJsonArray();
                        Gson objGson = new Gson();

                        // se deserializa el array
                        Type collectionType = new TypeToken<List<NoticiaCompleta>>(){}.getType();

                        itemsCollection = objGson.fromJson(jsonArray, collectionType);

                        estadoAdapter(true);

                    }
                    else
                    {
                        estadoAdapter(false);
                    }


                }
            });

        }
        catch (Exception e )
        {
            estadoAdapter(false);
        }
    }



    private void estadoAdapter(boolean pEstadoError)
    {
        mSwipeRefreshLayout.setRefreshing(false);

        mRecyclerView.setAdapter(new ComplexRecyclerViewAdapter(itemsCollection, NoticiasCategoriaActivity.this));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(NoticiasCategoriaActivity.this));


        if(!pEstadoError)
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setText(getResources().getString(R.string.noconnection));

        }
        else
        {
            imgvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
        }

    }

}
