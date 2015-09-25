package com.nansoft.find3r;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 7/5/2015.
 */
public class MyTime
{
    public static String getHora()
    {
        return getDateTime("HH:mm:ss");
    }

    public static String getFecha()
    {
        return getDateTime("dd/MM/yyyy");
    }

    private static String getDateTime(String pFormato)
    {
        Date fecha = new Date();

        DateFormat dateTimeFormat = new SimpleDateFormat(pFormato);

        return dateTimeFormat.format(fecha);
    }
}
