package com.nansoft.find3r.activity;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_description);



        TextView txtvNewName = (TextView) findViewById(R.id.txtvNewName);
        TextView txtvNewDescription = (TextView) findViewById(R.id.txtvNewDescription);
        PhotoView imgvNewDescription = (PhotoView) findViewById(R.id.imgvNewDescription);

        TextView txtvEstado = (TextView) findViewById(R.id.txtvEstado);
        ImageView imgvEstado = (ImageView) findViewById(R.id.imgvEstado);

        txtvNewDescription.setMovementMethod(new ScrollingMovementMethod());

        try {

            //String urlImagen = getIntent().getExtras().getString("URL_IMAGEN");
            final NoticiaCompleta objNoticiaCompleta = (NoticiaCompleta) getIntent().getParcelableExtra("obj");

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



    }
}
