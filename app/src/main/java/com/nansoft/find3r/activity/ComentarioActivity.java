package com.nansoft.find3r.activity;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.MyTime;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComentarioAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Comentario;
import com.nansoft.find3r.models.ComentarioCompleto;
import com.nansoft.find3r.models.Noticia;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ComentarioActivity extends ActionBarActivity {

    String ID_NOTICIA = "";
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

        ID_NOTICIA = getIntent().getExtras().getString("idNoticia");

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
                    agregarComentario(new Comentario("", edtDescripcionComentario.getText().toString(), "1", MyTime.getFecha(), MyTime.getHora(), ID_NOTICIA), edtDescripcionComentario);
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

        mSwipeRefreshLayout.setEnabled(false);


        try {

            adapter.clear();

            MobileServiceCustom mobileServiceCustom = new MobileServiceCustom(this);
            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",ID_NOTICIA));

            ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("postcomments", "GET", parameters);

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
                            final ComentarioCompleto objLastNews = objGson.fromJson(element, ComentarioCompleto.class);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    adapter.add(objLastNews);
                                    adapter.notifyDataSetChanged();


                                }
                            });
                        }

                    }




                }
            });

        }
        catch (Exception e )
        {

        }





    }

    public void agregarComentario(final Comentario objComentario,final EditText edtCheckComment)
    {
        /*
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
        */
    }
}
