package com.nansoft.find3r.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.NoticiasCategoriaActivity;
import com.nansoft.find3r.adapters.CategoriaAdapter;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Categoria;

import java.net.MalformedURLException;


/**
 * Created by User on 6/19/2015.
 */
public class CategoriaFragment extends Fragment
{
    public static CategoriaAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView imgvSad;
    TextView txtvSad;

    MobileServiceCustom mobileServiceCustom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.categorias_layout, container, false);

        View includedLayout = view.findViewById(R.id.sindatos);
        imgvSad = (ImageView) includedLayout.findViewById(R.id.imgvInfoProblema);
        txtvSad = (TextView) includedLayout.findViewById(R.id.txtvInfoProblema);
        txtvSad.setText(getResources().getString(R.string.noconnection));

        mobileServiceCustom = new MobileServiceCustom(view.getContext());

        //now you must initialize your list view
        GridView listview =(GridView)view.findViewById(R.id.gridAreas);

        adapter = new CategoriaAdapter(view.getContext(),R.layout.categoria_item);

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
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swprlCategorias);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.android_darkorange, R.color.green, R.color.android_blue);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarCategorias(getActivity());
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        cargarCategorias(getActivity());

        return view;
    }

    public void cargarCategorias(final FragmentActivity activity)
    {

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
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            adapter.clear();

                            for (Categoria item : result)
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
        }.execute();
    }


}
