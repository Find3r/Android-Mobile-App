package com.nansoft.find3r.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.activity.PerfilUsuarioActivity;
import com.nansoft.find3r.activity.UserProfileActivity;
import com.nansoft.find3r.helpers.CircularImageView;
import com.nansoft.find3r.models.Comentario;
import com.nansoft.find3r.models.ComentarioCompleto;
import com.nansoft.find3r.models.NoticiaCompleta;
import com.nansoft.find3r.models.Notificacion;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by User on 7/4/2015.
 */
public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder>
{

    private ComentarioCompleto[] lstComentarios;
    private Context context;

    public ComentarioAdapter(ComentarioCompleto[] plstComentarios,Context pcontext)
    {
        this.lstComentarios = plstComentarios;
        context = pcontext;
    }


    @Override
    public int getItemCount()
    {
        return lstComentarios.length;
    }

    @Override
    public void onBindViewHolder(ComentarioViewHolder contactViewHolder,final int position)
    {
        // se obtiene el objeto actual
        ComentarioCompleto objComentario= lstComentarios[position];

        // establecemos los atributos
        contactViewHolder.txtvTitulo.setText(objComentario.getNombre());
        contactViewHolder.txtvSubtitulo.setText(objComentario.getDescripcion());

        contactViewHolder.txtvFecha.setText(objComentario.getFecha());

        Glide.with(context)
                .load(objComentario.getUrlImagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(contactViewHolder.imgvLogoUsuario);

        // se establece onClickListener en el componente de cada vista
        contactViewHolder.txtvTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarPerfilUsuario(position,context);
            }
        });

        contactViewHolder.imgvLogoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarPerfilUsuario(position,context);
            }
        });

    }

    private void navegarPerfilUsuario(int position,Context context)
    {

        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("ID_USUARIO",lstComentarios[position].getIdUsuario());
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public ComentarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {

        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.comment_item, viewGroup, false);

        return new ComentarioViewHolder(itemView);

    }


    static class ComentarioViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtvTitulo;
        public TextView txtvSubtitulo;
        public TextView txtvFecha;
        public CircularImageView imgvLogoUsuario;

        public ComentarioViewHolder(View view)
        {
            super(view);

            imgvLogoUsuario = (CircularImageView) view.findViewById(R.id.imgvLogoUsuario_comment);
            txtvTitulo = (TextView) view.findViewById(R.id.txtvNombreUsuario_comment);
            txtvSubtitulo = (TextView) view.findViewById(R.id.txtvDescripcion_comment);
            txtvFecha = (TextView) view.findViewById(R.id.txtvFecha_comment);
        }
    }
}
