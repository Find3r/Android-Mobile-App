package com.nansoft.find3r.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.activity.NewDescriptionActivity;
import com.nansoft.find3r.activity.UserProfileActivity;
import com.nansoft.find3r.fragments.ItemOptionsDialog;
import com.nansoft.find3r.helpers.CircularImageView;
import com.nansoft.find3r.models.NoticiaCompleta;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by User on 6/20/2015.
 */
public class NoticiaCompletaAdapter extends RecyclerView.Adapter<NoticiaCompletaAdapter.NoticiaCompletaViewHolder>
{
    private List<NoticiaCompleta> lstNoticias;
    private PhotoViewAttacher mAttacher;
    private Context context;

    public NoticiaCompletaAdapter(List<NoticiaCompleta> plstNoticias,Context pcontext)
    {
        this.lstNoticias = plstNoticias;
        context = pcontext;
    }





    @Override
    public int getItemCount()
    {
        return lstNoticias.size();
    }

    @Override
    public void onBindViewHolder(NoticiaCompletaViewHolder viewHolder,final int position)
    {
        // se obtiene el objeto actual
        NoticiaCompleta objNoticia = lstNoticias.get(position);

        viewHolder.txtvNombreUsuarioNoticia.setText(objNoticia.getNombreUsuario());

        Glide.with(context)
                .load(objNoticia.getUrlImagenUsuario().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvFotoPerfilUsuario);


        Glide.with(context)
                .load(objNoticia.getUrlImagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvImagen);

        /*
        Picasso.with(context).load(objNoticia.getUrlImagen().trim())
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)

                .into(viewHolder.imgvImagen);
        */


        viewHolder.imgvImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NewDescriptionActivity.class);
                //intent.putExtra("URL_IMAGEN",lstNoticias.get(position).getUrlImagen());
                intent.putExtra("obj", lstNoticias.get(position));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);



            }
        });

        // establecemos los atributos
        viewHolder.txtvTitulo.setText(objNoticia.getNombre());
        viewHolder.txtvDescripcion.setText(objNoticia.getDescripcion());
        viewHolder.txtvFecha.setText("Desparecid@ el " + objNoticia.getFechadesaparicion());

        viewHolder.txtvCantidadComentarios.setText(String.valueOf(objNoticia.getCantidadComentarios()));

        if(objNoticia.getIdestado().trim().equals("0"))
        {
            viewHolder.txtvEstado.setText(context.getString(R.string.lost));
            viewHolder.imgvEstado.setImageResource(context.getResources().getIdentifier("lost","drawable", context.getPackageName()));

        }
        else
        {
            viewHolder.txtvEstado.setText(context.getString(R.string.found));
            viewHolder.imgvEstado.setImageResource(context.getResources().getIdentifier("found","drawable", context.getPackageName()));

        }

        // se establece onClickListener en el componente de cada vista

        viewHolder.layImagenComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComentarioActivity.class);
                intent.putExtra("idNoticia", lstNoticias.get(position).getId());
                context.startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        viewHolder.txtvNombreUsuarioNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarPerfilUsuario(position, v.getContext());
            }
        });
        viewHolder.imgvFotoPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarPerfilUsuario(position,v.getContext());
            }
        });

        viewHolder.imgvNoticiaOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemOptionsDialog(lstNoticias.get(position));
            }
        });

    }

    private void showItemOptionsDialog(NoticiaCompleta pobjNoticia) {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();

        // enviamos por parámetros el objeto noticia en el que se dió click
        Bundle args = new Bundle();
        args.putParcelable("objNoticia", pobjNoticia);

        ItemOptionsDialog itemOptionsDialog = ItemOptionsDialog.newInstance("Some Title");

        // establecemos los parámetros
        itemOptionsDialog.setArguments(args);
        itemOptionsDialog.show(fm, "fragment_edit_name");

    }





    private void navegarPerfilUsuario(int position,Context context)
    {
        //Intent intent = new Intent(context, PerfilUsuarioActivity.class);
        //intent.putExtra("id",lstNoticias[position].getIdusuario());
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("ID_USUARIO",lstNoticias.get(position).getIdusuario());

        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }




    @Override
    public NoticiaCompletaViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {

        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.new_item, viewGroup, false);

        return new NoticiaCompletaViewHolder(itemView);

    }


    public static class NoticiaCompletaViewHolder extends RecyclerView.ViewHolder
    {

        public TextView txtvTitulo;
        public TextView txtvDescripcion;
        public TextView txtvEstado;
        public TextView txtvFecha;
        public TextView txtvCantidadComentarios;
        public TextView txtvNombreUsuarioNoticia;
        public CircularImageView imgvFotoPerfilUsuario;
        public ImageView imgvImagen;
        public LinearLayout layImagenComentario;
        public ImageView imgvEstado;
        public ImageView imgvSeguimiento;
        public ImageView imgvComentario;
        public ImageView imgvNoticiaOptions;

        public NoticiaCompletaViewHolder(View view)
        {
            super(view);



            imgvFotoPerfilUsuario = (CircularImageView) view.findViewById(R.id.imgvPerfilUsuarioNoticia);

            txtvNombreUsuarioNoticia = (TextView) view.findViewById(R.id.txtvNombreUsuarioNoticia);

            imgvImagen = (ImageView) view.findViewById(R.id.imgvNoticia);

            txtvTitulo = (TextView) view.findViewById(R.id.txtvNombreNoticia);

            txtvDescripcion = (TextView) view.findViewById(R.id.txtvDescripcionNoticia);

            imgvEstado = (ImageView) view.findViewById(R.id.imgvEstado);

            imgvComentario = (ImageView) view.findViewById(R.id.imgvComentario);

            txtvCantidadComentarios = (TextView) view.findViewById(R.id.txtvCantidadComentarios);

            //viewHolder.imgvSeguimiento = (ImageView) row.findViewById(R.id.imgvSeguimiento);

            txtvEstado = (TextView) view.findViewById(R.id.txtvEstado);

            txtvFecha = (TextView) view.findViewById(R.id.txtvFechaNoticia);

            layImagenComentario = (LinearLayout) view.findViewById(R.id.layImagenComentario);

            imgvNoticiaOptions = (ImageView) view.findViewById(R.id.imgvNoticiaOptions);
        }


    }

}
