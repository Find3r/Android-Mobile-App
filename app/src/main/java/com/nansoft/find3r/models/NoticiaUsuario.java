package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 20/11/2015.
 */
public class NoticiaUsuario
{
    @SerializedName("id")
    public String id;

    @SerializedName("idusuario")
    public String idUsuario;

    @SerializedName("idnoticia")
    public String idNoticia;

    @SerializedName("estado_seguimiento")
    public boolean estadoSeguimiento;

    public NoticiaUsuario(String idUsuario, String idNoticia, boolean estadoSeguimiento) {
        this.idUsuario = idUsuario;
        this.idNoticia = idNoticia;
        this.estadoSeguimiento = estadoSeguimiento;
    }
}
