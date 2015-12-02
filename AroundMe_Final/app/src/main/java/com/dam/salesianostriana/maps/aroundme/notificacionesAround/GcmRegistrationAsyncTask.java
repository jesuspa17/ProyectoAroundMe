package com.dam.salesianostriana.maps.aroundme.notificacionesAround;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.dam.salesianostriana.maps.aroundme.MainActivity;
import com.dam.salesianostriana.maps.aroundme.pojo_usuarios.Registro;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jesús Pallares on 19/11/2015.
 */
public class GcmRegistrationAsyncTask extends AsyncTask<Registro, Void, Registro> {
    private GoogleCloudMessaging gcm;
    private Context context;
    private String regId;
    JSONArray response = new JSONArray();
    String nickName;
    String avatar;

    //ID del servidor en el que nos vamos a registrar
    private static final String SENDER_ID = "93663396119";

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Registro doInBackground(Registro... params) {

        Registro registro = params[0];

        Log.i("USUARIO_REGISTRADO", registro.getUsuario());
        Log.i("USUARIO_REGISTRADO", registro.getAvatar());


        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regId = gcm.register(SENDER_ID);

            sendRegistrationIdToBackend(registro.getAvatar(),registro.getUsuario());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return registro;
    }

    @Override
    protected void onPostExecute(Registro registro) {

        //Abrimos las preferencias y guardamos el regId
        SharedPreferences prefs = context.getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("clave", regId);
        editor.putString("usuario",registro.getUsuario());
        editor.putString("avatar", registro.getAvatar());
        editor.apply();

        //Iniciamos el activity main
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("clave", regId);
        context.startActivity(i);

    }

    private void sendRegistrationIdToBackend(String avatar, String nickName) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        Log.v("CatalogClient", "Entra en sendRegistration");

        try {

            //http://rest.miguelcr.com/aroundme/register?regId=XXXX&avatar=XXXX&nickname=XXXX

            url = new URL("http://rest.miguelcr.com/aroundme/register?regId=" + regId+"&avatar="+avatar+"&nickname="+nickName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            // Si queremos comprobar si el código HTTP = 200 (OK)
            //if(responseCode == HttpStatusCodes.STATUS_CODE_OK){

            String responseString = readStream(urlConnection.getInputStream());
            Log.v("CatalogClient", responseString);
            try {
                response = new JSONArray(responseString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.v("CatalogClient", "Error conexión");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
