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
import com.dam.salesianostriana.maps.aroundme.pojo_usuarios.Registro;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RegistroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button entrar;
    EditText nom_usuario;
    Spinner spinerAvatares;

    public static String URL_AVATAR(String avatar){
        return "http://rest.miguelcr.com/images/aroundme/"+avatar;
    }

    String imagen_seleccionada;
    ArrayList<String> listado_avatares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        entrar = (Button) findViewById(R.id.btn_entrar);
        nom_usuario = (EditText) findViewById(R.id.editTextNick);
        spinerAvatares = (Spinner) findViewById(R.id.spinnerAvatares);

        new GetAvatarTask().execute();
        //spinerAvatares.setAdapter(new AdapterSpinner(this, R.layout.list_item_spinner, nombres_avatares));

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
                    Registro reg = new Registro(imagen_seleccionada, nick);
                    Log.i("IMAGEN SELECCIONADA", imagen_seleccionada);
                    new GcmRegistrationAsyncTask(RegistroActivity.this).execute(reg);
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
        if (nom_seleccionado.equals(listado_avatares.get(position))) {
            imagen_seleccionada = listado_avatares.get(position);
            Log.i("AVATAR SELEC: ", imagen_seleccionada);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GetAvatarTask extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> lista_avatares = new ArrayList<>();

            try {
                URL url = new URL("http://rest.miguelcr.com/aroundme/avatars");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                String json_avatares = Utils.leer(br);
                Log.i("JSON AVATARES",json_avatares);
                JSONArray array = new JSONArray(json_avatares);

                for(int i = 0; i<array.length();i++){
                    String a = array.getString(i);
                    lista_avatares.add(a);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return lista_avatares;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            listado_avatares = strings;
            spinerAvatares.setAdapter(new AdapterSpinner(RegistroActivity.this, R.layout.list_item_spinner, strings));

        }
    }

    public class AdapterSpinner extends ArrayAdapter {

        public AdapterSpinner(Context context, int textViewResourceId, ArrayList<String> array) {
            super(context, textViewResourceId, array);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.list_item_spinner, parent, false);

            //TextView nom_avatar = (TextView) layout.findViewById(R.id.textViewNomAvatar);
            //nom_avatar
            // .setText(nombres_avatares[position]);
            ImageView img = (ImageView) layout.findViewById(R.id.imageViewAvatar);
            Picasso.with(RegistroActivity.this).load(URL_AVATAR(listado_avatares.get(position))).into(img);

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
