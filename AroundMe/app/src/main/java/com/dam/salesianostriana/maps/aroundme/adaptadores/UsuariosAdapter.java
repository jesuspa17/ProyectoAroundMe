package com.dam.salesianostriana.maps.aroundme.adaptadores;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dam.salesianostriana.maps.aroundme.EnviarMensajeActivity;
import com.dam.salesianostriana.maps.aroundme.R;
import com.dam.salesianostriana.maps.aroundme.pojo_listas.ItemUsuario;

import java.util.ArrayList;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> {

    private ArrayList<ItemUsuario> lista_usuarios;
    Context contexto;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public TextView enviar;


        public ViewHolder(View v) {
            super(v);
            nombre  = (TextView) v.findViewById(R.id.txtNombreUsuario);
            enviar = (TextView) v.findViewById(R.id.img_enviar);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public UsuariosAdapter(ArrayList<ItemUsuario> myDataset) {
        lista_usuarios = myDataset;
    }

    @Override
    public UsuariosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_usuarios, viewGroup, false);

        contexto = v.getContext();

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UsuariosAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombre.setText(lista_usuarios.get(position).getNombre());
        holder.enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(contexto, EnviarMensajeActivity.class);
                String nombre = lista_usuarios.get(position).getNombre();
                i.putExtra("usuario", nombre);
                (contexto).startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount()  {
        return lista_usuarios.size();
    }
}
