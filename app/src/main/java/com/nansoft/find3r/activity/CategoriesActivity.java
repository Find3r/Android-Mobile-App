package com.nansoft.find3r.activity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.ComplexRecyclerViewAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Categoria;

import java.util.ArrayList;

public class CategoriesActivity extends CustomAppCompatActivity {

    //public static CategoriaAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom mobileServiceCustom;
    RecyclerView mRecyclerView;
    ArrayList<Object> itemsCollection;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorias_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View includedLayout = findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));

        mobileServiceCustom = new MobileServiceCustom(this);

        //now you must initialize your list view
        //GridView listview =(GridView) findViewById(R.id.gridAreas);

        // Lookup the recyclerview in activity layout
        mRecyclerView = (RecyclerView) findViewById(R.id.gridAreas);

        /*
        adapter = new CategoriaAdapter(this,R.layout.categoria_item);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Intent intent = new Intent(view.getContext(), NoticiasCategoriaActivity.class);
                intent.putExtra("idCategoria",adapter.getItem(position).getId());
                intent.putExtra("nombreCategoria",adapter.getItem(position).getNombre());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });
        */
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlCategorias);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarCategorias();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        cargarCategorias();
    }



    public void cargarCategorias()
    {
        itemsCollection = new ArrayList<>();
        new AsyncTask<Void, Void, Boolean>() {


            MobileServiceTable<Categoria> mCategoriaTable;

            @Override
            protected void onPreExecute()
            {

                mCategoriaTable = mobileServiceCustom.mClient.getTable("categoria", Categoria.class);

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    final MobileServiceList<Categoria> result = mCategoriaTable.orderBy("nombre", QueryOrder.Ascending).execute().get();


                    for (Categoria item : result) {
                        itemsCollection.add(item);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(new ComplexRecyclerViewAdapter(itemsCollection, CategoriesActivity.this));

                            mRecyclerView.setLayoutManager(new GridLayoutManager(CategoriesActivity.this,2));
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

                estadoAdapter(success);

            }
        }.execute();
    }


    private void estadoAdapter(boolean success)
    {

        mSwipeRefreshLayout.setRefreshing(false);
        if (!success)
        {
            imgvSad.setVisibility(View.VISIBLE);
            txtvSad.setVisibility(View.VISIBLE);
        }
        else
        {
            imgvSad.setVisibility(View.INVISIBLE);
            txtvSad.setVisibility(View.INVISIBLE);
        }
    }

}
