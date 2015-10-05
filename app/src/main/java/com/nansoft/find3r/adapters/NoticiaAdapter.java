package com.nansoft.find3r.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.nansoft.find3r.R;
import com.nansoft.find3r.helpers.CircularImageView;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Noticia;
import com.nansoft.find3r.models.NoticiaUsuario;

import org.w3c.dom.Text;

import java.net.MalformedURLException;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by User on 6/20/2015.
 */
public class NoticiaAdapter extends ArrayAdapter<com.nansoft.find3r.models.Noticia>
{
    Context mContext;
    int mLayoutResourceId;
    private PhotoViewAttacher mAttacher;

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

            viewHolder.imgvFotoPerfilUsuario = (CircularImageView) row.findViewById(R.id.imgvPerfilUsuarioNoticia);

            viewHolder.txtvNombreUsuarioNoticia = (TextView) row.findViewById(R.id.txtvNombreUsuarioNoticia);

            viewHolder.imgvImagen = (PhotoView) row.findViewById(R.id.imgvNoticia);

            viewHolder.txtvTitulo = (TextView) row.findViewById(R.id.txtvNombreNoticia);

            viewHolder.txtvDescripcion = (TextView) row.findViewById(R.id.txtvDescripcionNoticia);

            viewHolder.imgvEstado = (ImageView) row.findViewById(R.id.imgvEstado);

            viewHolder.imgvComentario = (ImageView) row.findViewById(R.id.imgvComentario);

            viewHolder.imgvSeguimiento = (ImageView) row.findViewById(R.id.imgvSeguimiento);

            viewHolder.txtvEstado = (TextView) row.findViewById(R.id.txtvEstado);

            viewHolder.txtvFecha = (TextView) row.findViewById(R.id.txtvFechaNoticia);

            viewHolder.imgvSeguimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //MobileServiceCustom mobileServiceCustom = new MobileServiceCustom(v.getContext());
                    //Toast.makeText(v.getContext(),mobileServiceCustom.mClient.getCurrentUser().getUserId() , Toast.LENGTH_SHORT).show();
                    /*
                    new AsyncTask<Void, Void, Boolean>() {

                        MobileServiceCustom mobileServiceCustom;
                        MobileServiceTable<NoticiaUsuario> mNoticiaUsuarioaTable;

                        @Override
                        protected void onPreExecute()
                        {
                            mobileServiceCustom = new MobileServiceCustom(mContext);
                            mNoticiaUsuarioaTable = mobileServiceCustom.mClient.getTable("noticiausuario", NoticiaUsuario.class);

                        }

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            try {
                                final MobileServiceList<NoticiaUsuario> result = mNoticiaUsuarioaTable.where().field("idusuario").eq(false).execute().get();
                                activity.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {


                                        String datos = "";
                                        for (Noticia item : result) {

                                            //adapter.add(item);
                                            adapter.notifyDataSetChanged();
                                        }
                                        //Toast.makeText(mContext,datos,Toast.LENGTH_SHORT).show();

                                    }
                                });
                                return true;
                            } catch (Exception exception) {

                            }
                            return false;
                        }

                        @Override
                        protected void onPostExecute(Boolean success)
                        {


                        }

                        @Override
                        protected void onCancelled()
                        {
                            super.onCancelled();
                        }
                    }.execute();
                    */
                    
                    
                }
            });

            row.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();

        viewHolder.txtvNombreUsuarioNoticia.setText(currentItem.getNombreUsuario());

        Glide.with(mContext)
                .load(currentItem.getUrlImagenUsuario().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvFotoPerfilUsuario);

        Glide.with(mContext)
                .load(currentItem.getUrlimagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvImagen);

        // The MAGIC happens here!
        //mAttacher = new PhotoViewAttacher(viewHolder.imgvImagen);


        // Lets attach some listeners, not required though!
        //mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
        //mAttacher.setOnPhotoTapListener(new PhotoTapListener());


        viewHolder.txtvTitulo.setText(currentItem.getNombre());

        viewHolder.txtvDescripcion.setText(currentItem.getDescripcion());

        viewHolder.txtvFecha.setText("Desaparecido(a) el " + currentItem.getFechadesaparicion());

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
        public TextView txtvDescripcion;
        public TextView txtvEstado;
        public TextView txtvFecha;
        public TextView txtvNombreUsuarioNoticia;
        public CircularImageView imgvFotoPerfilUsuario;
        public PhotoView imgvImagen;
        public ImageView imgvEstado;
        public ImageView imgvSeguimiento;
        public ImageView imgvComentario;
    }

}
