package com.nansoft.find3r.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.NoticiaAdapter;
import com.nansoft.find3r.models.Noticia;
import com.nansoft.find3r.models.Usuario;

import java.net.MalformedURLException;

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


        cargarUsuario(getActivity(),view);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


        return view;
    }

    public void cargarUsuario(final FragmentActivity activity,final View view) {

        new AsyncTask<Void, Void, Boolean>() {

            MobileServiceClient mClient;
            MobileServiceTable<Usuario> mUserTable;
            MobileServiceTable<Noticia> mNoticiaTable;
            @Override
            protected void onPreExecute()
            {
                try {
                    mClient = new MobileServiceClient(
                            "https://wantedapp.azure-mobile.net/",
                            "MIqlLCMyhKNIonsgsNuFlpBXzqqNWj11",
                            activity.getApplicationContext()
                    );
                } catch (MalformedURLException e) {

                }
                mUserTable = mClient.getTable("usuario", Usuario.class);
                mNoticiaTable = mClient.getTable("noticia", Noticia.class);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    objUsuario = mUserTable.lookUp("1").get();

                    //final MobileServiceList<Noticia> result = mNoticiaTable.where().field("idusuario").eq("1").execute().get();
                    final MobileServiceList<Noticia> result = mNoticiaTable.execute().get();

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            adapter.clear();
                            for (Noticia item : result)
                            {

                                adapter.add(item);
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
            protected void onPostExecute(Boolean success)
            {

                //mSwipeRefreshLayout.setRefreshing(false);
                if (!success)
                    Toast.makeText(activity.getApplicationContext(), "Verifique la conexión a internet", Toast.LENGTH_SHORT).show();
                else
                {


                    txtvNombreUsuario.setText(objUsuario.getNombre() + " " + objUsuario.getPrimerapellido() + " " + objUsuario.getSegundoapellido());

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
            protected void onCancelled()
            {
                super.onCancelled();
            }
        }.execute();


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
