package com.nansoft.find3r.activity;

import android.os.Bundle;
import android.view.Menu;

import com.nansoft.find3r.R;
import com.nansoft.find3r.fragments.UserProfileFragment;

public class UserProfileActivity extends CustomAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        loadFragment(new UserProfileFragment());
    }


}