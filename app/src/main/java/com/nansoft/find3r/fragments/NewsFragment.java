package com.nansoft.find3r.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.AgregarNoticia;
import com.nansoft.find3r.adapters.NoticiaCompletaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.interfaces.FragmentSwipeListener;
import com.nansoft.find3r.models.NoticiaCompleta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/20/2015.
 */
public class NewsFragment extends Fragment implements FragmentSwipeListener
{
    public static NoticiaCompletaAdapter adapter;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    MobileServiceCustom mobileService;
    private Context mContext;
    ImageView imgvSad;
    TextView txtvSad;

    public static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<NoticiaCompleta> imageResults;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.news_layout, container, false);
        View includedLayout = view.findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));

        //now you must initialize your list view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lstvNoticias);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mContext = view.getContext();

        mobileService = new MobileServiceCustom(view.getContext());
   FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAgregarNoticia);


        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
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

        return view;
    }

    private void disableSwipe()
    {
        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void setEnabledSwipe(boolean status) {

    }


    public void cargarNoticias(final FragmentActivity activity) {
        imgvSad.setVisibility(View.GONE);
        txtvSad.setVisibility(View.GONE);



        try {


            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",MobileServiceCustom.USUARIO_LOGUEADO.id));
            ListenableFuture<JsonElement> lst = mobileService.mClient.invokeApi("last_newsaux", "GET", parameters);

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
                        //final NoticiaCompleta[] myTypes = objGson.fromJson(jsonArray, NoticiaCompleta[].class);

                        Type collectionType = new TypeToken<List<NoticiaCompleta>>(){}.getType();

                        imageResults = objGson.fromJson(jsonArray, collectionType);

                        mAdapter = new NoticiaCompletaAdapter(imageResults,mContext);
                        mRecyclerView.setAdapter(mAdapter);

                        estadoAdapter(false);
                    }
                    else
                    {
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

        if(pEstadoError)
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