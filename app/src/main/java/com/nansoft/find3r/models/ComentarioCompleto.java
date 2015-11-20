package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 03/10/2015.
 */
public class ComentarioCompleto extends Comentario
{
    @SerializedName("nombre")
    public String nombre;

    @SerializedName("urlimagen")
    public String urlImagen;

}
