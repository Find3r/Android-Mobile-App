package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 * Created by User on 6/19/2015.
 */
public class Noticia
{
    @SerializedName("id_noticia")
    private String id;

    @SerializedName("nombre_noticia")
    private String nombre;

    @SerializedName("descripcion_noticia")
    private String descripcion;

    @SerializedName("urlimagen_noticia")
    private String urlimagen;

    @SerializedName("fechadesaparicion_noticia")
    private String fechadesaparicion;

    @SerializedName("idusuario_noticia")
    private String idusuario;

    @SerializedName("idestado_noticia")
    private String idestado;

    @SerializedName("idcategoria_noticia")
    private String idCategoria;

    @SerializedName("idprovincia_noticia")
    private String idProvincia;

    @SerializedName("cantidad_reportes_noticia")
    private int cantidadReportes;

    @SerializedName("latitud_noticia")
    private String latitud;

    @SerializedName("longitud_noticia")
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

    public String getHoraDesaparicion() {

        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String fecha = "Sin definir";
        try {

            //fecha = myFormat.format(fromUser.parse(fechadesaparicion.substring(11,15)));
            fecha = myFormat.format(fromUser.parse(fechadesaparicion));
        } catch (Exception e) {

        }
        return fecha;

    }

    public String getFechadesaparicionCompleta() {

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
