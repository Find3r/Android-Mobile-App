package com.nansoft.find3r.models;

/**
 * Created by User on 7/5/2015.
 */
public class NotificacionUsuario
{
    private String id;
    private String idusuario;
    private String idnotificacion;
    private boolean estadoleido;
    private String descripcion;
    private String fecha;
    private String hora;

    public NotificacionUsuario(String id,String idusuario, String idnotificacion, boolean estadoleido,String descripcion, String fecha, String hora) {
        this.id = id;
        this.idusuario = idusuario;
        this.idnotificacion = idnotificacion;
        this.estadoleido = estadoleido;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
