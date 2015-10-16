package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 11/10/2015.
 */
public class NoticiaCompleta extends Noticia
{
    @SerializedName("Column8")
    private String nombreUsuario;

    @SerializedName("Column9")
    private String urlImagenUsuario;

    @SerializedName("Column10")
    private int cantidadComentarios;

    @SerializedName("estado_seguimiento")
    private boolean estadoSeguimiento;

    public NoticiaCompleta() {
        estadoSeguimiento = false;
        cantidadComentarios = 0;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getUrlImagenUsuario() {
        return urlImagenUsuario;
    }

    public void setUrlImagenUsuario(String urlImagenUsuario) {
        this.urlImagenUsuario = urlImagenUsuario;
    }

    public boolean isEstadoSeguimiento() {

        return estadoSeguimiento;
    }

    public void setEstadoSeguimiento(boolean estadoSeguimiento) {
        this.estadoSeguimiento = estadoSeguimiento;
    }

    public int getCantidadComentarios() {
        return cantidadComentarios;
    }

    public void setCantidadComentarios(int cantidadComentarios) {
        this.cantidadComentarios = cantidadComentarios;
    }
}
