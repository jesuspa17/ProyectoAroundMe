package com.dam.salesianostriana.maps.aroundme.pojo_usuarios;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Jesús Pallares on 30/11/2015.
 */
public class Usuario {

    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("latlon")
    @Expose
    private String latlon;

    /**
     * No args constructor for use in serialization
     */
    public Usuario() {
    }

    /**
     * @param latlon
     * @param nickname
     * @param avatar
     */
    public Usuario(String nickname, String avatar, String latlon) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.latlon = latlon;
    }

    /**
     * @return The nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname The nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * @return The latlon
     */
    public String getLatlon() {
        return latlon;
    }

    /**
     * @param latlon The latlon
     */
    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }

}
