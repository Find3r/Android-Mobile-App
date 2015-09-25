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
import com.nansoft.find3r.models.Categoria;

/**
 * Created by User on 6/19/2015.
 */
public class CategoriaAdapter extends ArrayAdapter<Categoria>
{

    int mLayoutResourceId;
    Context mContext;

    public CategoriaAdapter(Context context, int resource)
    {
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;

        final Categoria currentItem = getItem(position);

        // verificamos si la fila que se va dibujar no existe
        if (row == null) {
            // si es así la creamos
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
           // ViewHolder holder = new ViewHolder();
            ViewHolder holder = new ViewHolder();
            holder.txtvTitulo = (TextView) row.findViewById(R.id.txtvTituloCategoria);
            holder.imgvImagen = (ImageView) row.findViewById(R.id.imgvCategoria);

            row.setTag(holder);
        }
        // en caso contrario la recuperamos
        ViewHolder holder = (ViewHolder) row.getTag();

        holder.txtvTitulo.setText(currentItem.getNombre());

        Glide.with(mContext)
                .load(currentItem.getUrlimagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(holder.imgvImagen);

       return row;

    }

    static class ViewHolder
    {
        public TextView txtvTitulo;
        public ImageView imgvImagen;
    }
}
