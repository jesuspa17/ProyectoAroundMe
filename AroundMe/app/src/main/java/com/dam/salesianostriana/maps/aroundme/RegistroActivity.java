package com.dam.salesianostriana.maps.aroundme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.salesianostriana.maps.aroundme.notificacionesAround.GcmRegistrationAsyncTask;
import com.squareup.picasso.Picasso;


public class RegistroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button entrar;
    EditText nom_usuario;
    Spinner spinerAvatares;

    String[] nombres_avatares = {
            "Agente Coulson"
            , "Black Widow"
            , "Capitán América"
            , "El hombre gigante"
            , "Ojo de Halcón"
            , "Hulk"
            , "Iron Man"
            , "Loki"
            , "Nick Fury"
            , "Thor"
            , "War Machine"};

    String[] imagenes = {
            "http://rest.miguelcr.com/images/aroundme/1.png"
            , "http://rest.miguelcr.com/images/aroundme/2.png"
            , "http://rest.miguelcr.com/images/aroundme/3.png"
            , "http://rest.miguelcr.com/images/aroundme/4.png"
            , "http://rest.miguelcr.com/images/aroundme/5.png"
            , "http://rest.miguelcr.com/images/aroundme/6.png"
            , "http://rest.miguelcr.com/images/aroundme/7.png"
            , "http://rest.miguelcr.com/images/aroundme/8.png"
            , "http://rest.miguelcr.com/images/aroundme/9.png"
            , "http://rest.miguelcr.com/images/aroundme/10.png"
            , "http://rest.miguelcr.com/images/aroundme/11.png"};

    String imagen_seleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        entrar = (Button) findViewById(R.id.btn_entrar);
        nom_usuario = (EditText) findViewById(R.id.editTextNick);
        spinerAvatares = (Spinner) findViewById(R.id.spinnerAvatares);

        spinerAvatares.setAdapter(new AdapterSpinner(this, R.layout.list_item_spinner, nombres_avatares));

        SharedPreferences prefs = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getString("clave", null) != null) {
            Intent i = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(i);
            RegistroActivity.this.finish();
        }


        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = nom_usuario.getText().toString();
                if (!nick.isEmpty()) {
                    new GcmRegistrationAsyncTask(RegistroActivity.this).execute(nick);
                    RegistroActivity.this.finish();
                    editor.putString("avatar",imagen_seleccionada);
                    editor.commit();
                } else {
                    Toast.makeText(RegistroActivity.this, "Introduzca algún nick", Toast.LENGTH_LONG).show();
                }
            }
        });

        spinerAvatares.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String nom_seleccionado = (String) parent.getItemAtPosition(position);
        if (nom_seleccionado.equals(nombres_avatares[position])) {
            imagen_seleccionada = imagenes[position];
            Log.i("AVATAR SELEC: ", imagen_seleccionada);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class AdapterSpinner extends ArrayAdapter {

        public AdapterSpinner(Context context, int textViewResourceId, String[] array) {
            super(context, textViewResourceId, array);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.list_item_spinner, parent, false);

            TextView tvLanguage = (TextView) layout.findViewById(R.id.textViewNomAvatar);
            tvLanguage.setText(nombres_avatares[position]);
            ImageView img = (ImageView) layout.findViewById(R.id.imageViewAvatar);
            Picasso.with(RegistroActivity.this).load(imagenes[position]).into(img);

            return layout;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }


}
