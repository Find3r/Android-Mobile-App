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
import com.nansoft.find3r.models.Usuario;

/**
 * Created by User on 6/21/2015.
 */
public class UsuarioAdapter extends ArrayAdapter<Usuario>
{
    Context mContext;
    int mLayoutResourceId;
    public UsuarioAdapter(Context context, int resource)
    {
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View row = convertView;

        final Usuario currentItem = getItem(position);

        // verificamos si la fila que se va dibujar no existe
        if (row == null)
        {
            // si es así la creamos
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            ViewHolder holder = new ViewHolder();

            //holder.txtvTitulo = (TextView) row.findViewById(R.id.txtvSubtituloNoticia);
            //holder.txtvSubtitulo = (TextView) row.findViewById(R.id.txtvNombreNoticia);

            row.setTag(holder);
        }

        // en caso contrario la recuperamos
        ViewHolder holder = (ViewHolder) convertView.getTag();

        //holder.txtvTitulo.setText(currentItem.getNombre() + " " + currentItem.getPrimerapellido() + " " + currentItem.getSegundoapellido());





        return row;

    }

    static class ViewHolder
    {
        public TextView txtvTitulo;
        public TextView txtvSubtitulo;
        public ImageView imgvImagen;
    }
}
