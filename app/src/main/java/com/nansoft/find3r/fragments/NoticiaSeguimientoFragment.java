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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.InfoNoticiaActivity;
import com.nansoft.find3r.adapters.NoticiaAdapter;
import com.nansoft.find3r.models.Noticia;

import java.net.MalformedURLException;

/**
 * Created by Carlos on 01/10/2015.
 */
public class NoticiaSeguimientoFragment extends Fragment
{
    public static NoticiaAdapter adapter;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
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
        new AsyncTask<Void, Void, Boolean>() {

            MobileServiceClient mClient;
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
                    adapter.clear();
                } catch (MalformedURLException e) {

                }
                mNoticiaTable = mClient.getTable("noticia", Noticia.class);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    final MobileServiceList<Noticia> result = mNoticiaTable.where().field("eliminado").eq(false).orderBy("fechadesaparicion", QueryOrder.Descending).execute().get();
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


                            String datos = "";
                            for (Noticia item : result) {

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
                mSwipeRefreshLayout.setEnabled(true);
                estadoAdapter(success);
            }

            @Override
            protected void onCancelled()
            {
                super.onCancelled();
            }
        }.execute();
    }

    private void estadoAdapter(boolean pEstadoError)
    {
        if(adapter.isEmpty() && pEstadoError)
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setText(getResources().getString(R.string.nodata));

        }
        else if (adapter.isEmpty() && !pEstadoError)
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setText(getResources().getString(R.string.noconnection));
        }
    }
}