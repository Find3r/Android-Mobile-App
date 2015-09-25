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
import com.nansoft.find3r.adapters.NoticiaAdapter;
import com.nansoft.find3r.models.Noticia;

import java.net.MalformedURLException;

public class InfoNoticiaActivity extends ActionBarActivity {

    String idNoticia;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView imgvPerfil;
    ImageView imgvInfoEstado;
    TextView txtvNombre;
    TextView txtvDescripcion;
    TextView txtvFecha;
    TextView txtvInfoEstado;
    RelativeLayout layout;
    ImageView imgvSad;
    TextView txtvSad;
    NoticiaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_noticia);
        idNoticia = getIntent().getExtras().getString("idNoticia");

        final ListView listview = (ListView) findViewById(R.id.lstvComentarioPost);

        adapter = new NoticiaAdapter(this, R.layout.noticia_item);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(view.getContext(), InfoNoticiaActivity.class);
                intent.putExtra("idNoticia",adapter.getItem(i).getId());
                startActivity(intent);
            }
        });

        /*
        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));
        */

        View headerListView = getLayoutInflater().inflate(R.layout.info_noticia_header,null);

        layout = (RelativeLayout) headerListView.findViewById(R.id.layInfoNoticia);
        imgvPerfil = (ImageView) headerListView.findViewById(R.id.imgvInfoNoticia);
        imgvInfoEstado = (ImageView) headerListView.findViewById(R.id.imgvInfoEstado);
        txtvNombre = (TextView) headerListView.findViewById(R.id.txtvInfoNombreNoticia);
        txtvDescripcion = (TextView) headerListView.findViewById(R.id.txtvInfoDescripcion);
        txtvFecha = (TextView) headerListView.findViewById(R.id.txtvInfoSubFecha);
        txtvInfoEstado = (TextView) headerListView.findViewById(R.id.txtvInfoEstado);

        listview.addHeaderView(headerListView);
        listview.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlInfoNoticia);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);

        ImageView imgvLlamada = (ImageView) findViewById(R.id.imgvInfoTelefono);
        imgvLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {

                    Intent Llamada = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"));
                    startActivity(Llamada);

                } catch (ActivityNotFoundException activityException) {

                    Toast.makeText(getBaseContext(), "Error al realizar la llamada, intente más tarde", Toast.LENGTH_SHORT).show();

                }
            }
        });

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
            MobileServiceTable<Noticia> mNoticiaTable;
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
                mNoticiaTable = mClient.getTable("noticia", Noticia.class);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    result = mNoticiaTable.lookUp(idNoticia).get();
                    final MobileServiceList<Noticia> result = mNoticiaTable.execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


                            for (Noticia item : result) {
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
                    txtvNombre.setText(result.getNombre());
                    txtvDescripcion.setText(result.getDescripcion());
                    txtvFecha.setText("Se desapareció el " + result.getFechadesaparicion());


                    if (result.getIdestado().equals("0"))
                    {
                        txtvInfoEstado.setText(R.string.lost);
                        imgvInfoEstado.setImageResource(R.drawable.lost);
                    }
                    else
                    {
                        txtvInfoEstado.setText(R.string.found);
                        imgvInfoEstado.setImageResource(R.drawable.found);
                    }

                    Glide.with(getApplicationContext())
                            .load(result.getUrlimagen().trim())
                            .asBitmap()
                            .fitCenter()
                            .placeholder(R.drawable.picture_default)
                            .error(R.drawable.error_image)
                            .into(imgvPerfil);
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
