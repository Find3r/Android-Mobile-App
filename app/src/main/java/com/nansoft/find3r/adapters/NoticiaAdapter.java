package com.nansoft.find3r.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.helpers.CircularImageView;

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
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.imgvFotoPerfilUsuario = (CircularImageView) row.findViewById(R.id.imgvPerfilUsuarioNoticia);

            viewHolder.txtvNombreUsuarioNoticia = (TextView) row.findViewById(R.id.txtvNombreUsuarioNoticia);

            viewHolder.imgvImagen = (PhotoView) row.findViewById(R.id.imgvNoticia);

            viewHolder.txtvTitulo = (TextView) row.findViewById(R.id.txtvNombreNoticia);

            viewHolder.txtvDescripcion = (TextView) row.findViewById(R.id.txtvDescripcionNoticia);

            viewHolder.imgvEstado = (ImageView) row.findViewById(R.id.imgvEstado);

            viewHolder.imgvComentario = (ImageView) row.findViewById(R.id.imgvComentario);

            //viewHolder.imgvSeguimiento = (ImageView) row.findViewById(R.id.imgvSeguimiento);

            viewHolder.txtvEstado = (TextView) row.findViewById(R.id.txtvEstado);

            viewHolder.txtvFecha = (TextView) row.findViewById(R.id.txtvFechaNoticia);

            viewHolder.layImagenComentario = (LinearLayout) row.findViewById(R.id.layImagenComentario);

            viewHolder.layImagenComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ComentarioActivity.class);
                    intent.putExtra("idNoticia",currentItem.getId());
                    mContext.startActivity(intent);
                }
            });
        /*
            viewHolder.imgvSeguimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    new AsyncTask<Void, Void, Boolean>() {

                        MobileServiceCustom mobileServiceCustom;
                        MobileServiceTable<NoticiaUsuario> mNoticiaUsuarioaTable;
                        String mensaje = "";
                        String nombreImagen = "unlock";
                        @Override
                        protected void onPreExecute()
                        {
                            mobileServiceCustom = new MobileServiceCustom(mContext);
                            mNoticiaUsuarioaTable = mobileServiceCustom.mClient.getTable("noticia_usuario", NoticiaUsuario.class);

                        }

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            try {

                                // buscamos el registro
                                final MobileServiceList<NoticiaUsuario> result = mNoticiaUsuarioaTable.where().field("idusuario").eq(MobileServiceCustom.USUARIO_LOGUEADO.getId()).and().field("idnoticia").eq(currentItem.getId()).execute().get();



                                // se verifica que hayan elementos
                                if (result.getTotalCount() != 0)
                                {
                                    NoticiaUsuario objNoticiaUsuario = result.get(0);

                                    // se cambia el estado
                                    objNoticiaUsuario.setEstado(false);

                                    // se actualiza el registro
                                    mNoticiaUsuarioaTable.update(objNoticiaUsuario);

                                    nombreImagen = "unlock";
                                    mensaje = "Ya no sigues la noticia";

                                }
                                else
                                {
                                    // si no existe se crea el objeto
                                    NoticiaUsuario objNoticiaUsuario = new NoticiaUsuario();
                                    objNoticiaUsuario.setIdNoticia(currentItem.getId());
                                    objNoticiaUsuario.setIdUsuario(MobileServiceCustom.USUARIO_LOGUEADO.getId());
                                    objNoticiaUsuario.setEstado(true);

                                    // se agrega el registro
                                    mNoticiaUsuarioaTable.insert(objNoticiaUsuario);

                                    // se cambia la imagen
                                    nombreImagen = "lock";

                                    mensaje = "Ahora sigues la noticia";
                                }




                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        // se cambia la imagen
                                        viewHolder.imgvSeguimiento.setImageResource(mContext.getResources().getIdentifier(nombreImagen, "drawable", mContext.getPackageName()));
                                        NoticiaFragment.adapter.notifyDataSetChanged();

                                        Toast.makeText(mContext, mensaje, Toast.LENGTH_SHORT).show();
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

                    
                    
                }
            });
            */

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
        public LinearLayout layImagenComentario;
        public ImageView imgvEstado;
        public ImageView imgvSeguimiento;
        public ImageView imgvComentario;
    }

}
