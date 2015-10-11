package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by User on 7/5/2015.
 */
public class Notificacion
{

    private String id;
    private String idnoticia;

    @SerializedName("__createdAt")
    private String __createdAt;

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

    public String get__createdAt() {
        return __createdAt;
    }

    public void set__createdAt(String __createdAt) {
        this.__createdAt = __createdAt;
    }

    public String getFechaCreacion() {

        //return getDiferenciaFecha();
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = "Sin definir";
        try {

            fecha = myFormat.format(fromUser.parse(__createdAt.substring(0,10)));
        } catch (ParseException e) {

        }
        return fecha;
    }

    public String getDiferenciaFecha()
    {
        // Crear 2 instancias de Calendar

        Calendar cal1 = Calendar.getInstance();

        Calendar cal2 = Calendar.getInstance();

        String fechaAux = getFechaCreacion();
        // Establecer las fechas

        // año / mes / día
        cal2.set(Integer.parseInt(fechaAux.substring(6, 10)), Integer.parseInt(fechaAux.substring(3, 5)), Integer.parseInt(fechaAux.substring(0, 2)));

        // conseguir la representacion de la fecha en milisegundos

        long milis1 = cal1.getTimeInMillis();

        long milis2 = cal2.getTimeInMillis();


        // calcular la diferencia en milisengundos

        long diff = milis2 - milis1;


        long diffSeconds = diff / 1000;


        // calcular la diferencia en minutos

        long diffMinutes = diff / (60 * 1000);


        // calcular la diferencia en horas

        long diffHours = diff / (60 * 60 * 1000);

        // calcular la diferencia en dias
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return String.valueOf(diffDays);
    }


    public long getDiferenciaSegundos()
    {
        // Crear 2 instancias de Calendar

        Calendar cal1 = Calendar.getInstance();

        Calendar cal2 = Calendar.getInstance();

        String fechaAux = getFechaCreacion();
        // Establecer las fechas

        // año / mes / día
        cal2.set(Integer.parseInt(fechaAux.substring(6, 10)), Integer.parseInt(fechaAux.substring(3, 5)), Integer.parseInt(fechaAux.substring(0, 2)));

        // conseguir la representacion de la fecha en milisegundos

        long milis1 = cal1.getTimeInMillis();

        long milis2 = cal2.getTimeInMillis();

        // calcular la diferencia en milisengundos
        long diff = milis2 - milis1;

        return diff / 1000;
    }

    public long getDiferenciaMinutos()
    {

        // calcular la diferencia en minutos

        return getDiferenciaSegundos() / (60 * 1000);
    }

    public long getDiferenciaHoras()
    {
        // calcular la diferencia en horas

        return getDiferenciaSegundos() / (60 * 60 * 1000);
    }

    public long getDiferenciaDias()
    {
        // calcular la diferencia en dias
        return getDiferenciaSegundos() / (24 * 60 * 60 * 1000);
    }

    public Calendar dif()
    {
        // Crear 2 instancias de Calendar


        Calendar c = Calendar.getInstance();

        //fecha inicio

        Calendar fechaInicio = Calendar.getInstance();



        //fecha fin

        Calendar fechaFin = new GregorianCalendar();

        String fechaAux = getFechaCreacion();
        // Establecer las fechas

        // año / mes / día
        fechaFin.set(Integer.parseInt(fechaAux.substring(6, 10)), Integer.parseInt(fechaAux.substring(3, 5)), Integer.parseInt(fechaAux.substring(0, 2)));


        //restamos las fechas como se puede ver son de tipo Calendar,

        //debemos obtener el valor long con getTime.getTime.

        c.setTimeInMillis(

                fechaFin.getTime().getTime() - fechaInicio.getTime().getTime());

        //la resta provoca que guardamos este valor en c,

        //los milisegundos corresponde al tiempo en dias

        //asi sabemos cuantos dias

       return c;

    }







}
