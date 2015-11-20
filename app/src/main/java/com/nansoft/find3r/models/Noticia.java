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
    public String id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("urlimagen")
    public String urlimagen;

    @SerializedName("fechadesaparicion")
    public String fechadesaparicion;

    @SerializedName("idusuario")
    public String idusuario;

    @SerializedName("idestado")
    public String idestado;

    @SerializedName("idcategoria")
    public String idCategoria;

    @SerializedName("idprovincia")
    public String idProvincia;

    @SerializedName("cantidad_reportes")
    public int cantidadReportes;

    @SerializedName("latitud")
    public String latitud;

    @SerializedName("longitud")
    public String longitud;

    @SerializedName("solved")
    public boolean solved;

    public Noticia() {
    }

    public String getUrlImagen() {
        if(urlimagen == null)
        {
            urlimagen = "www.bing.com";
        }
        return urlimagen;
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
}
