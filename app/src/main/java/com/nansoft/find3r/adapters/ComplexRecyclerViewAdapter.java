package com.nansoft.find3r.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.ComentarioActivity;
import com.nansoft.find3r.activity.NoticiasCategoriaActivity;
import com.nansoft.find3r.activity.UserProfileActivity;
import com.nansoft.find3r.holder.CategoriaViewHolder;
import com.nansoft.find3r.holder.ViewHolder1;
import com.nansoft.find3r.holder.UserProfileViewHolder;
import com.nansoft.find3r.models.Categoria;
import com.nansoft.find3r.models.NoticiaCompleta;
import com.nansoft.find3r.models.User;
import com.nansoft.find3r.models.Usuario;

import java.util.List;

/**
 * Created by Carlos on 28/10/2015.
 */
public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;

    private final int USER = 0, HEADER_USERPROFILE = 1, NEW = 2, CATEGORY = 3;
    Context context;
    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapter(List<Object> items,Context pcontext) {
        this.items = items;
        context = pcontext;
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof User) {
            return USER;
        } else if (items.get(position) instanceof Usuario) {
            return HEADER_USERPROFILE;
        }
        else if ( items.get(position) instanceof NoticiaCompleta)
        {
            return NEW;
        }
        else if ( items.get(position) instanceof Categoria)
        {
            return CATEGORY;
        }

        return -1;
    }
    /**
     * This method creates different RecyclerView.ViewHolder objects based on the item view type.\
     *
     * @param viewGroup ViewGroup container for the item
     * @param viewType type of view to be inflated
     * @return viewHolder to be inflated
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        switch (viewType) {
            case USER:
                view = inflater.inflate(R.layout.layout_viewholder1, viewGroup, false);
                viewHolder = new ViewHolder1(view);
                break;
            case HEADER_USERPROFILE:
                view = inflater.inflate(R.layout.header_userprofile, viewGroup, false);
                viewHolder = new UserProfileViewHolder(view,context);
                break;

            case NEW:
                view = inflater.inflate(R.layout.new_item, viewGroup, false);
                viewHolder = new NoticiaCompletaAdapter.NoticiaCompletaViewHolder(view);
                break;

            case CATEGORY:
                view = inflater.inflate(R.layout.categoria_item, viewGroup, false);
                viewHolder = new CategoriaViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.layout_viewholder1, viewGroup, false);
                viewHolder = new ViewHolder1(view);
                break;
        }
        return viewHolder;
    }

    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * @param viewHolder The type of RecyclerView.ViewHolder to populate
     * @param position Item position in the viewgroup.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case USER:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;

            case HEADER_USERPROFILE:
                UserProfileViewHolder vh2 = (UserProfileViewHolder) viewHolder;
                configureHeaderProfileViewHolder(vh2, position);
                break;

            case NEW:
                NoticiaCompletaAdapter.NoticiaCompletaViewHolder vh3 = (NoticiaCompletaAdapter.NoticiaCompletaViewHolder) viewHolder;
                configureNewViewHolder(vh3, position);
                break;

            case CATEGORY:
                CategoriaViewHolder vh4 = (CategoriaViewHolder) viewHolder;
                configureCategoryViewHolder(vh4, position);
                break;
        }
    }

    private void configureNewViewHolder(NoticiaCompletaAdapter.NoticiaCompletaViewHolder viewHolder, int position)
    {
        final NoticiaCompleta objNoticia = (NoticiaCompleta) items.get(position);
        if (objNoticia != null) {
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
                    intent.putExtra("idNoticia", objNoticia.getId());
                    context.startActivity(intent);
                    ((Activity) v.getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            });

            viewHolder.txtvNombreUsuarioNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navegarPerfilUsuario(objNoticia.getIdusuario(), v.getContext());
                }
            });
            viewHolder.imgvFotoPerfilUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navegarPerfilUsuario(objNoticia.getIdusuario(), v.getContext());
                }
            });

        }
    }

    private void navegarPerfilUsuario(String pIdUsuario,Context context)
    {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("ID_USUARIO",pIdUsuario);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        User user = (User) items.get(position);
        if (user != null) {
            vh1.getLabel1().setText("Name: " + user.name);
            vh1.getLabel2().setText("Hometown: " + user.hometown);
        }
    }

    private void configureHeaderProfileViewHolder(UserProfileViewHolder viewHolder,int position)
    {
        final Usuario objUsuario = (Usuario) items.get(position);

        viewHolder.txtvUserProfileAddress.setText(objUsuario.getNombre());

        Glide.with(context)
                .load(objUsuario.getCover_picture().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvUserProfileCover);

        Glide.with(context)
                .load(objUsuario.getUrlimagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvUserProfilePhoto);
    }

    private void configureCategoryViewHolder(CategoriaViewHolder viewHolder,int position)
    {
        final Categoria objCategoria = (Categoria) items.get(position);

        viewHolder.layItemCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoticiasCategoriaActivity.class);
                intent.putExtra("idCategoria", objCategoria.getId());
                intent.putExtra("nombreCategoria", objCategoria.getNombre());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        viewHolder.txtvTitulo.setText(objCategoria.getNombre());

        Glide.with(context)
                .load(objCategoria.getUrlimagen().trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(viewHolder.imgvImagen);


    }
}
