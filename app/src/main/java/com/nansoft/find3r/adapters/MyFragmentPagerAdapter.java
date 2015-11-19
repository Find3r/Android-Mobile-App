package com.nansoft.find3r.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.nansoft.find3r.R;
import com.nansoft.find3r.fragments.FragmentSwipe;

/**
 * Created by Carlos on 20/04/2015.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter
{

    List<FragmentSwipe> fragments;
    List<String> titles;

    Context mContext;

    public MyFragmentPagerAdapter(FragmentManager fm ,Context pContexto)
    {
        super(fm);
        fragments = new ArrayList<FragmentSwipe>();
        titles = new ArrayList<String>();
        mContext = pContexto;
    }

    /**
     *
     * @param pFragment fragment a agregar
     * @param pTitle título del fragment a agregar
     */
    public void addFragment(FragmentSwipe pFragment,String pTitle)
    {
        fragments.add(pFragment);
        titles.add(pTitle);
    }

    /**
     *
     * @param position entero que indica la posición
     * @return fragment en la posición recibida
     */
    @Override
    public FragmentSwipe getItem(int position)
    {
        return fragments.get(position);
    }

    /**
     *
     * @return cantidad de fragments
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     *
     * @param position posición del título a consultar
     * @return título en la posición indicada
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}