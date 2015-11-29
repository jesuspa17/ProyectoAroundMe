package com.dam.salesianostriana.maps.aroundme.adaptadores;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.salesianostriana.maps.aroundme.R;
import com.dam.salesianostriana.maps.aroundme.pojo_listas.ItemMensaje;

import java.util.ArrayList;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {

    private ArrayList<ItemMensaje> lista_mensajes;
    Context contexto;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cuerpo;
        public ImageView img_msg;
        public TextView autor;


        public ViewHolder(View v) {
            super(v);
            cuerpo  = (TextView) v.findViewById(R.id.textViewMensaje);
            img_msg = (ImageView) v.findViewById(R.id.img_mensaje);
            autor  = (TextView) v.findViewById(R.id.textViewAutor);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MensajeAdapter(ArrayList<ItemMensaje> myDataset) {
        lista_mensajes = myDataset;
    }

    @Override
    public MensajeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_mensajes, viewGroup, false);

        contexto = v.getContext();

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MensajeAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cuerpo.setText(lista_mensajes.get(position).getCuerpo());
        holder.autor.setText("@"+lista_mensajes.get(position).getAutor());

        if(lista_mensajes.get(position).isEnviado()){
            holder.img_msg.setImageResource(R.drawable.ic_send);
        }else{
            holder.img_msg.setImageResource(R.drawable.ic_recibido);
        }

    }

    @Override
    public int getItemCount()  {
        return lista_mensajes.size();
    }
}
