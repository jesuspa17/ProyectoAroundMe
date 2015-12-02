package com.dam.salesianostriana.maps.aroundme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.salesianostriana.maps.aroundme.notificacionesAround.GcmSendMessageAsyncTask;
import com.dam.salesianostriana.maps.aroundme.pojo_usuarios.Mensaje;
import com.squareup.picasso.Picasso;


public class EnviarMensajeActivity extends AppCompatActivity {

    EditText mensaje;
    TextView usuario;
    ImageView enviar_mensaje;
    ImageView avatar_user;

    String nombre;
    String avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);

        usuario = (TextView) findViewById(R.id.txtNombreEnviar);
        enviar_mensaje = (ImageView) findViewById(R.id.imgEnviarMensaje);
        mensaje = (EditText) findViewById(R.id.editMensaje);
        avatar_user = (ImageView) findViewById(R.id.imageViewAvatarEnviar);

        Bundle extras = getIntent().getExtras();

        if(extras!=null) {
            nombre = extras.getString("usuario");
            avatar = extras.getString("avatar");
        }
        usuario.setText(nombre);
        Picasso.with(this).load(RegistroActivity.URL_AVATAR(avatar)).placeholder(R.drawable.user).into(avatar_user);

        enviar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mensaje.getText().toString();
                if(msg.isEmpty()){
                    Toast.makeText(EnviarMensajeActivity.this, "Debe introducir algún mensaje", Toast.LENGTH_SHORT).show();
                }else {
                    Mensaje m = new Mensaje(nombre, msg);
                    Log.i("OBJETO MENSAJE: ", m.getNick_usuario() + "\n" + m.getMsg());
                    new GcmSendMessageAsyncTask(EnviarMensajeActivity.this).execute(m);
                    mensaje.setText("");
                }

            }
        });
    }
}
