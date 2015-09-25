package com.nansoft.find3r.models;

/**
 * Created by User on 7/5/2015.
 */
public class Notificacion
{
    private String id;
    private String idnoticia;
    private String descripcion;
    private String fecha;
    private String hora;

    public Notificacion(String id, String idnoticia, String descripcion, String fecha, String hora) {
        this.id = id;
        this.idnoticia = idnoticia;
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

    public String getIdnoticia() {
        return idnoticia;
    }

    public void setIdnoticia(String idnoticia) {
        this.idnoticia = idnoticia;
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
