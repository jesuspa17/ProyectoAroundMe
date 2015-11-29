package com.dam.salesianostriana.maps.aroundme;

/**
 * Created by Jesús Pallares on 20/11/2015.
 */
public class Mensaje {

    private String nick_usuario;
    private String msg;

    public Mensaje(String nick_usuario, String msg) {
        this.nick_usuario = nick_usuario;
        this.msg = msg;
    }

    public String getNick_usuario() {
        return nick_usuario;
    }

    public void setNick_usuario(String nick_usuario) {
        this.nick_usuario = nick_usuario;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
