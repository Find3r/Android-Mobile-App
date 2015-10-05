package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 * Created by User on 6/19/2015.
 */
public class Noticia
{
    @SerializedName("id")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("urlimagen")
    private String urlimagen;

    @SerializedName("fechadesaparicion")
    private String fechadesaparicion;

    @SerializedName("idusuario")
    private String idusuario;

    @SerializedName("idestado")
    private String idestado;

    @SerializedName("idcategoria")
    private String idCategoria;

    @SerializedName("Column8")
    private String nombreUsuario;

    @SerializedName("Column9")
    private String urlImagenUsuario;

    public Noticia() {
    }

    public Noticia(String id, String descripcion, String urlimagen, String fechadesaparicion, String hora, String idprovincia, String idcanton, String iddistrito, String idusuario, String idcategoria,String idEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.urlimagen = urlimagen;
        this.fechadesaparicion = fechadesaparicion;

        this.idusuario = idusuario;
        this.idestado = idEstado;
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

    public String getUrlimagen() {
        if(urlimagen == null)
        {
            urlimagen = "www.bing.com";
        }
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }

    public String getFechadesaparicion() {



            //return getDiferenciaFecha();
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = "Sin definir";
            try {

                fecha = myFormat.format(fromUser.parse(fechadesaparicion.substring(0,10)));
            } catch (ParseException e) {

            }
            return fecha;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechadesaparicion(String fechadesaparicion) {
        this.fechadesaparicion = fechadesaparicion;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getIdcategoria() {
        return idCategoria;
    }

    public void setIdcategoria(String idcategoria) {
        this.idCategoria = idcategoria;
    }

    public String getIdestado() {
        return idestado;
    }

    public void setIdestado(String idestado) {
        this.idestado = idestado;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
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
}
