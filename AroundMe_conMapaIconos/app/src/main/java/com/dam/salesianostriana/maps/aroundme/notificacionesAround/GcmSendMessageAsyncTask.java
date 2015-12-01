package com.dam.salesianostriana.maps.aroundme.notificacionesAround;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dam.salesianostriana.maps.aroundme.pojo_usuarios.Mensaje;

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
public class GcmSendMessageAsyncTask extends AsyncTask<Mensaje, Void, String> {

    private Context context;
    JSONArray response = new JSONArray();

    public GcmSendMessageAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Mensaje... params) {

        SharedPreferences prefs = context.getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        String clave = prefs.getString("clave", null);
        Mensaje msg = params[0];

        if (params[0] !=null)
            sendMessageToBackend(clave, msg.getNick_usuario(), msg.getMsg());
            Log.i("ASYNTASK ENVIAR: ", clave + msg.getNick_usuario() + msg.getMsg());

        return msg.getNick_usuario();
    }

    @Override
    protected void onPostExecute(String strings) {
        Toast.makeText(context, "Mensaje enviado a: " + strings, Toast.LENGTH_LONG).show();
    }

    private void sendMessageToBackend(String id, String nick, String mensaje) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        Log.v("CatalogClient", "Entra en sendRegistration");

        try {
            url = new URL("http://rest.miguelcr.com/aroundme/send?regId="+id+"&nickname="+nick+"&mensaje="+mensaje);
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
