package com.dam.salesianostriana.maps.aroundme.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dam.salesianostriana.maps.aroundme.R;
import com.dam.salesianostriana.maps.aroundme.Utils;
import com.dam.salesianostriana.maps.aroundme.adaptadores.DividerItemDecoration;
import com.dam.salesianostriana.maps.aroundme.adaptadores.MensajeAdapter;
import com.dam.salesianostriana.maps.aroundme.pojo_listas.ItemMensaje;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Jesús Pallares
 *
 * Fragment en el cual se muestran los usuarios registados actualmente.
 */
public class MensajesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MensajesFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mensajes, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_mensajes);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


        new GetMensajesTask().execute();

        //Esta linea hará que se sobreescriba el menú de por defecto.
        setHasOptionsMenu(true);

        return v;
    }

    /**
     * Asyntask que realiza la conexión a internet de la cual obtenemos los mensajes que el usuario tiene actualmente.
     */
    public class GetMensajesTask extends AsyncTask<Void,Void,ArrayList<ItemMensaje>> {

        @Override
        protected ArrayList<ItemMensaje> doInBackground(Void... params) {

            ArrayList<ItemMensaje> result = null;
            if(params!=null)
                try {

                    SharedPreferences prefs = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                    String url = "http://rest.miguelcr.com/aroundme/mensajes?regId="+prefs.getString("clave",null);

                    //se abre conexión
                    InputStream is = new URL(url).openStream();

                    //se lee lo que se recibe de la conexión
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                    //se almacena el texto leido y se convierte de json a array.
                    String jsonText = Utils.leer(rd);
                    JSONArray arr = new JSONArray(jsonText);


                    result = new ArrayList<>();

                    for (int i=0; i<arr.length(); i++){

                        JSONObject obj = arr.getJSONObject(i);
                        String msg = obj.getString("mensaje");
                        String from = obj.getString("nickname");
                        result.add(new ItemMensaje(false,msg,from));
                        Log.i("MENSAJES_RECIBIDOS",result.get(i).getCuerpo());
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }


            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemMensaje> itemMensajes) {
            mAdapter = new MensajeAdapter(itemMensajes);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mensajes, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            new GetMensajesTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}