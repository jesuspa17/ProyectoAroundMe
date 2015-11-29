package com.dam.salesianostriana.maps.aroundme.pojo_listas;

/**
 * @author Jesús Pallares on 29/11/2015.
 */
public class ItemSpinner {

    private String imagen;
    private String nombre_avatar;

    public ItemSpinner(){}


    public ItemSpinner(String imagen, String nombre_avatar) {
        this.imagen = imagen;
        this.nombre_avatar = nombre_avatar;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre_avatar() {
        return nombre_avatar;
    }

    public void setNombre_avatar(String nombre_avatar) {
        this.nombre_avatar = nombre_avatar;
    }
}
