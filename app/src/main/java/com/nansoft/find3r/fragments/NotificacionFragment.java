package com.nansoft.find3r.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.adapters.NotificacionAdapter;
import com.nansoft.find3r.models.Notificacion;
import com.nansoft.find3r.models.NotificacionUsuario;

import java.net.MalformedURLException;

/**
 * Created by User on 7/5/2015.
 */
public class NotificacionFragment  extends Fragment
{
    public static NotificacionAdapter adapter;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.notificacion_layout, container, false);

        final ListView listview = (ListView) view.findViewById(R.id.lstvNotificacion);

        adapter = new NotificacionAdapter(view.getContext(), R.layout.notificacion_item);
        mContext = view.getContext();


        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), ComentarioActivity.class);
                intent.putExtra("idNoticia",adapter.getItem(i).getId());
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swprlNotificacion);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarNotificaciones(getActivity());
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        cargarNotificaciones(getActivity());

        return view;
    }

    public void cargarNotificaciones(final FragmentActivity activity) {

        new AsyncTask<Void, Void, Boolean>() {

            MobileServiceClient mClient;
            MobileServiceTable<NotificacionUsuario> mNotificacionUsuarioTable;
            MobileServiceTable<Notificacion> mNotificacionTable;

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
                mNotificacionUsuarioTable = mClient.getTable("notificacionusuario", NotificacionUsuario.class);
                mNotificacionTable = mClient.getTable("notificacion", Notificacion.class);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    final MobileServiceList<NotificacionUsuario> result = mNotificacionUsuarioTable.where().field("idusuario").eq("1").top(10).execute().get();
                    Notificacion objNotificacion;
                    for (NotificacionUsuario item : result)
                    {
                        objNotificacion = mNotificacionTable.lookUp(item.getIdnotificacion()).get();
                        item.setDescripcion(objNotificacion.getDescripcion());
                    }

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            adapter.clear();

                            for (NotificacionUsuario item : result) {

                                adapter.add(item);
                                adapter.notifyDataSetChanged();
                            }
                            //Toast.makeText(mContext,datos,Toast.LENGTH_SHORT).show();

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

                mSwipeRefreshLayout.setRefreshing(false);
                if (!success)
                    Toast.makeText(activity.getApplicationContext(), "Verifique la conexión a internet", Toast.LENGTH_SHORT).show();

            }

            @Override
            protected void onCancelled()
            {
                super.onCancelled();
            }
        }.execute();
    }
}
