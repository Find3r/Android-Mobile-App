package com.nansoft.find3r.fragments;

/**
 * Created by Carlos on 01/11/2015.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.AgregarNoticia;
import com.nansoft.find3r.activity.NewDescriptionActivity;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.NoticiaCompleta;
// ...

public class ItemOptionsDialog extends DialogFragment {

    LinearLayout layReportar;
    LinearLayout layEditar;

    public ItemOptionsDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ItemOptionsDialog newInstance(String title) {
        ItemOptionsDialog frag = new ItemOptionsDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_item_options, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileServiceCustom mobileServiceCustom = new MobileServiceCustom(getActivity());


        layReportar = (LinearLayout) view.findViewById(R.id.layReportarContenido);
        layEditar = (LinearLayout) view.findViewById(R.id.layEditarContenido);

        // obtenemos los parámetros que se nos enviaron
        Bundle mArgs = getArguments();
        final NoticiaCompleta objNoticia = (NoticiaCompleta) mArgs.getParcelable("objNoticia");

        // verificamos si se debe mostrar el layout para editar una noticia
        // solo se debe mostrar cuando yo sea el dueño de la noticia

        if (objNoticia.getIdusuario().equalsIgnoreCase(mobileServiceCustom.getUserId()))
        {
            layEditar.setVisibility(View.VISIBLE);
        }
        else
        {
            layEditar.setVisibility(View.GONE);
        }

        // on click listeners de editar y reportar noticias
        layEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AgregarNoticia.class);
                //intent.putExtra("URL_IMAGEN",lstNoticias.get(position).getUrlImagen());
                intent.putExtra("obj",objNoticia);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });


        Window window = getDialog().getWindow();

        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //p.x = 200;

        window.setAttributes(p);


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        window.setTitle("Opciones");

        //window.requestFeature(Window.FEATURE_NO_TITLE);


    }

}