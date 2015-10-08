package com.nansoft.find3r.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.NoticiaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.ComentarioCompleto;
import com.nansoft.find3r.models.Noticia;
import com.nansoft.find3r.models.Usuario;
import com.nansoft.find3r.models.UsuarioFacebook;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/21/2015.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {

    ListView listview;
    NoticiaAdapter adapter;

    TextView txtvNombreUsuario;
    ImageView imgvPerfilUsuario;
    ImageView imgvCelular;
    ImageView imgvTelefono;
    ImageView imgvEmail;

    MobileServiceCustom customClient;

   SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.usuario_layout, container, false);
        View headerListView = inflater.inflate(R.layout.perfil_usuario_header,null);

        listview = (ListView) view.findViewById(R.id.lstvPublicacionesUsuario);
        listview.addHeaderView(headerListView);

        //now you must initialize your list view
        adapter = new NoticiaAdapter(view.getContext(), R.layout.noticia_item);

        listview.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swprlPerfilUsuario);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);





        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarUsuario(getActivity());
            }
        });


        txtvNombreUsuario = (TextView) headerListView.findViewById(R.id.txtvNombreUsuario);
        imgvPerfilUsuario = (ImageView) headerListView.findViewById(R.id.imgvPerfilUsuario);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

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

        try {

            txtvNombreUsuario.setText(MobileServiceCustom.USUARIO_LOGUEADO.getNombre());

            Glide.with(activity.getApplicationContext())
                    .load(MobileServiceCustom.USUARIO_LOGUEADO.getUrlimagen().trim())
                    .asBitmap()
                    .fitCenter()
                    .placeholder(R.drawable.picture_default)
                    .error(R.drawable.error_image)
                    .into(imgvPerfilUsuario);

            adapter.clear();


            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",MobileServiceCustom.USUARIO_LOGUEADO.getId()));

            MobileServiceCustom mobileServiceCustom = new MobileServiceCustom(activity.getApplicationContext());

            ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("news_user", "GET", parameters);

            Futures.addCallback(lst, new FutureCallback<JsonElement>() {
                @Override
                public void onFailure(Throwable exc) {


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
                            final Noticia objNoticia = objGson.fromJson(element, Noticia.class);

                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    adapter.add(objNoticia);
                                    adapter.notifyDataSetChanged();


                                }
                            });
                        }

                    }

                    mSwipeRefreshLayout.setRefreshing(false);


                }
            });
        }
        catch (Exception e )
        {

        }


    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

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
}
