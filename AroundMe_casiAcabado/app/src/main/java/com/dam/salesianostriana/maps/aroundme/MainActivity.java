package com.dam.salesianostriana.maps.aroundme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.salesianostriana.maps.aroundme.fragments.MensajesFragment;
import com.dam.salesianostriana.maps.aroundme.fragments.UsuariosFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout contenedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contenedor = (FrameLayout) findViewById(R.id.contenedor);

        SharedPreferences prefs = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        String clave = prefs.getString("clave", null);
        String avatar = prefs.getString("avatar",null);
        Log.i("AVATAR GUARDADO",avatar);

        Log.i("CLAVE", "CLAVE: " + clave);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View cabeceraMenuLateral = navigationView.getHeaderView(0);
        TextView nombreUsuario = (TextView)cabeceraMenuLateral.findViewById(R.id.textViewNombreUsuario);
        ImageView imagenUsuari = (ImageView) cabeceraMenuLateral.findViewById(R.id.imageViewNavegacion);

        nombreUsuario.setText(prefs.getString("usuario",null));
        Picasso.with(this).load(RegistroActivity.URL_AVATAR(avatar)).resize(100, 100).into(imagenUsuari);

        Fragment f = new UsuariosFragment();
        transicionPagina(f);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment f = null;
        Intent i = null;

        if (id == R.id.nav_usuarios) {
            f = new UsuariosFragment();
        } else if (id == R.id.nav_mensajes) {
            f = new MensajesFragment();
        } else if (id == R.id.nav_cerrar) {
            i = new Intent(this,RegistroActivity.class);

            SharedPreferences prefs = getSharedPreferences("preferencias",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            startActivity(i);
            MainActivity.this.finish();
        }else if(id == R.id.nav_maps){
            i = new Intent(this,MapsActivity.class);
            startActivity(i);
        }

        if(f!=null) {
            transicionPagina(f);
        }

        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void transicionPagina(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor,f).commit();
    }
}
