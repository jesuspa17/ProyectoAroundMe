package com.dam.salesianostriana.maps.aroundme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.dam.salesianostriana.maps.aroundme.notificacionesAround.GcmRegistrationAsyncTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RegistroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button entrar;
    EditText nom_usuario;
    Spinner spinerAvatares;

    public static String URL_AVATAR(String icono){
        return "http://rest.miguelcr.com/images/aroundme/"+icono;
    }

    /*String[] nombres_avatares = {
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
            , "War Machine"};*/

    /*String[] imagenes = {
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
            , "http://rest.miguelcr.com/images/aroundme/11.png"};*/

     /* String[] imagenes = {
            "1.png"
            , "2.png"
            , "3.png"
            , "4.png"
            , "5.png"
            , "6.png"
            , "7.png"
            , "8.png"
            , "9.png"
            , "10.png"
            , "11.png"};*/

    String imagen_seleccionada = "";

    ArrayList<String> avatares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        entrar = (Button) findViewById(R.id.btn_entrar);
        nom_usuario = (EditText) findViewById(R.id.editTextNick);
        spinerAvatares = (Spinner) findViewById(R.id.spinnerAvatares);

        new GetAvatarTask().execute();

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

    //ASYNTASK QUE OBTIENE LOS AVATARES DE INTERNET

    private class GetAvatarTask extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> result = new ArrayList<>();

            try {
                String url = "http://rest.miguelcr.com/aroundme/avatars";

                InputStream is = new URL(url).openStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                String json_avatares = Utils.leer(rd);
                JSONArray arr = new JSONArray(json_avatares);
                Log.i("AVATARES","USER: " + arr.length());

                for (int i=0; i<arr.length(); i++){
                    String obj = arr.getString(i);
                    result.add(obj);
                    Log.i("AVATARES",result.get(i));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            avatares = strings;
            spinerAvatares.setAdapter(new AdapterSpinner(RegistroActivity.this, R.layout.list_item_spinner, strings));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String avatar_seleccionado = (String) parent.getItemAtPosition(position);
        if (avatar_seleccionado.equals(avatares.get(position))) {
            imagen_seleccionada = avatares.get(position);
            Log.i("AVATAR SELEC: ", imagen_seleccionada);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class AdapterSpinner extends ArrayAdapter {

        public AdapterSpinner(Context context, int textViewResourceId, ArrayList<String> array) {
            super(context, textViewResourceId, array);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.list_item_spinner, parent, false);

           /* TextView name = (TextView) layout.findViewById(R.id.textViewNomAvatar);
            name.setText(nombres_avatares[position]);*/
            ImageView img = (ImageView) layout.findViewById(R.id.imageViewAvatar);
            Picasso.with(RegistroActivity.this).load(RegistroActivity.URL_AVATAR(avatares.get(position))).resize(100, 100).into(img);

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
