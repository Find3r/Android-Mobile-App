package com.nansoft.find3r.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.NoticiaCompletaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Noticia;
import com.nansoft.find3r.models.NoticiaCompleta;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class NoticiasCategoriaActivity extends ActionBarActivity {

    String idCategoria = "";
    SwipeRefreshLayout mSwipeRefreshLayout;
    NoticiaCompletaAdapter adapter;
    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom mobileServiceCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticias_layout);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));

        mobileServiceCustom = new MobileServiceCustom(this);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        idCategoria = getIntent().getExtras().getString("idCategoria");
        String nombreCategoria = getIntent().getExtras().getString("nombreCategoria");
        setTitle(nombreCategoria);

        adapter = new NoticiaCompletaAdapter(this,R.layout.noticia_item);

        ListView listview = (ListView) findViewById(R.id.lstvNoticias);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(view.getContext(), ComentarioActivity.class);
                intent.putExtra("idNoticia",adapter.getItem(i).getId());
                startActivity(intent);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_noticias_categoria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId())
        {



            case android.R.id.home:
                finish();
                return true;
        }



    return super.onOptionsItemSelected(item);
    }

    public void cargarNoticias() {
        imgvSad.setVisibility(View.GONE);
        txtvSad.setVisibility(View.GONE);



        try {




            adapter.clear();

            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",idCategoria));
            ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("news_category", "GET", parameters);

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
                            final NoticiaCompleta objLastNews = objGson.fromJson(element, NoticiaCompleta.class);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    adapter.add(objLastNews);


                                }
                            });
                        }

                        adapter.notifyDataSetChanged();

                        estadoAdapter(false);
                    } else {
                        estadoAdapter(true);
                    }


                }
            });

        }
        catch (Exception e )
        {
            estadoAdapter(true);
        }





    }



    private void estadoAdapter(boolean pEstadoError)
    {
        mSwipeRefreshLayout.setRefreshing(false);
        if(adapter.isEmpty())
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            if(pEstadoError)
            {
                txtvSad.setText(getResources().getString(R.string.nodata));
            }
            else
            {
                txtvSad.setText(getResources().getString(R.string.noconnection));
            }
        }

    }
}
