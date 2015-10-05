package com.nansoft.find3r.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.InfoNoticiaActivity;
import com.nansoft.find3r.adapters.NoticiaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Noticia;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/20/2015.
 */
public class NoticiaFragment extends Fragment
{
    public static NoticiaAdapter adapter;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    MobileServiceCustom mobileService;
    private Context mContext;
    ImageView imgvSad;
    TextView txtvSad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.noticias_layout, container, false);
        View includedLayout = view.findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));
        //now you must initialize your list view

        final ListView listview = (ListView) view.findViewById(R.id.lstvNoticias);

        adapter = new NoticiaAdapter(view.getContext(), R.layout.noticia_item);
        mContext = view.getContext();

        mobileService = new MobileServiceCustom(view.getContext());


        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                Intent intent = new Intent(view.getContext(), ComentarioActivity.class);
                intent.putExtra("idNoticia",adapter.getItem(i).getId());
                startActivity(intent);
                */
                Intent intent = new Intent(view.getContext(), InfoNoticiaActivity.class);
                intent.putExtra("idNoticia",adapter.getItem(i).getId());
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swprlNoticias);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarNoticias(getActivity());
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        cargarNoticias(getActivity());




        //EDITED Code


        //To have custom list view use this : you must define CustomeAdapter class
        // listview.setadapter(new CustomeAdapter(getActivity()));
        //getActivty is used instead of Context
        return view;
    }

    public void cargarNoticias(final FragmentActivity activity) {
        imgvSad.setVisibility(View.INVISIBLE);
        txtvSad.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setEnabled(false);

        MobileServiceClient mClient;
        MobileServiceTable<Noticia> mNoticiaTable;

        try {


            mNoticiaTable = mobileService.mClient.getTable("noticia", Noticia.class);

            adapter.clear();

            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            ListenableFuture<JsonElement> lst = mobileService.mClient.invokeApi("last_news", "GET", parameters);

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
                            final Noticia objLastNews = objGson.fromJson(element, Noticia.class);

                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    adapter.add(objLastNews);
                                    adapter.notifyDataSetChanged();


                                }
                            });
                        }

                    }

                    estadoAdapter(false);


                }
            });

        }
        catch (Exception e )
        {

        }





    }

    private void estadoAdapter(boolean pEstadoError)
    {
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
        if(pEstadoError)
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setText(getResources().getString(R.string.nodata));

        }
        else
        {
            imgvSad.setVisibility(View.INVISIBLE);
            txtvSad.setVisibility(View.INVISIBLE);
            txtvSad.setVisibility(View.INVISIBLE);
        }
    }
}