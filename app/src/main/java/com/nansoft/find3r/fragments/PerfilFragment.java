package com.nansoft.find3r.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.NoticiaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
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

    Usuario objUsuario;

    MobileServiceCustom customClient;




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

        // cargamos la información de usuario
        cargarUsuario(getActivity(),view);




        return view;
    }


    public void cargarUsuario(final FragmentActivity activity,final View view) {

        List<Pair<String, String> > lp = new ArrayList<Pair<String, String> >();
        lp.add(new Pair("id", customClient.mClient.getCurrentUser().getUserId()));
        ListenableFuture<UsuarioFacebook> result = customClient.mClient.invokeApi("userlogin", "GET", null, UsuarioFacebook.class);

        Futures.addCallback(result, new FutureCallback<UsuarioFacebook>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(activity.getApplicationContext(), "Ha ocurrido un error al intentar conectar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(final UsuarioFacebook objUsuarioFacebook) {

                new AsyncTask<Void, Void, Boolean>() {


                    MobileServiceTable<Usuario> mUserTable;
                    MobileServiceTable<Noticia> mNoticiaTable;

                    @Override
                    protected void onPreExecute() {

                        mUserTable = customClient.mClient.getTable("usuario", Usuario.class);
                        mNoticiaTable = customClient.mClient.getTable("noticia", Noticia.class);
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        try {
                            // buscamos por el usuario
                            objUsuario = mUserTable.lookUp(objUsuarioFacebook.id).get();

                            // se verifica si el usuario es null
                            if(objUsuario == null)
                            {
                                // debemos de insertar el registro

                                // establecemos primero los atributos
                                objUsuario.setId(objUsuarioFacebook.id);
                                objUsuario.setNombre(objUsuarioFacebook.name);
                                objUsuario.setUrlimagen(objUsuarioFacebook.data.PictureURL.PictureURL);

                                // agregamos el registro
                                mUserTable.insert(objUsuario);
                            }

                            // se buscan las noticias del usuario
                            final MobileServiceList<Noticia> result = mNoticiaTable.where().field("idusuario").eq(objUsuario.getId()).execute().get();

                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    adapter.clear();
                                    for (Noticia item : result) {

                                        //adapter.add(item);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            });
                            return true;
                        } catch (Exception exception) {

                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {

                        //mSwipeRefreshLayout.setRefreshing(false);
                        if (!success)
                            Toast.makeText(activity.getApplicationContext(), "Verifique la conexión a internet", Toast.LENGTH_SHORT).show();
                        else {


                            txtvNombreUsuario.setText(objUsuario.getNombre());

                            Glide.with(view.getContext())
                                    .load(objUsuario.getUrlimagen().trim())
                                    .asBitmap()
                                    .fitCenter()
                                    .placeholder(R.drawable.picture_default)
                                    .error(R.drawable.error_image)
                                    .into(imgvPerfilUsuario);
                        }
                    }

                    @Override
                    protected void onCancelled() {
                        super.onCancelled();
                    }
                }.execute();

            }
        });




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
        String [] correoDestino = {objUsuario.getEmail()};
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
