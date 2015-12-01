package com.dam.salesianostriana.maps.aroundme.pojo_usuarios;

/**
 * @author Jesús Pallares on 30/11/2015.
 */
public class Registro {

    private String avatar;
    private String usuario;

    public Registro(){
    }

    public Registro(String avatar, String usuario) {
        this.avatar = avatar;
        this.usuario = usuario;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "avatar='" + avatar + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
