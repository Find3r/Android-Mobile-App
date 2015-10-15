package com.nansoft.find3r.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.nansoft.find3r.R;
import com.nansoft.find3r.fragments.NotificationsFragment;

public class NotificationsActivity extends CustomAppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        loadFragment(new NotificationsFragment());

    }

}
