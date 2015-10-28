package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by User on 7/5/2015.
 */
public class NotificacionUsuario
{
    @SerializedName("id")
    private String id;

    @SerializedName("idusuario")
    private String idusuario;

    @SerializedName("idnoticia")
    private String idnoticia;

    @SerializedName("__createdAt")
    private String __createdAt;

    @SerializedName("idnotificacion")
    private String idnotificacion;

    @SerializedName("estadoleido")
    private boolean estadoleido;

    @SerializedName("descripcion")
    private String descripcion;

    public NotificacionUsuario(String id,String idusuario, String idnotificacion, boolean estadoleido,String descripcion) {
        this.id = id;
        this.idusuario = idusuario;
        this.idnotificacion = idnotificacion;
        this.estadoleido = estadoleido;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getIdnotificacion() {
        return idnotificacion;
    }

    public void setIdnotificacion(String idnotificacion) {
        this.idnotificacion = idnotificacion;
    }

    public boolean isEstadoleido() {
        return estadoleido;
    }

    public void setEstadoleido(boolean estadoleido) {
        this.estadoleido = estadoleido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdnoticia() {
        return idnoticia;
    }

    public void setIdnoticia(String idnoticia) {
        this.idnoticia = idnoticia;
    }

    public String getFechaCreacion() {



        //return getDiferenciaFecha();
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = "Sin definir";
        try {

            fecha = myFormat.format(fromUser.parse(__createdAt.substring(0,10)));
        } catch (ParseException e) {

        }
        return fecha;

    }
}
