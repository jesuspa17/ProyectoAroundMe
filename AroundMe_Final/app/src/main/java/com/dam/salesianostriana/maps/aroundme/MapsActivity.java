package com.dam.salesianostriana.maps.aroundme;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dam.salesianostriana.maps.aroundme.pojo_usuarios.Usuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient = null;
    private Location mCurrentLocation;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;
    SharedPreferences prefs;
    private Marker mi_marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 1. Instancio un objeto de tipo GoogleApiClient
        buildGoogleApiClient();

        // 2. Activar la detección de localización
        createLocationRequest();
        Context context = mapFragment.getContext();

        //3. Inicializo las preferencias para obtener datos como la KEY y el AVATAR
        prefs = getSharedPreferences("preferencias", MODE_PRIVATE);

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mi_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        LatLng lat = new LatLng(37.389092, -5.984459);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat, 10));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("POSICION", "POSICION: onLocationChanged");
        mCurrentLocation = location;
        String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();

    }

    private void updateUI() {
        LatLng posicion_actual = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        String clave = prefs.getString("clave", null);

        if (posicion_actual != null) {
            new GetNickNameTask().execute(clave);
            new GetActualPositionTask().execute(posicion_actual);
            if (mi_marker != null) {
                mi_marker.setTitle("YO");
                mi_marker.setIcon(Utils.obtenerBitmapDescriptor(prefs.getString("avatar", null)));
                mi_marker.setPosition(posicion_actual);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("POSICION", "POSICION: onConnected");
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

    private class GetActualPositionTask extends AsyncTask<LatLng, Void, Void> {

        @Override
        protected Void doInBackground(LatLng... params) {

            if (params[0] != null) {

                String url = "http://rest.miguelcr.com/aroundme/position?regId=" + prefs.getString("clave", null) + "&lat=" + params[0].latitude + "&lon=" + params[0].longitude;
                InputStream is;
                try {
                    is = new URL(url).openStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String msg = Utils.leer(rd);

                    Log.i("POSICION ACTUALIZADA", msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private class GetNickNameTask extends AsyncTask<String, Void, ArrayList<Usuario>> {
        @Override
        protected ArrayList<Usuario> doInBackground(String... params) {

            ArrayList<Usuario> result = new ArrayList<>();
            if (params != null)
                try {

                    String url = "http://rest.miguelcr.com/aroundme/users?regId=" + params[0];

                    InputStream is = new URL(url).openStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                    String jsonText = Utils.leer(rd);
                    JSONArray arr = new JSONArray(jsonText);

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
                String[] array_latlong = posicion_seleccionada.getLatlon().split(",");
                if (!array_latlong[0].equals("0") && !array_latlong[1].equals("0")) {

                    Log.i("LATLONG", array_latlong[0]);
                    Log.i("LATLONG", array_latlong[1]);
                    LatLng posicion = new LatLng(Double.parseDouble(array_latlong[0]), Double.parseDouble(array_latlong[1]));

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(posicion)
                            .title(posicion_seleccionada.getNickname()));
                    String imagen_obtenida = posicion_seleccionada.getAvatar();

                    BitmapDescriptor b = Utils.obtenerBitmapDescriptor(imagen_obtenida);

                    if(b != null){
                        marker.setIcon(Utils.obtenerBitmapDescriptor(imagen_obtenida));
                    }


                }
            }
        }
    }
}