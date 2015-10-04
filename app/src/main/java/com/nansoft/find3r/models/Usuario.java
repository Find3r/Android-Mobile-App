package com.nansoft.find3r.models;

/**
 * Created by User on 6/19/2015.
 */
public class Usuario
{
    private String id;
    private String nombre;
    private String primerapellido;
    private String segundoapellido;
    private String email;
    private String telefonocelular;
    private String telefonocasa;
    private String urlimagen;

    public Usuario(String id, String nombre, String primerApellido, String segundoApellido, String email, String telefonoCelular,String telefonoCasa, String urlImagen) {
        this.id = id;
        this.nombre = nombre;
        this.primerapellido = primerApellido;
        this.segundoapellido = segundoApellido;
        this.email = email;
        this.telefonocelular = telefonoCelular;
        this.telefonocasa = telefonoCasa;
        this.urlimagen = urlImagen;
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

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonocelular() {
        return telefonocelular;
    }

    public void setTelefonocelular(String telefonocelular) {
        this.telefonocelular = telefonocelular;
    }

    public String getTelefonocasa() {
        return telefonocasa;
    }

    public void setTelefonocasa(String telefonocasa) {
        this.telefonocasa = telefonocasa;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }
}