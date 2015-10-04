package com.nansoft.find3r.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComentarioAdapter;
import com.nansoft.find3r.adapters.NoticiaAdapter;
import com.nansoft.find3r.models.Comentario;
import com.nansoft.find3r.models.Noticia;

import java.net.MalformedURLException;

public class InfoNoticiaActivity extends ActionBarActivity {

    String idNoticia;
    public static SwipeRefreshLayout mSwipeRefreshLayout;

    ComentarioAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_noticia);
        idNoticia = getIntent().getExtras().getString("idNoticia");

        final ListView listview = (ListView) findViewById(R.id.lstvComentarioPost);

        //adapter = new NoticiaAdapter(this, R.layout.noticia_item);
        adapter = new ComentarioAdapter(this, R.layout.comment_item);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /*
                Intent intent = new Intent(view.getContext(), InfoNoticiaActivity.class);
                intent.putExtra("idNoticia",adapter.getItem(i).getId());
                startActivity(intent);
                */
            }
        });


        listview.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlInfoNoticia);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                cargarNoticia();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        cargarNoticia();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_info_noticia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cargarNoticia() {

        new AsyncTask<Void, Void, Boolean>() {

            MobileServiceClient mClient;
            MobileServiceTable<Comentario> comentarioTable;
            Noticia result;
            @Override
            protected void onPreExecute()
            {
                try {
                    mClient = new MobileServiceClient(
                            "https://wantedapp.azure-mobile.net/",
                            "MIqlLCMyhKNIonsgsNuFlpBXzqqNWj11",
                            getApplicationContext()
                    );
                } catch (MalformedURLException e) {

                }
                comentarioTable = mClient.getTable("comentario", Comentario.class);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {

                    final MobileServiceList<Comentario> result = comentarioTable.where().field("idnoticia").eq(idNoticia).execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


                            for (Comentario item : result) {
                                for (int i = 0; i < 40; i++) {
                                    adapter.add(item);
                                }

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
                if (!success)
                {
                    /*
                    layout.setVisibility(View.INVISIBLE);
                    imgvSad.setVisibility(View.VISIBLE);
                    txtvSad.setVisibility(View.VISIBLE);
                    */
                }
                else
                {
                    /*
                    imgvSad.setVisibility(View.INVISIBLE);
                    txtvSad.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    */


                }

            }

            @Override
            protected void onCancelled()
            {
                super.onCancelled();
            }
        }.execute();
    }
}
