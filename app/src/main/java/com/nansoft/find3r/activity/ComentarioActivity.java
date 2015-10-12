package com.nansoft.find3r.activity;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.MyTime;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComentarioAdapter;
import com.nansoft.find3r.helpers.CircularImageView;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Comentario;
import com.nansoft.find3r.models.ComentarioCompleto;
import com.nansoft.find3r.models.Noticia;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ComentarioActivity extends AppCompatActivity {

    String ID_NOTICIA = "";
    ComentarioAdapter adapter;
    public static SwipeRefreshLayout mSwipeRefreshLayout;

    ImageView imgvSad;
    TextView txtvSad;
    MobileServiceCustom mobileServiceCustom ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentario_activity);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.no_comments));

        mobileServiceCustom = new MobileServiceCustom(this);

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

        btnAgregarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText edtDescripcionComentario = (EditText) findViewById(R.id.edtDescripcionComentario_add_comment);

                if (edtDescripcionComentario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar datos en el comentario", Toast.LENGTH_SHORT).show();
                } else {
                    agregarComentario(new Comentario("", edtDescripcionComentario.getText().toString(), MobileServiceCustom.USUARIO_LOGUEADO.getId(), MyTime.getFecha(), MyTime.getHora(), ID_NOTICIA), edtDescripcionComentario);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cargarComentarios() {

        imgvSad.setVisibility(View.INVISIBLE);
        txtvSad.setVisibility(View.INVISIBLE);

        CircularImageView imgvPerfilUsuario = (CircularImageView) findViewById(R.id.imgvLogoUsuario_add_comment);
        TextView txtvNombreUsuario = (TextView) findViewById(R.id.txtvNombreUsuario_add_comment);

        txtvNombreUsuario.setText(MobileServiceCustom.USUARIO_LOGUEADO.getNombre());


        Glide.with(this)
                .load(MobileServiceCustom.USUARIO_LOGUEADO.getUrlimagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(imgvPerfilUsuario);


        try {

            adapter.clear();


            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<String, String>("id",ID_NOTICIA));

            ListenableFuture<JsonElement> lst = mobileServiceCustom.mClient.invokeApi("postcomments", "GET", parameters);

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

                    mSwipeRefreshLayout.setRefreshing(false);
                    estadoAdapter(false);

                }
            });
            mSwipeRefreshLayout.setEnabled(true);
        }
        catch (Exception e )
        {

        }





    }

    private void estadoAdapter(boolean pEstadoError)
    {
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
        if(pEstadoError || adapter.isEmpty())
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
            txtvSad.setText(getResources().getString(R.string.no_comments));

        }
        else
        {
            imgvSad.setVisibility(View.INVISIBLE);
            txtvSad.setVisibility(View.INVISIBLE);
            txtvSad.setVisibility(View.INVISIBLE);
        }
    }

    public void agregarComentario(final Comentario objComentario,final EditText edtCheckComment)
    {

        new AsyncTask<Void, Void, Boolean>()
        {

            MobileServiceClient mClient;
            MobileServiceTable<Comentario> mComentarioTable;

            @Override
            protected void onPreExecute() {


                mComentarioTable = mobileServiceCustom.mClient.getTable("comentario",Comentario.class);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mComentarioTable.insert(objComentario).get();



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
                else
                {
                    Toast.makeText(getApplicationContext(),"Ha ocurrido un error al intentar agregar el comentario, intenta de nuevo",Toast.LENGTH_SHORT).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
                cargarComentarios();
            }
        }.execute();

    }
}
