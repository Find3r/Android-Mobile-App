package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 10/11/2015.
 */
public class Report
{
    @SerializedName("id")
    public String id;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("idnoticia")
    public String idNoticia;

    @SerializedName("idusuario")
    public String idUsuario;

    public Report(String descripcion, String idNoticia, String idUsuario) {
        this.descripcion = descripcion;
        this.idNoticia = idNoticia;
        this.idUsuario = idUsuario;
    }

}
