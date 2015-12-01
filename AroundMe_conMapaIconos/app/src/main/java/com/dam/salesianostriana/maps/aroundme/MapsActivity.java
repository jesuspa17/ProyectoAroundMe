package com.dam.salesianostriana.maps.aroundme;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dam.salesianostriana.maps.aroundme.pojo_usuarios.Usuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient = null;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;
    private LatLng latlong_Actual;

    double latitud;
    double longitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 1. Instancio un objeto de tipo GoogleApiClient
        buildGoogleApiClient();

        // 2. Activar la detección de localización
        createLocationRequest();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                        // Indico que la API que voy a utilizar
                        // dentro de Google Play Services, es la API
                        // del Servicio de Localización
                .addApi(LocationServices.API)
                .build();

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Intervalo de uso normal de la la aplicación
        mLocationRequest.setInterval(10000);
        // Interval de una app que requiera una localización exhaustiva
        mLocationRequest.setFastestInterval(5000);
        // GPS > mejor método de localización / consume más batería
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opciones, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_locate:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    SharedPreferences prefs;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        prefs = getSharedPreferences("preferencias", MODE_PRIVATE);
        new GetNickNameTask().execute(prefs.getString("clave", null));

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("POSICION", "POSICION: onLocationChanged");
        // guardo en la variable mCurrentLocation la
        // localización del usuario

        mCurrentLocation = location;
        latitud = location.getLatitude();
        longitud = location.getLongitude();


        
        // guardo la última vez que se actualizó la posición
        // del usuario en un objeto de tipo String
        // (en nuestro ejemplo no lo estamos utilizando)
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    /*
                        *
                      * * *
                    **********
                   ************
                  **************
                 ****************
                ******************
                       **
                       **
                     ******
                  FELIZ NAVIDAD
    */

    // Este método se encarga de actualizar la Interfaz de Usuario
    // Cada vez que se actualiza la posición del dispositivo.


    private void updateUI() {

        // Transformo el objeto "mCurrentLocation" de tipo Location
        // a un objeto de tipo LatLng
        // lo hago mediante los métodos: mCurrentLocation.getLatitude()
        // y mCurrentLocation.getLongitude()

        LatLng posicion = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        new actualizarLocalizacion().execute(posicion);

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i("POSICION", "POSICION: onConnected");
        // La siguiente condición indica que sólo se inicie actualización
        // de la localización del usuario, si tengo activado la "escucha"
        if (mRequestingLocationUpdates) {
            startLocationUpdates();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
        Log.i("POSICION", "POSICION: stopLocationUpdates");
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        mRequestingLocationUpdates = true;
        Log.i("POSICION", "POSICION: startLocationUpdates");


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private class actualizarLocalizacion extends AsyncTask<LatLng, Void, Void>{

        @Override
        protected Void doInBackground(LatLng... params) {

            latlong_Actual = params[0];
            prefs = getSharedPreferences("preferencias",MODE_PRIVATE);
            String url = "http://rest.miguelcr.com/aroundme/position?regId=" +prefs.getString("clave",null)+"&lat="+params[0].latitude+"&lon="+params[0].longitude;
            InputStream is = null;
            try {
                is = new URL(url).openStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String msg = Utils.leer(rd);
                Log.i("POSICION ACTUALIZADA",msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    private class GetNickNameTask extends AsyncTask<String, Void, ArrayList<Usuario>> {

        @Override
        protected ArrayList<Usuario> doInBackground(String... params) {

            ArrayList<Usuario> result = new ArrayList<>();
            if (params != null)
                try {

                    String url = "http://rest.miguelcr.com/aroundme/users?regId=" + params[0];

                    //se abre conexión
                    InputStream is = new URL(url).openStream();

                    //se lee lo que se recibe de la conexión
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                    //se almacena el texto leido y se convierte de json a array.
                    String jsonText = Utils.leer(rd);
                    JSONArray arr = new JSONArray(jsonText);
                    Log.i("USUARIO CLAVE", "USER: " + arr.length());

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject obj = arr.getJSONObject(i);
                        String name = obj.getString("nickname");
                        String avatar = obj.getString("avatar");
                        String latlon = obj.getString("latlon");

                        result.add(new Usuario(name, avatar, latlon));
                        Log.i("USERS", result.get(i).getNickname());
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Usuario> itemUsuarios) {

            for (int i = 0; i < itemUsuarios.size(); i++) {
                Usuario posicion_seleccionada = itemUsuarios.get(i);
                if (!posicion_seleccionada.getLatlon().equals("0,0")) {
                    String[] array_latlong = posicion_seleccionada.getLatlon().split(",");

                    Log.i("TAM ARRAY", "TAMANYO " + array_latlong.length);

                        Log.i("LATLONG", array_latlong[0]);
                        Log.i("LATLONG", array_latlong[1]);
                        LatLng posicion = new LatLng(Double.parseDouble(array_latlong[0]),Double.parseDouble(array_latlong[1]));
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(posicion)
                                .title(posicion_seleccionada.getNickname()));

                        URL url = null;
                        Bitmap b;
                        BitmapDescriptor bitmap;
                        String imagen_obtenida = posicion_seleccionada.getAvatar();
                        try {
                            url = new URL(RegistroActivity.URL_AVATAR(imagen_obtenida));
                            InputStream is =  url.openStream();
                            b = BitmapFactory.decodeStream(is);
                            bitmap = BitmapDescriptorFactory.fromBitmap(b);
                            marker.setIcon(bitmap);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }


        }
    }
}