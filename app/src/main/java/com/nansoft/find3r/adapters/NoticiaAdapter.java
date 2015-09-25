package com.nansoft.find3r.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nansoft.find3r.R;

/**
 * Created by User on 6/20/2015.
 */
public class NoticiaAdapter extends ArrayAdapter<com.nansoft.find3r.models.Noticia>
{
    Context mContext;
    int mLayoutResourceId;

    public NoticiaAdapter(Context context, int resource)
    {
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View row = convertView;
        final com.nansoft.find3r.models.Noticia currentItem = getItem(position);

        // verificamos si la fila que se va dibujar no existe
        if (row == null)
        {
            // si es así la creamos
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.imgvImagen = (ImageView) row.findViewById(R.id.imgvNoticia);

            viewHolder.txtvTitulo = (TextView) row.findViewById(R.id.txtvNombreUsuarioNoticia);

           // viewHolder.txtvSubtitulo = (TextView) row.findViewById(R.id.txtvSubtituloNoticia);

            viewHolder.imgvEstado = (ImageView) row.findViewById(R.id.imgvEstado);

            viewHolder.txtvEstado = (TextView) row.findViewById(R.id.txtvEstado);

            viewHolder.txtvFecha = (TextView) row.findViewById(R.id.txtvFechaNoticia);

            row.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();

        Glide.with(mContext)
                .load(currentItem.getUrlimagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvImagen);


        viewHolder.txtvTitulo.setText(currentItem.getNombre());

       // viewHolder.txtvSubtitulo.setText(currentItem.getDescripcion());

        viewHolder.txtvFecha.setText(currentItem.getFechadesaparicion());

        if(currentItem.getIdestado().trim().equals("0"))
        {
            viewHolder.txtvEstado.setText(mContext.getString(R.string.lost));
            viewHolder.imgvEstado.setImageResource(mContext.getResources().getIdentifier("lost","drawable", mContext.getPackageName()));

        }
        else
        {
            viewHolder.txtvEstado.setText(mContext.getString(R.string.found));
            viewHolder.imgvEstado.setImageResource(mContext.getResources().getIdentifier("found","drawable", mContext.getPackageName()));

        }





        //row.setTag(viewHolder);
        return row;

    }

    static class ViewHolder
    {
        public TextView txtvTitulo;
        public TextView txtvSubtitulo;
        public TextView txtvEstado;
        public TextView txtvFecha;
        public ImageView imgvImagen;
        public ImageView imgvEstado;
    }

}
