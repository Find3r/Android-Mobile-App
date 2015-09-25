package com.nansoft.find3r.activity;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.MyTime;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComentarioAdapter;
import com.nansoft.find3r.models.Comentario;

import java.net.MalformedURLException;

public class ComentarioActivity extends ActionBarActivity {

    String idNoticia = "";
    ComentarioAdapter adapter;
    public static SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentario_activity);

        adapter = new ComentarioAdapter(this,R.layout.comment_item);
        ListView listview = (ListView) findViewById(R.id.lstvComentarios);
        listview.setAdapter(adapter);

        idNoticia = getIntent().getExtras().getString("idNoticia");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlComentarios);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarComentarios();
            }
        });

        Button btnAgregarComentario = (Button) findViewById(R.id.btnChekComment);

        btnAgregarComentario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                EditText edtDescripcionComentario = (EditText) findViewById(R.id.edtDescripcionComentario_add_comment);

                if (edtDescripcionComentario.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Debe ingresar datos en el comentario",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    agregarComentario(new Comentario("", edtDescripcionComentario.getText().toString(), "1", MyTime.getFecha(), MyTime.getHora(), idNoticia), edtDescripcionComentario);
                }
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        cargarComentarios();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comentario, menu);
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

    public void cargarComentarios() {

        new AsyncTask<Void, Void, Boolean>() {

            MobileServiceClient mClient;
            MobileServiceTable<Comentario> mComentarioTable;

            @Override
            protected void onPreExecute() {
                try {
                    mClient = new MobileServiceClient(
                            "https://wantedapp.azure-mobile.net/",
                            "MIqlLCMyhKNIonsgsNuFlpBXzqqNWj11",
                            getApplicationContext()
                    );
                } catch (MalformedURLException e) {

                }
                mComentarioTable = mClient.getTable("comentario", Comentario.class);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    final MobileServiceList<Comentario> result = mComentarioTable.where().field("idnoticia").eq(idNoticia).execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            adapter.clear();
                            for (Comentario item : result)
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
            protected void onPostExecute(Boolean success) {

                mSwipeRefreshLayout.setRefreshing(false);
                if (!success)
                    Toast.makeText(getApplicationContext(),"Verifique la conexión a internet",Toast.LENGTH_SHORT).show();

            }
        }.execute();
    }

    public void agregarComentario(final Comentario objComentario,final EditText edtCheckComment)
    {

        new AsyncTask<Void, Void, Boolean>()
        {

            MobileServiceClient mClient;
            MobileServiceTable<Comentario> mComentarioTable;

            @Override
            protected void onPreExecute() {
                try {
                    mClient = new MobileServiceClient(
                            "https://wantedapp.azure-mobile.net/",
                            "MIqlLCMyhKNIonsgsNuFlpBXzqqNWj11",
                            getApplicationContext()
                    );
                } catch (MalformedURLException e) {

                }
                mComentarioTable = mClient.getTable("comentario", Comentario.class);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mComentarioTable.insert(objComentario).get();
                         runOnUiThread(new Runnable() {
                            public void run() {
                                adapter.add(objComentario);
                            }
                        });

                        return true;
                } catch (Exception exception) {

                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if(success)
                {
                    Toast.makeText(getApplicationContext(),"Comentario agregado",Toast.LENGTH_SHORT).show();
                    edtCheckComment.setText("");
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }
}
