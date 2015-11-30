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


public class EnviarMensajeActivity extends AppCompatActivity {

    TextView usuario;
    ImageView enviar_mensaje;
    String nombre;
    EditText mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);

        usuario = (TextView) findViewById(R.id.txtNombreEnviar);
        enviar_mensaje = (ImageView) findViewById(R.id.imgEnviarMensaje);
        mensaje = (EditText) findViewById(R.id.editMensaje);

        Bundle extras = getIntent().getExtras();

        if(extras!=null) {
            nombre = extras.getString("usuario");
        }
        usuario.setText(nombre);

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
