package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 10/11/2015.
 */
public class Report
{
    @SerializedName("id")
    private String id;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("idnoticia")
    private String idNoticia;

    @SerializedName("idusuario")
    private String idUsuario;

    public Report(String descripcion, String idNoticia, String idUsuario) {
        this.descripcion = descripcion;
        this.idNoticia = idNoticia;
        this.idUsuario = idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(String idNoticia) {
        this.idNoticia = idNoticia;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
