package com.nansoft.find3r.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.MyProfileActivity;
import com.nansoft.find3r.adapters.ComplexRecyclerViewAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.NoticiaCompleta;
import com.nansoft.find3r.models.Usuario;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 19/11/2015.
 */
public class ProfileFragment extends FragmentSwipe
{
    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom customClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView mRecyclerView;

    Usuario objUsuario;

    ArrayList<Object> itemsCollection;

    private Toolbar toolbar;
    private PagerSlidingTabStrip tabs;
    private Context mContext;
    ComplexRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usuario_layout, container, false);


        mContext = view.getContext();

        /////////LAYOUT DE ERROR////////////
        View includedLayout = view.findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_posts_user));
        ///////////////////////

        // Lookup the recyclerview in activity layout
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lstvPublicacionesUsuario);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // Create adapter passing in the sample user data

        ///////////// FLOATING ACTION BUTTON ////////////
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAgregarNoticiaPerfil);
        fab.setVisibility(View.INVISIBLE);
        /*
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        ///////////////
        */

        /////////////// SWIPE ///////
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swprlPerfilUsuario);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarUsuario();

            }
        });
        //////////////


        customClient = new MobileServiceCustom(getActivity());


        // cargamos la información de usuario
        cargarUsuario();

        return view;
    }

    @Override
    public  void setEnabledSwipe(boolean status)
    {
        mSwipeRefreshLayout.setEnabled(status);
    }

    public void cargarUsuario() {

        try{
            // cargamos el token
            customClient.loadUserTokenCache(customClient.mClient);
            cargarNoticiasUsuario();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "er " + e.toString(), Toast.LENGTH_SHORT).show();

        }




    }

    private void cargarNoticiasUsuario()
    {
        itemsCollection = new ArrayList<Object>();

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();

        parameters.add(new Pair<String, String>("id",MobileServiceCustom.USUARIO_LOGUEADO.id));


        ListenableFuture<JsonElement> lst = customClient.mClient.invokeApi("my_news", "GET", parameters);

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
                    Type collectionType = new TypeToken<List<NoticiaCompleta>>(){}.getType();

                    itemsCollection = objGson.fromJson(jsonArray, collectionType);
                    adapter = new ComplexRecyclerViewAdapter(itemsCollection, mContext);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(adapter);



                    estadoAdapter(false);
                } else {
                    estadoAdapter(true);

                }


            }
        });
    }

    private void estadoAdapter(boolean pEstadoError)
    {


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

            MyProfileActivity.loadUserInformation();
        }

    }
}
