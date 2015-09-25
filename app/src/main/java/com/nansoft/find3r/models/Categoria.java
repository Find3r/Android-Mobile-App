package com.nansoft.find3r.models;

/**
 * Created by Carlos on 16/05/2015.
 */
public class Categoria
{
    private String id;

    private String nombre;

    private String urlimagen;

    public Categoria(String id, String nombre, String urlimagen) {
        this.id = id;
        this.nombre = nombre;
        this.urlimagen = urlimagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }
}
