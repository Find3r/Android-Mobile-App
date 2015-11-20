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
    public String id;

    @SerializedName("idusuario")
    public String idusuario;

    @SerializedName("idnoticia")
    public String idnoticia;

    @SerializedName("__createdAt")
    public String __createdAt;

    @SerializedName("idnotificacion")
    public String idnotificacion;

    @SerializedName("estadoleido")
    public boolean estadoleido;

    @SerializedName("descripcion")
    public String descripcion;

    public NotificacionUsuario(String id,String idusuario, String idnotificacion, boolean estadoleido,String descripcion) {
        this.id = id;
        this.idusuario = idusuario;
        this.idnotificacion = idnotificacion;
        this.estadoleido = estadoleido;
        this.descripcion = descripcion;
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
