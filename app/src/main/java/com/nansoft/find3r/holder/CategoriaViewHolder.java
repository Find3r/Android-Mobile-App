package com.nansoft.find3r.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nansoft.find3r.R;

/**
 * Created by Carlos on 30/10/2015.
 */
public class CategoriaViewHolder extends RecyclerView.ViewHolder{
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public LinearLayout layItemCategoria;
    public TextView txtvTitulo;
    public ImageView imgvImagen;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public CategoriaViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);
        layItemCategoria = (LinearLayout) itemView.findViewById(R.id.layItemCategoria);
        txtvTitulo = (TextView) itemView.findViewById(R.id.txtvTituloCategoria);
        imgvImagen = (ImageView) itemView.findViewById(R.id.imgvCategoria);
    }
}
