package com.nansoft.find3r.fragments;

/**
 * Created by Carlos on 01/11/2015.
 */
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.MyTime;
import com.nansoft.find3r.R;
import com.nansoft.find3r.activity.AgregarNoticia;
import com.nansoft.find3r.activity.NewDescriptionActivity;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Comentario;
import com.nansoft.find3r.models.NoticiaCompleta;
import com.nansoft.find3r.models.Report;
// ...

public class ItemOptionsDialog extends DialogFragment {

    LinearLayout layReportar;
    LinearLayout layEditar;
    MobileServiceCustom mobileServiceCustom ;

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

        mobileServiceCustom = new MobileServiceCustom(getActivity());


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

        // on click listener de reportar
        layReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /* Alert Dialog Code Start*/
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(MobileServiceCustom.USUARIO_LOGUEADO.getNombre()); //Set Alert dialog title here
                alert.setMessage("Coméntenos el problema de este contenido"); //Message here

                // Set an EditText view to get user input
                final EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("Reportar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        String srt = input.getEditableText().toString();

                        if (srt.isEmpty()) {
                            Snackbar.make(layReportar, "Debe ingresar datos en el comentario", Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!
                        } else {

                            agregarReporte(new Report(srt,objNoticia.getId(),MobileServiceCustom.USUARIO_LOGUEADO.getId()));
                            //agregarComentario(new Comentario("", srt, MobileServiceCustom.USUARIO_LOGUEADO.getId(), MyTime.getFecha(), MyTime.getHora(), ID_NOTICIA));
                        }

                    } // End of onClick(DialogInterface dialog, int whichButton)
                }); //End of alert.setPositiveButton
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                /* Alert Dialog Code End*/
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

    public void agregarReporte(final Report objComentario)
    {

        new AsyncTask<Void, Void, Boolean>()
        {

            MobileServiceClient mClient;
            MobileServiceTable<Report> mComentarioTable;
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {


                mComentarioTable = mobileServiceCustom.mClient.getTable("report",Report.class);

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Agregando reporte...");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mComentarioTable.insert(objComentario).get();



                    return true;
                } catch (Exception exception) {

                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean success) {

                progressDialog.hide();
                String mensajeSnack = "";
                if(success)
                {
                    mensajeSnack = "Reporte agregado";
                }
                else
                {
                    mensajeSnack = "Ha ocurrido un error al intentar agregar el reporte, intenta de nuevo";
                }

                Snackbar.make(layReportar, mensajeSnack, Snackbar.LENGTH_SHORT).show(); // Don’t forget to show!



            }
        }.execute();

    }

}