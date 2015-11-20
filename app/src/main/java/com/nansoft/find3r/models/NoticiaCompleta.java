package com.nansoft.find3r.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 11/10/2015.
 */
public class NoticiaCompleta extends Noticia implements Parcelable {

    @SerializedName("nombre_usuario")
    public String nombreUsuario;

    @SerializedName("urlimagen_perfil_usuario")
    public String urlImagenUsuario;

    @SerializedName("nombre_provincia")
    public String nombreProvincia;

    @SerializedName("cantidad_comentarios")
    public int cantidadComentarios;

    @SerializedName("estado_follow")
    public boolean estadoSeguimiento;

    public NoticiaCompleta() {
        estadoSeguimiento = false;
        cantidadComentarios = 0;
        nombreUsuario = "Sin definir";
        urlImagenUsuario = "Sin definir";
        nombreProvincia = "Sin definir";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(super.id);
        dest.writeString(super.nombre);
        dest.writeString(super.descripcion);
        dest.writeString(super.getUrlImagen());
        dest.writeString(super.getFechadesaparicionCompleta());
        dest.writeString(super.idusuario);
        dest.writeString(super.idestado);
        dest.writeString(super.idCategoria);
        dest.writeString(super.idProvincia);
        dest.writeString(super.latitud);
        dest.writeString(super.longitud);
        dest.writeInt(super.cantidadReportes);
        dest.writeString(this.nombreUsuario);
        dest.writeString(this.urlImagenUsuario);
        dest.writeInt(this.cantidadComentarios);
        dest.writeString(this.nombreProvincia);
        dest.writeByte(estadoSeguimiento ? (byte) 1 : (byte) 0);
    }

    protected NoticiaCompleta(Parcel in) {
        super.id = in.readString();
        super.nombre = in.readString();
        super.descripcion = in.readString();
        super.urlimagen = in.readString();
        super.fechadesaparicion = in.readString();
        super.idusuario = in.readString();
        super.idestado = in.readString();
        super.idCategoria = in.readString();
        super.idProvincia = in.readString();
        super.longitud = in.readString();
        super.latitud = in.readString();
        super.cantidadReportes = in.readInt();
        this.nombreUsuario = in.readString();
        this.urlImagenUsuario = in.readString();
        this.cantidadComentarios = in.readInt();
        this.nombreProvincia = in.readString();
        this.estadoSeguimiento = in.readByte() != 0;

    }

    public static final Parcelable.Creator<NoticiaCompleta> CREATOR = new Parcelable.Creator<NoticiaCompleta>() {
        public NoticiaCompleta createFromParcel(Parcel source) {
            return new NoticiaCompleta(source);
        }

        public NoticiaCompleta[] newArray(int size) {
            return new NoticiaCompleta[size];
        }
    };
}
