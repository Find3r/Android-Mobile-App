package com.nansoft.find3r.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.nansoft.find3r.R;
import com.nansoft.find3r.animacion.ZoomOutPageTransformer;
import com.nansoft.find3r.adapters.MyFragmentPagerAdapter;
import com.nansoft.find3r.fragments.CategoriaFragment;
import com.nansoft.find3r.fragments.NoticiaFragment;
import com.nansoft.find3r.fragments.NoticiaSeguimientoFragment;
import com.nansoft.find3r.fragments.PerfilFragment;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.UsuarioFacebook;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

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
        adapter.addFragment(new NoticiaSeguimientoFragment());
        //adapter.addFragment(new CategoriaFragment());
        //adapter.addFragment(new NotificacionFragment());
        adapter.addFragment(new PerfilFragment());

        this.pager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(this);


    }







    @Override
    public void onBackPressed() {

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }

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
    private int tabIcons[] = {R.drawable.news, R.drawable.lock,R.drawable.user};
    private int tabIconsActive[] = {R.drawable.news_active,R.drawable.lock_active, R.drawable.user_active};

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