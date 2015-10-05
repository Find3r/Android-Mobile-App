package com.nansoft.find3r.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nansoft.find3r.R;
import com.nansoft.find3r.adapters.NoticiaAdapter;


public class PerfilUsuarioActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_layout);

        ListView lstvProyectosUsuario = (ListView) findViewById(R.id.lstvPublicacionesUsuario);
        NoticiaAdapter adapter = new NoticiaAdapter(this,R.layout.noticia_item);

        lstvProyectosUsuario.setAdapter(adapter);

        for (int i= 0; i <15; i++){
            //adapter.add(new com.nansoft.find3r.models.Noticia("a","nombre " + i," ","","","","","","","",""));


        }

        lstvProyectosUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                Intent intent = new Intent(view.getContext(), ProyectoActivity.class);

                startActivity(intent);
                */
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_usuario, menu);
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
