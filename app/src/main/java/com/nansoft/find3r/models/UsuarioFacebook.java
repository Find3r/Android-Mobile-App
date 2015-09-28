package com.nansoft.find3r.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 24/09/2015.
 */
public class UsuarioFacebook
{
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("gender")
    public String gender;

    @SerializedName("link")
    public String link;

    @SerializedName("picture")
    public Data data;

    @SerializedName("cover")
    public CoverPhoto cover ;

    public class Data
    {
        @SerializedName("data")
        public Data2 PictureURL;

    }

    public class Data2
    {
        @SerializedName("url")
        public String PictureURL;


    }

    public class CoverPhoto
    {
        @SerializedName("id")
        public String Id ;

        @SerializedName("offset_x")
        public float Offset_X ;

        @SerializedName("offset_y")
        public float Offset_Y ;

        @SerializedName("source")
        public String PictureURL;

    }
}
