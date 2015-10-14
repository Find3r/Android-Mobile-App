package com.nansoft.find3r.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.AgregarNoticia;
import com.nansoft.find3r.adapters.NoticiaCompletaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.NoticiaCompleta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/21/2015.
 */
public class PerfilFragment extends Fragment
 {



    TextView txtvNombreUsuario;
    ImageView imgvPerfilUsuario;
    ImageView imgvCelular;
    ImageView imgvTelefono;
    ImageView imgvEmail;
    ImageView imgvCover;

    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom customClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

     public static RecyclerView mRecyclerView;
     private NoticiaCompletaAdapter mAdapter;
     private RecyclerView.LayoutManager mLayoutManager;

     RecyclerViewHeader headerRecyclerView;

     RelativeLayout userContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.test, container, false);

        userContainer = (RelativeLayout) view.findViewById(R.id.layUserContainer);
        userContainer.setEnabled(false);

        View includedLayout = view.findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_posts_user));

        //now you must initialize your list view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lstvPublicacionesUsuario);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        headerRecyclerView = (RecyclerViewHeader) view.findViewById(R.id.header);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager


        /*
        View headerRecyclerView = inflater.inflate(R.layout.perfil_usuario_header,null);

        listview = (ListView) view.findViewById(R.id.lstvPublicacionesUsuario);
        */


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAgregarNoticiaPerfil);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });




        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swprlPerfilUsuario);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);




        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mAdapter.clear();

            }
        });


        txtvNombreUsuario = (TextView) headerRecyclerView.findViewById(R.id.txtvNombreUsuario);
        imgvPerfilUsuario = (ImageView) headerRecyclerView.findViewById(R.id.imgvPerfilUsuario);
        imgvCover = (ImageView) headerRecyclerView.findViewById(R.id.imgvCoverPicture);




        customClient = new MobileServiceCustom(view.getContext());

        try{
            // cargamos el token
            customClient.loadUserTokenCache(customClient.mClient);

        }
        catch (Exception e)
        {
            Toast.makeText(view.getContext(),"er " + e.toString(),Toast.LENGTH_SHORT).show();

        }


        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        // cargamos la información de usuario
        cargarUsuario(getActivity());




        return view;
    }


    public void cargarUsuario(final FragmentActivity activity) {

        imgvSad.setVisibility(View.GONE);
        txtvSad.setVisibility(View.GONE);

        try {

            txtvNombreUsuario.setText(MobileServiceCustom.USUARIO_LOGUEADO.getNombre());

            Glide.with(activity.getApplicationContext())
                    .load(MobileServiceCustom.USUARIO_LOGUEADO.getUrlimagen().trim())
                    .asBitmap()
                    .fitCenter()
                    .placeholder(R.drawable.picture_default)
                    .error(R.drawable.error_image)
                    .into(imgvPerfilUsuario);

            Glide.with(activity.getApplicationContext())
                    .load(MobileServiceCustom.USUARIO_LOGUEADO.getCover_picture().trim())
                    .asBitmap()
                    .placeholder(R.drawable.picture_default)
                    .error(R.drawable.error_image)
                    .into(imgvCover);



            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",MobileServiceCustom.USUARIO_LOGUEADO.getId()));

            MobileServiceCustom mobileServiceCustom = new MobileServiceCustom(activity.getApplicationContext());

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
                        mAdapter = new NoticiaCompletaAdapter(myTypes,getActivity());


                        mRecyclerView.setAdapter(mAdapter);


                        try {
                           // mAdapter.addAll(myTypes);
                        } catch (final Exception exception) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "e " + exception.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                       // headerRecyclerView.attachTo(mRecyclerView);



                        //mAdapter.addAll(myTypes);

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
            txtvSad.setText(getResources().getString(R.string.no_posts_user));

        }
        else
        {
            imgvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
            txtvSad.setVisibility(View.GONE);
        }
        userContainer.setEnabled(true);
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
