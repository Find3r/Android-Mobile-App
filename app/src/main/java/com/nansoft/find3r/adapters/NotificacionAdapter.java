package com.nansoft.find3r.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.models.Notificacion;

import java.util.List;

/**
 * Created by User on 7/5/2015.
 */
public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>  {

    private List<Notificacion> lstNotifications;

    protected View mRootView;

    public NotificacionAdapter(List<Notificacion> plstNotifications)
    {
        this.lstNotifications = plstNotifications;


    }

    @Override

    public int getItemCount() {

        return lstNotifications.size();

    }

    @Override

    public void onBindViewHolder(NotificacionViewHolder contactViewHolder,final  int position)
    {

        Notificacion ci = lstNotifications.get(position);
        contactViewHolder.txtvTitulo.setText(ci.getDescripcion());

        contactViewHolder.txtvSubtitulo.setText(ci.getFechaCreacion());

        contactViewHolder.txtvTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ComentarioActivity.class);
                intent.putExtra("idNoticia", lstNotifications.get(position).getIdnoticia());
                v.getContext().startActivity(intent);
            }
        });




    }

    @Override

       public NotificacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

            View itemView = LayoutInflater.from(viewGroup.getContext()).

            inflate(R.layout.notificacion_item, viewGroup, false);

            return new NotificacionViewHolder(itemView);

       }




    static class NotificacionViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtvTitulo;
        public TextView txtvSubtitulo;
        public ImageView imgvImagen;

        public NotificacionViewHolder(View view) {
            super(view);

            imgvImagen = (ImageView) view.findViewById(R.id.imgvNotificacion);

            txtvTitulo = (TextView) view.findViewById(R.id.txtvNombreUsuarioNotificacion);


           txtvSubtitulo = (TextView) view.findViewById(R.id.txtvSubtituloNotificación);
        }
    }

}
