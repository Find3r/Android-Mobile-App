package com.nansoft.find3r.models;

/**
 * Created by User on 6/27/2015.
 */
public class Comentario
{
    private String id;
    private String descripcion;
    private String idUsuario;
    private String idNoticia;
    private String fecha;
    private String hora;

    public Comentario(String id, String descripcion, String idUsuario,String fecha, String hora, String idNoticia) {
        this.id = id;
        this.descripcion = descripcion;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.hora = hora;
        this.idNoticia = idNoticia;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(String idNoticia) {
        this.idNoticia = idNoticia;
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
