package com.nansoft.find3r.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nansoft.find3r.R;
import com.nansoft.find3r.models.NotificacionUsuario;

/**
 * Created by User on 7/5/2015.
 */
public class NotificacionAdapter extends ArrayAdapter<NotificacionUsuario>
{
    Context mContext;
    int mLayoutResourceId;

    public NotificacionAdapter(Context context, int resource)
    {
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View row = convertView;
        final NotificacionUsuario currentItem = getItem(position);

        // verificamos si la fila que se va dibujar no existe
        if (row == null)
        {
            // si es así la creamos
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.imgvImagen = (ImageView) row.findViewById(R.id.imgvNotificacion);

            viewHolder.txtvTitulo = (TextView) row.findViewById(R.id.txtvNombreUsuarioNotificacion);


            viewHolder.txtvFecha = (TextView) row.findViewById(R.id.txtvSubtituloNotificación);

            row.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();
        /*
        Glide.with(mContext)
                .load(currentItem.getUrlimagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvImagen);

        */
        viewHolder.txtvTitulo.setText(currentItem.getDescripcion());

        viewHolder.txtvFecha.setText(currentItem.getFecha());




        //row.setTag(viewHolder);
        return row;

    }


    static class ViewHolder
    {
        public TextView txtvTitulo;
        public TextView txtvFecha;
        public ImageView imgvImagen;
    }

}
