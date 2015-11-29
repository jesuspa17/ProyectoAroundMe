package com.dam.salesianostriana.maps.aroundme.notificacionesAround;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.dam.salesianostriana.maps.aroundme.MainActivity;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jesús Pallares on 19/11/2015.
 */
public class GcmRegistrationAsyncTask extends AsyncTask<String, Void, String> {
    private GoogleCloudMessaging gcm;
    private Context context;
    private String regId;
    JSONArray response = new JSONArray();
    String nickName;

    //ID del servidor en el que nos vamos a registrar
    private static final String SENDER_ID = "93663396119";

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String msg = "";
        nickName = params[0];
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regId = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regId;

            sendRegistrationIdToBackend();


        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {

        //Abrimos las preferencias y guardamos el regId
        SharedPreferences prefs = context.getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("clave", regId);
        editor.putString("usuario",nickName);

        editor.apply();

        //Iniciamos el activity main
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("clave",regId);
        context.startActivity(i);

        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
    }

    private void sendRegistrationIdToBackend() {
        URL url = null;
        HttpURLConnection urlConnection = null;
        Log.v("CatalogClient", "Entra en sendRegistration");

        try {
            url = new URL("http://miguelcr.hol.es/talkme/register?regId=" + regId+"&nickname="+nickName);
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
