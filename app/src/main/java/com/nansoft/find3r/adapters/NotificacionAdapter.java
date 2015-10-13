package com.nansoft.find3r.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.models.Notificacion;

import java.util.List;

/**
 * Created by User on 7/5/2015.
 */
public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>
{

    private List<Notificacion> lstNotifications;

    public NotificacionAdapter(List<Notificacion> plstNotifications)
    {
        this.lstNotifications = plstNotifications;
    }

    @Override
    public int getItemCount()
    {
        return lstNotifications.size();
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder contactViewHolder,final int position)
    {
        // se obtiene el objeto actual
        Notificacion objNotificacion = lstNotifications.get(position);

        // establecemos los atributos
        contactViewHolder.txtvTitulo.setText(objNotificacion.getDescripcion());
        contactViewHolder.txtvSubtitulo.setText(objNotificacion.getFechaCreacion());

        // se establece onClickListener en el componente de cada vista
        contactViewHolder.layItemNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ComentarioActivity.class);
                intent.putExtra("idNoticia", lstNotifications.get(position).getIdnoticia());
                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {

        View itemView = LayoutInflater.from(viewGroup.getContext()).
            inflate(R.layout.notificacion_item, viewGroup, false);

        return new NotificacionViewHolder(itemView);

       }


    static class NotificacionViewHolder extends RecyclerView.ViewHolder
    {
        public RelativeLayout layItemNotificacion;
        public TextView txtvTitulo;
        public TextView txtvSubtitulo;
        public ImageView imgvImagen;

        public NotificacionViewHolder(View view)
        {
            super(view);

            layItemNotificacion = (RelativeLayout) view.findViewById(R.id.layItemNotificacion);

            imgvImagen = (ImageView) view.findViewById(R.id.imgvNotificacion);

            txtvTitulo = (TextView) view.findViewById(R.id.txtvNombreUsuarioNotificacion);


            txtvSubtitulo = (TextView) view.findViewById(R.id.txtvSubtituloNotificación);
        }
    }

}
