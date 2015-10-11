package com.nansoft.find3r.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Carlos on 11/10/2015.
 */
public class CustomDate
{
    public static String getNewDate()
    {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
}
