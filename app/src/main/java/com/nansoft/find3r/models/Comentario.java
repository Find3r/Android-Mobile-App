package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 6/27/2015.
 */
public class Comentario
{
    @SerializedName("id")
    public String id;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("idusuario")
    public String idUsuario;

    @SerializedName("idnoticia")
    public String idNoticia;

    @SerializedName("fecha")
    public String fecha;

    @SerializedName("hora")
    public String hora;

    public Comentario() {
        this.id = "Sin definir";
        this.descripcion = "Sin definir";
        this.idUsuario = "Sin definir";
        this.fecha = "Sin definir";
        this.hora= "Sin definir";
        this.idNoticia = "Sin definir";
    }

    public Comentario(String id, String descripcion, String idUsuario,String fecha, String hora, String idNoticia) {
        this.id = id;
        this.descripcion = descripcion;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.hora = hora;
        this.idNoticia = idNoticia;
    }

}
