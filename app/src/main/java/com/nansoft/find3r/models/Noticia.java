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

    @SerializedName("idprovincia")
    private String idProvincia;

    @SerializedName("cantidad_reportes")
    private int cantidadReportes;

    @SerializedName("latitud")
    private String latitud;

    @SerializedName("longitud")
    private String longitud;


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

    public String getUrlImagen() {
        if(urlimagen == null)
        {
            urlimagen = "www.bing.com";
        }
        return urlimagen;
    }

    public void setUrlImagen(String urlimagen) {
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

    public String getFechadesaparicion(int a) {




        return fechadesaparicion;

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

    public String getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }

    public int getCantidadReportes() {
        return cantidadReportes;
    }

    public void setCantidadReportes(int cantidadReportes) {
        this.cantidadReportes = cantidadReportes;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
