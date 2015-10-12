package com.nansoft.find3r.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.NoticiaCompletaAdapter;
import com.nansoft.find3r.models.Noticia;

import java.net.MalformedURLException;

public class NoticiasCategoriaActivity extends ActionBarActivity {

    String idCategoria = "";
    SwipeRefreshLayout mSwipeRefreshLayout;
    NoticiaCompletaAdapter adapter;
    ImageView imgvSad;
    TextView txtvSad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticias_layout);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));

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
                /*
                Intent intent = new Intent(getApplicationContext(), ComentarioActivity.class);
                intent.putExtra("idNoticia",adapter.getItem(i).getId());
                startActivity(intent);
                */
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
                            getApplicationContext()
                    );
                    adapter.clear();
                } catch (MalformedURLException e) {

                }
                mNoticiaTable = mClient.getTable("noticia", Noticia.class);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    final MobileServiceList<Noticia> result = mNoticiaTable.where().field("idcategoria").eq(idCategoria).and().field("eliminado").eq(false).orderBy("fechadesaparicion", QueryOrder.Descending).execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


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
