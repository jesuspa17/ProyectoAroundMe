package com.dam.salesianostriana.maps.aroundme.pojo_listas;

/**
 * Created by Jesús Pallares on 19/11/2015.
 */
public class ItemMensaje {

    private boolean enviado;
    private String cuerpo;
    private String autor;

    public ItemMensaje(boolean enviado, String cuerpo, String autor) {
        this.enviado = enviado;
        this.cuerpo = cuerpo;
        this.autor = autor;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
