package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 03/10/2015.
 */
public class ComentarioCompleto extends Comentario
{
    @SerializedName("nombre")
    private String nombre;

    @SerializedName("urlimagen")
    private String urlImagen;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String pNombre) {
        nombre = pNombre;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
