package com.nansoft.find3r.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nansoft.find3r.R;
import com.nansoft.find3r.models.NoticiaCompleta;

import uk.co.senab.photoview.PhotoView;

public class NewDescriptionActivity extends CustomAppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    TextView txtvNewName;
    TextView txtvNewDescription;
    PhotoView imgvNewDescription;

    TextView txtvEstado;
    ImageView imgvEstado;

    NoticiaCompleta objNoticiaCompleta;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_description);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swprlNewDescription);
        txtvNewName = (TextView) findViewById(R.id.txtvNewName);
        txtvNewDescription = (TextView) findViewById(R.id.txtvNewDescription);
        imgvNewDescription = (PhotoView) findViewById(R.id.imgvNewDescription);

        txtvEstado = (TextView) findViewById(R.id.txtvEstado);
        imgvEstado = (ImageView) findViewById(R.id.imgvEstado);

        txtvNewDescription.setMovementMethod(new ScrollingMovementMethod());

        try {


            objNoticiaCompleta = (NoticiaCompleta) getIntent().getParcelableExtra("obj");

            cargarDatos();

            findViewById(R.id.imgvComentario).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewDescriptionActivity.this, ComentarioActivity.class);
                    intent.putExtra("idNoticia", objNoticiaCompleta.getId());
                    startActivity(intent);
                    ((Activity) v.getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            });

        } catch (Exception exception) {

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarDatos();
            }
        });


    }

    private void cargarDatos()
    {
        txtvNewName.setText(objNoticiaCompleta.getNombre());

        txtvNewDescription.setText(objNoticiaCompleta.getDescripcion());

        Glide.with(this)
                .load(objNoticiaCompleta.getUrlImagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(imgvNewDescription);

        if(objNoticiaCompleta.getIdestado().trim().equals("0"))
        {
            txtvEstado.setText(getString(R.string.lost));
            imgvEstado.setImageResource(getResources().getIdentifier("lost","drawable", getPackageName()));

        }
        else
        {
            txtvEstado.setText(getString(R.string.found));
            imgvEstado.setImageResource(getResources().getIdentifier("found","drawable", getPackageName()));

        }

        swipeRefreshLayout.setRefreshing(false);
    }
}
