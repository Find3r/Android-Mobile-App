package com.nansoft.find3r.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.melnykov.fab.FloatingActionButton;
import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.MyFragmentPagerAdapter;
import com.nansoft.find3r.fragments.FollowingNewsFragment;
import com.nansoft.find3r.fragments.FragmentSwipe;
import com.nansoft.find3r.fragments.NewsFragment;
import com.nansoft.find3r.fragments.ProfileFragment;
import com.nansoft.find3r.helpers.CircularImageView;
import com.nansoft.find3r.helpers.MobileServiceCustom;

public class MyProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,ViewPager.OnPageChangeListener
{

    int index = 0;
    int positionActiveTab = 0;
    AppBarLayout appBarLayout;
    MyFragmentPagerAdapter adapterViewPager;
    ViewPager viewPager;
    FloatingActionButton fab;
    static TextView txtvUserName;
    static CircularImageView imgvUserProfile;
    static ImageView imgvCover;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_profile);

        context = getBaseContext();

        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        //region setupToolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        //region viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyFragmentPagerAdapter(getSupportFragmentManager(),this);
        adapterViewPager.addFragment(new ProfileFragment(), "Publicaciones");
        adapterViewPager.addFragment(new FollowingNewsFragment(), "Seguimiento");

        viewPager.setAdapter(adapterViewPager);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        //endregion

        //region setup collapsing Toolbar
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        // se indica que no debe mostrar la propia vista ya que se usará una personalizada
        collapsingToolbar.setTitleEnabled(false);
        //endregion

        //region FloatingActionButton
        fab = (FloatingActionButton) findViewById(R.id.fabAgregarNoticia);
        //fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarNoticia.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        //endregion

        //region header perfil
        View includedLayout = findViewById(R.id.headerMyProfile);
        txtvUserName = (TextView) includedLayout.findViewById(R.id.txtvUserProfileName);
        imgvUserProfile = (CircularImageView) includedLayout.findViewById(R.id.imgvUserProfilePhoto);
        imgvCover = (ImageView) includedLayout.findViewById(R.id.imgvUserProfileCover);



        //endregion

    }

    public static void loadUserInformation() {


        txtvUserName.setText(MobileServiceCustom.USUARIO_LOGUEADO.nombre);

        Glide.with(context)
                .load(MobileServiceCustom.USUARIO_LOGUEADO.urlimagen.trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(imgvUserProfile);

        Glide.with(context)
                .load(MobileServiceCustom.USUARIO_LOGUEADO.cover_picture.trim())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.picture_default)
                .error(R.drawable.error_image)
                .into(imgvCover);

    }


    //region handlers scroll
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i)
    {
        index = i;

        if(index > -40)
        {
            fab.show();
        }
        else
        {
            fab.hide();
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {

        // se obtiene el evento touch
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action)
        {
            // se verifica cuál evento fue el que sucedió
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:

                // se obtiene el fragment que está activo
                FragmentSwipe pageFragment = adapterViewPager.getItem(positionActiveTab);

                // se verifica si no es null el fragment
                if (pageFragment != null)
                {
                    // verificamos si el index
                    boolean statusSwipe = index == 0 ? true : false;

                    // de acuerdo al valor que tengamos activamos o desactivamos el swipe
                    // si está en el tope de la pantalla entonces se habilita
                    pageFragment.setEnabledSwipe(statusSwipe);




                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //endregion

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    //region TabsListeners
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        positionActiveTab = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //endregion

    //region handlers back action
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        //noinspection SimplifiableIfStatement
        switch(item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    //endregion

    private void llamar(String pNumero)
    {
        try {
            Intent Llamada = new Intent(Intent.ACTION_CALL);
            Llamada.setData(Uri.parse("tel:" + pNumero));
            startActivity(Llamada);
        } catch (SecurityException activityException)
        {

        }
        catch (ActivityNotFoundException activityException) {

        }
    }

    public void AbrirCorreo(Context pContexto)
    {
        String [] correoDestino = {MobileServiceCustom.USUARIO_LOGUEADO.email};
        try {
            Intent Correo = new Intent(Intent.ACTION_SEND);
            Correo.setData(Uri.parse("mailto:"));
            Correo.putExtra(Intent.EXTRA_EMAIL,correoDestino);
            Correo.putExtra(Intent.EXTRA_CC, "");
            Correo.putExtra(Intent.EXTRA_SUBJECT, "");
            Correo.putExtra(Intent.EXTRA_TEXT, "");
            Correo.setType("message/rfc822");
            startActivity(Intent.createChooser(Correo, "Email "));
        } catch (ActivityNotFoundException activityException) {

            Toast.makeText(pContexto, "Error verifique que tenga una aplicación de correo", Toast.LENGTH_SHORT).show();

        }
    }

}

