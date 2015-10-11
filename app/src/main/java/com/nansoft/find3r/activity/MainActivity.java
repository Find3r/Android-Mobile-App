package com.nansoft.find3r.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.nansoft.find3r.R;
import com.nansoft.find3r.animacion.ZoomOutPageTransformer;
import com.nansoft.find3r.adapters.MyFragmentPagerAdapter;
import com.nansoft.find3r.fragments.CategoriaFragment;
import com.nansoft.find3r.fragments.NoticiaFragment;
import com.nansoft.find3r.fragments.NoticiaSeguimientoFragment;
import com.nansoft.find3r.fragments.NotificacionFragment;
import com.nansoft.find3r.fragments.PerfilFragment;
import com.nansoft.find3r.helpers.CustomNotificationHandler;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.Noticia;
import com.nansoft.find3r.models.Usuario;
import com.nansoft.find3r.models.UsuarioFacebook;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.microsoft.windowsazure.notifications.NotificationsManager;
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */


    private PagerSlidingTabStrip tabs;
    private ViewPager pager = null;
    private MyFragmentPagerAdapter adapter;

    public static MobileServiceCustom customClient;
    public static final String SENDER_ID = "129689044298";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.main_activity);



        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.pager);
        this.pager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Create an adapter with the fragments we show on the ViewPager
        adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),this);

        adapter.addFragment(new NoticiaFragment());
        //adapter.addFragment(new NoticiaSeguimientoFragment());
        //adapter.addFragment(new CategoriaFragment());
        adapter.addFragment(new NotificacionFragment());
        adapter.addFragment(new PerfilFragment());

        this.pager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(this);

        customClient = new MobileServiceCustom(this);

        try{
            // cargamos el token
            customClient.loadUserTokenCache(customClient.mClient);

            NotificationsManager.handleNotifications(this, SENDER_ID, CustomNotificationHandler.class);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"er " + e.toString(),Toast.LENGTH_SHORT).show();

        }

        // cargamos la información de usuario
        cargarUsuario();


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

                try
                {
                    URL img_value = null;
                    img_value = new URL("http://graph.facebook.com/"+customClient.mClient.getCurrentUser().getUserId()+"/picture?type=large");

                    Toast.makeText(MainActivity.this, img_value.toString(), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(MainActivity.this, "error " + e.toString(), Toast.LENGTH_SHORT).show();
                }

                new AsyncTask<Void, Void, Boolean>() {


                    MobileServiceTable<Usuario> mUserTable;
                    MobileServiceTable<Noticia> mNoticiaTable;

                    @Override
                    protected void onPreExecute() {

                        mUserTable = customClient.mClient.getTable("usuario", Usuario.class);
                        mNoticiaTable = customClient.mClient.getTable("noticia", Noticia.class);
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        try {
                            // buscamos por el usuario
                            MobileServiceCustom.USUARIO_LOGUEADO = mUserTable.lookUp(objUsuarioFacebook.id).get();

                            // se verifica si el usuario es null
                            if(MobileServiceCustom.USUARIO_LOGUEADO == null)
                            {
                                // debemos de insertar el registro

                                // establecemos primero los atributos
                                MobileServiceCustom.USUARIO_LOGUEADO.setId(objUsuarioFacebook.id);
                                MobileServiceCustom.USUARIO_LOGUEADO.setNombre(objUsuarioFacebook.name);
                                objUsuarioFacebook.data.PictureURL.PictureURL = "http://graph.facebook.com/"+objUsuarioFacebook.id+"/picture?type=large";
                                MobileServiceCustom.USUARIO_LOGUEADO.setUrlimagen(objUsuarioFacebook.data.PictureURL.PictureURL);

                                // agregamos el registro
                                mUserTable.insert(MobileServiceCustom.USUARIO_LOGUEADO);
                            }

                            // obtenemos la imagen del usuario en caso que la haya cambiado
                            MobileServiceCustom.USUARIO_LOGUEADO.setUrlimagen("http://graph.facebook.com/"+MobileServiceCustom.USUARIO_LOGUEADO.getId()+"/picture?type=large");

                            return true;
                        } catch (final Exception exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "error in async " + exception.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {

                        //mSwipeRefreshLayout.setRefreshing(false);
                        if (!success)
                            Toast.makeText(getApplicationContext(), "Verifique la conexión a internet", Toast.LENGTH_SHORT).show();

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

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }
    */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
       
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_contact:
                //QuickContactFragment dialog = new QuickContactFragment();
                //dialog.show(getSupportFragmentManager(), "QuickContactFragment");
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    //private int tabIcons[] = {R.drawable.news, R.drawable.category, R.drawable.notification,R.drawable.user};
    //private int tabIconsActive[] = {R.drawable.news_active, R.drawable.category_active, R.drawable.notification_active,R.drawable.user_active};
    private int tabIcons[] = {R.drawable.news,R.drawable.notification,R.drawable.user};
    private int tabIconsActive[] = {R.drawable.news_active,R.drawable.notification_active, R.drawable.user_active};

    @Override
    public void onPageSelected(int position) {
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        LinearLayout view = (LinearLayout) tabStrip.getChildAt(0);
        ImageView imageView;
        int idImagen;
        for(int i=0;i<tabIconsActive.length;i++)
        {
            imageView = (ImageView) view.getChildAt(i);
            if(i == position)
            {
                idImagen = tabIconsActive[position];
            }
            else
            {
                idImagen = tabIcons[i];
            }
            imageView.setImageResource(idImagen);
        }



        switch (position)
        {
            case 0:
                NoticiaFragment.adapter.notifyDataSetChanged();
                break;

            case 1:
                break;

            case 2:
                break;

            case 3:
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}