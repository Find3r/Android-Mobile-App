package com.nansoft.find3r.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.nansoft.find3r.R;

/**
 * Created by Carlos on 20/04/2015.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider
{

    // List of fragments which are going to set in the view pager widget
    List<Fragment> fragments;

    //private int tabIcons[] = {R.drawable.news, R.drawable.category, R.drawable.notification,R.drawable.user};
    private int tabIcons[] = {R.drawable.news_active,R.drawable.lock,R.drawable.notification, R.drawable.user};
    Context mContext;
    /**
     * Constructor
     *
     * @param fm
     *            interface for interacting with Fragment objects inside of an
     *            Activity
     */
    public MyFragmentPagerAdapter(FragmentManager fm ,Context pContexto) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
        mContext = pContexto;
    }

    /**
     * Add a new fragment in the list.
     *
     * @param fragment
     *            a new fragment
     */
    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    /*
    @Override
    public CharSequence getPageTitle(int position)
    {
        return TITLES[position];
    }
    */

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}