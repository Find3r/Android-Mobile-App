package com.nansoft.find3r.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.fragments.NewsFragment;
import com.nansoft.find3r.helpers.CustomNotificationHandler;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Usuario;
import com.nansoft.find3r.models.UsuarioFacebook;

import java.util.ArrayList;
import java.util.List;
import com.microsoft.windowsazure.notifications.NotificationsManager;




public class MainActivity extends AppCompatActivity
{


    public static MobileServiceCustom customClient;

    // número de proyecto
    public static final String SENDER_ID = "129689044298";

    private GoogleCloudMessaging mGcm;
    private String mRegistrationId;
    private NotificationHub mHub;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.activity_main);

        ActionBar ab =getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // se muestra el layout personalizado
        ab.setCustomView(R.layout.custom_actionbar);

        findViewById(R.id.imgvNewsHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFragment.mRecyclerView.scrollToPosition(0);
            }
        });

        customClient = new MobileServiceCustom(this);

        try{
            // cargamos el token
            customClient.loadUserTokenCache(customClient.mClient);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"er " + e.toString(),Toast.LENGTH_SHORT).show();

        }


        mGcm = GoogleCloudMessaging.getInstance(this);
        String connectionString = "Endpoint=sb://wantedapphub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=AogK50usAqncP1e+G3M7YU94gWKS7IO9emd8E2Nkkic=";
        mHub = new NotificationHub("wantedapphub", connectionString, this);
        NotificationsManager.handleNotifications(this, SENDER_ID, CustomNotificationHandler.class);



        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Replace the contents of the container with the new fragment
        ft.replace(R.id.your_placeholder, new NewsFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());

        // Add this transaction to the back stack
        // Append this transaction to the backstack
        //ft.addToBackStack("optional tag");

        // Complete the changes added above
        ft.commit();

        // cargamos la información de usuario
        cargarUsuario();


    }

    @SuppressWarnings("unchecked")
    private void registerWithGcm() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    mRegistrationId = mGcm.register(SENDER_ID);

                    /// unregister
                    //mHub.register(mRegistrationId,MobileServiceCustom.USUARIO_LOGUEADO.getId(),"All");



                    mHub.registerTemplate(mRegistrationId, "notificationTemplate", "{\"data\":{\"id\":\"$(id)\", \"message\":\"$(message)\"}}",
                            MobileServiceCustom.USUARIO_LOGUEADO.id,"All");



                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error al registrarse " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return null;
            }

            protected void onPostExecute(Object result) {

            }
        }.execute(null, null, null);
    }

    public void cargarUsuario()
    {

        List<Pair<String, String> > lp = new ArrayList<Pair<String, String> >();
        lp.add(new Pair("id", customClient.mClient.getCurrentUser().getUserId()));
        ListenableFuture<UsuarioFacebook> result = customClient.mClient.invokeApi("userlogin", "GET", null, UsuarioFacebook.class);

        Futures.addCallback(result, new FutureCallback<UsuarioFacebook>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al intentar conectar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(final UsuarioFacebook objUsuarioFacebook) {


                new AsyncTask<Void, Void, Boolean>() {


                    MobileServiceTable<Usuario> mUserTable;

                    @Override
                    protected void onPreExecute() {

                        mUserTable = customClient.mClient.getTable("usuario", Usuario.class);

                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        try {



                            // buscamos por el usuario
                            MobileServiceCustom.USUARIO_LOGUEADO = mUserTable.lookUp(objUsuarioFacebook.id).get();



                            return true;
                        } catch (final Exception exception) {
                            /*
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "error in async " + exception.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            */

                            // se verifica si el usuario es null

                            try {
                                // debemos de insertar el registro

                                // establecemos primero los atributos
                                MobileServiceCustom.USUARIO_LOGUEADO.id = objUsuarioFacebook.id;
                                MobileServiceCustom.USUARIO_LOGUEADO.nombre = objUsuarioFacebook.name;
                                objUsuarioFacebook.data.PictureURL.PictureURL = "http://graph.facebook.com/" + objUsuarioFacebook.id + "/picture?type=large";
                                MobileServiceCustom.USUARIO_LOGUEADO.urlimagen = objUsuarioFacebook.data.PictureURL.PictureURL;

                                // verificamos si la imagen no está nula
                                if(!objUsuarioFacebook.cover.PictureURL.isEmpty())
                                {
                                    MobileServiceCustom.USUARIO_LOGUEADO.cover_picture = objUsuarioFacebook.cover.PictureURL;
                                }
                                else
                                {

                                    MobileServiceCustom.USUARIO_LOGUEADO.cover_picture = "https://wanted.blob.core.windows.net/img/Hakuna_Matata opacidad.jpg";
                                }

                                // agregamos el registro
                                mUserTable.insert(MobileServiceCustom.USUARIO_LOGUEADO);
                            } catch (final Exception exception2) {
                                /*
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "error al registrar el usuario " + exception2.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                */
                            }


                        } finally {
                            // obtenemos la imagen del usuario en caso que la haya cambiado
                            MobileServiceCustom.USUARIO_LOGUEADO.urlimagen = "http://graph.facebook.com/" + MobileServiceCustom.USUARIO_LOGUEADO.id + "/picture?type=large";
                            //MobileServiceCustom.USUARIO_LOGUEADO.setCover_picture(objUsuarioFacebook.cover.PictureURL);

                            // verificamos si la imagen no está nula
                            if(!objUsuarioFacebook.cover.PictureURL.isEmpty())
                            {
                                MobileServiceCustom.USUARIO_LOGUEADO.cover_picture = objUsuarioFacebook.cover.PictureURL;
                            }
                            else
                            {

                                MobileServiceCustom.USUARIO_LOGUEADO.cover_picture = "https://wanted.blob.core.windows.net/img/Hakuna_Matata opacidad.jpg";
                            }

                            try {
                                mUserTable.update(MobileServiceCustom.USUARIO_LOGUEADO).get();


                            } catch (final Exception exception3) {
                                /*
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "error al actualizar el usuario " + exception3.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                */
                            }

                        }



                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {

                        registerWithGcm();
                    }

                    @Override
                    protected void onCancelled() {
                        super.onCancelled();
                    }
                }.execute();

            }
        });




    }
    /*
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else {
            super.onBackPressed();

        }
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.news_menu, menu);

        return super.onCreateOptionsMenu(menu);
       
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {



            case android.R.id.home:
                NewsFragment.mRecyclerView.scrollToPosition(0);
                return true;

            case R.id.action_notifications:
                startMyActivity(new Intent(this, NotificationsActivity.class));
                return true;

            case R.id.action_categories:
                startMyActivity(new Intent(this,CategoriesActivity.class));
                return true;

            case R.id.action_user:
                startMyActivity(new Intent(this,MyProfileActivity.class));
                return true;



        }


        return super.onOptionsItemSelected(item);
    }

    private void startMyActivity(Intent pIntent)
    {
        startActivity(pIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

    }





    }