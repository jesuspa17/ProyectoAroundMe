package com.dam.salesianostriana.maps.aroundme.adaptadores;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.salesianostriana.maps.aroundme.EnviarMensajeActivity;
import com.dam.salesianostriana.maps.aroundme.R;
import com.dam.salesianostriana.maps.aroundme.RegistroActivity;
import com.dam.salesianostriana.maps.aroundme.pojo_usuarios.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * @author Jesús Pallares.
 * Adaptador del RecyclerView que mostrará los usuarios registrados.
 */
public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> {

    private ArrayList<Usuario> lista_usuarios;
    Context contexto;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public TextView enviar;
        public TextView latlong;
        public ImageView img;


        public ViewHolder(View v) {
            super(v);
            nombre  = (TextView) v.findViewById(R.id.txtNombreUsuario);
            enviar = (TextView) v.findViewById(R.id.img_enviar);
            img = (ImageView) v.findViewById(R.id.imageViewAvatarUsuario);
            latlong = (TextView) v.findViewById(R.id.txtLatLong);
        }
    }
    public UsuariosAdapter(ArrayList<Usuario> myDataset) {
        lista_usuarios = myDataset;
    }

    @Override
    public UsuariosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item_usuarios, viewGroup, false);

        contexto = v.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UsuariosAdapter.ViewHolder holder, final int position) {

        final Usuario elemento_actual = lista_usuarios.get(position);

        holder.nombre.setText(elemento_actual.getNickname());

        //Al pulsar sobre algún contacto, abrirá el activity que nos permitirá enviar un mensaje.
        holder.enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(contexto, EnviarMensajeActivity.class);
                String nombre = elemento_actual.getNickname();
                String avatar = elemento_actual.getAvatar();
                i.putExtra("usuario", nombre);
                i.putExtra("avatar", avatar);
                (contexto).startActivity(i);
            }
        });
        Picasso.with(contexto).load(RegistroActivity.URL_AVATAR(elemento_actual.getAvatar())).placeholder(R.drawable.user).into(holder.img);
        holder.latlong.setText(elemento_actual.getLatlon());
    }

    @Override
    public int getItemCount()  {
        return lista_usuarios.size();
    }
}
