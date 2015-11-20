package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 6/19/2015.
 */
public class Usuario
{
    public String id;
    public String nombre;
    public String primerapellido;
    public String segundoapellido;
    public String email;
    public String telefonocelular;
    public String telefonocasa;
    public String urlimagen;
    public String cover_picture;

    @SerializedName("idpais")
    private String idPais;

    public Usuario() {

        this.id = "0";
        this.nombre = "Sin definir";
        this.primerapellido = "Sin definir";
        this.segundoapellido = "Sin definir";
        this.email = "Sin definir";
        this.telefonocelular = "Sin definir";
        this.telefonocasa = "Sin definir";
        this.urlimagen = "Sin definir";
        this.cover_picture = "Sin definir";
        idPais = "0";
    }

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


}
