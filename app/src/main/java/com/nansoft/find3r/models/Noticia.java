package com.nansoft.find3r.models;

import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 * Created by User on 6/19/2015.
 */
public class Noticia
{
    private String id;
    private String nombre;
    private String descripcion;
    private String urlimagen;
    private String fechadesaparicion;
    private String hora;
    private String idprovincia;
    private String idcanton;
    private String iddistrito;
    private String idusuario;
    private String idcategoria;
    private String idestado;
    private boolean eliminado;

    public Noticia() {
    }

    public Noticia(String id, String descripcion, String urlimagen, String fechadesaparicion, String hora, String idprovincia, String idcanton, String iddistrito, String idusuario, String idcategoria,String idEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.urlimagen = urlimagen;
        this.fechadesaparicion = fechadesaparicion;
        this.hora = hora;
        this.idprovincia = idprovincia;
        this.idcanton = idcanton;
        this.iddistrito = iddistrito;
        this.idusuario = idusuario;
        this.idcategoria = idcategoria;
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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(String idprovincia) {
        this.idprovincia = idprovincia;
    }

    public String getIdcanton() {
        return idcanton;
    }

    public void setIdcanton(String idcanton) {
        this.idcanton = idcanton;
    }

    public String getIddistrito() {
        return iddistrito;
    }

    public void setIddistrito(String iddistrito) {
        this.iddistrito = iddistrito;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(String idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getIdestado() {
        return idestado;
    }

    public void setIdestado(String idestado) {
        this.idestado = idestado;
    }
}
