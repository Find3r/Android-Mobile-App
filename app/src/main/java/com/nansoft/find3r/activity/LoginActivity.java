package com.nansoft.find3r.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.nansoft.find3r.R;

import java.net.MalformedURLException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ExecutionException;

import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.nansoft.find3r.helpers.MobileServiceCustom;
import com.nansoft.find3r.models.UsuarioFacebook;

public class LoginActivity extends ActionBarActivity {


    MobileServiceCustom customClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        customClient = new MobileServiceCustom(this);

        Button btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Authenticate passing false to load the current token cache if available.
                authenticate();

            }
        });

        // se verifica si el usuario ya está registrado
        if(checkUser())
        {
            // si es así se inicia la acitivuty principal
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

            // se finaliza para que no pueda volver acá
            finish();
        }



    }

    private boolean checkUser()
    {
        // We first try to load a token cache if one exists.
        if (customClient.loadUserTokenCache(customClient.mClient))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }



    private void authenticate() {

        final ProgressDialog progress = ProgressDialog.show(this, "Cargando",
                "Espere un momemento...", true);

        // We first try to load a token cache if one exists.
        if (customClient.loadUserTokenCache(customClient.mClient))
        {
            // si es así se inicia la acitivuty principal
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

            // se finaliza para que no pueda volver acá
            finish();
        }
        // If we failed to load a token cache, login and create a token cache
        else
        {
            // Login using the Google provider.
            ListenableFuture<MobileServiceUser> mLogin = customClient.mClient.login(MobileServiceAuthenticationProvider.Facebook);

            Futures.addCallback(mLogin, new FutureCallback<MobileServiceUser>() {
                @Override
                public void onFailure(Throwable exc) {
                    createAndShowDialog("You must log in. Login Required", "Error");
                }
                @Override
                public void onSuccess(MobileServiceUser user) {

                    customClient.cacheUserToken(customClient.mClient.getCurrentUser());
                    // si es así se inicia la acitivuty principal
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

                    // se finaliza para que no pueda volver acá
                    finish();
                }
            });
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
