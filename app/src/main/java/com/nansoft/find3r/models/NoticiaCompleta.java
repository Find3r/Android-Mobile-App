package com.nansoft.find3r.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos on 11/10/2015.
 */
public class NoticiaCompleta extends Noticia implements Parcelable {

    @SerializedName("Column13")
    private String nombreUsuario;

    @SerializedName("Column14")
    private String urlImagenUsuario;

    @SerializedName("Column15")
    private int cantidadComentarios;

    @SerializedName("estado_seguimiento")
    private boolean estadoSeguimiento;

    public NoticiaCompleta() {
        estadoSeguimiento = false;
        cantidadComentarios = 0;
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

    public boolean isEstadoSeguimiento() {

        return estadoSeguimiento;
    }

    public void setEstadoSeguimiento(boolean estadoSeguimiento) {
        this.estadoSeguimiento = estadoSeguimiento;
    }

    public int getCantidadComentarios() {
        return cantidadComentarios;
    }

    public void setCantidadComentarios(int cantidadComentarios) {
        this.cantidadComentarios = cantidadComentarios;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(super.getId());
        dest.writeString(super.getNombre());
        dest.writeString(super.getDescripcion());
        dest.writeString(super.getUrlImagen());
        dest.writeString(super.getFechadesaparicion());
        dest.writeString(super.getIdusuario());
        dest.writeString(super.getIdestado());
        dest.writeString(super.getIdCategoria());
        dest.writeString(super.getIdProvincia());
        dest.writeString(this.nombreUsuario);
        dest.writeString(this.urlImagenUsuario);
        dest.writeInt(this.cantidadComentarios);
        dest.writeByte(estadoSeguimiento ? (byte) 1 : (byte) 0);
    }

    protected NoticiaCompleta(Parcel in) {
        super.setId(in.readString());
        super.setNombre(in.readString());
        super.setDescripcion(in.readString());
        super.setUrlImagen(in.readString());
        super.setFechadesaparicion(in.readString());
        super.setIdusuario(in.readString());
        super.setIdestado(in.readString());
        super.setIdCategoria(in.readString());
        super.setIdProvincia(in.readString());
        this.nombreUsuario = in.readString();
        this.urlImagenUsuario = in.readString();
        this.cantidadComentarios = in.readInt();
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
