package com.nansoft.find3r.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nansoft.find3r.R;

/**
 * Created by Carlos on 14/10/2015.
 */
public class CustomAppCompatActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void loadFragment(Fragment pFragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Replace the contents of the container with the new fragment
        ft.replace(R.id.your_placeholder,pFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());

        // Add this transaction to the back stack
        // Append this transaction to the backstack
        // ft.addToBackStack("notifications");

        // Complete the changes added above
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
}
