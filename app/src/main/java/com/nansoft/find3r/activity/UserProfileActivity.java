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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }
}
