package com.example.appchat.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Chat")
public class Chat extends ParseObject {

    public ParseUser getEmisor() {
        return getParseUser("emisor");
    }

    public void setEmisor(ParseUser emisor) {
        put("emisor", emisor);
    }

    public ParseUser getReceptor() {
        return getParseUser("receptor");
    }

    public void setReceptor(ParseUser receptor) {
        put("receptor", receptor);
    }

    public String getMensaje() {
        return getString("mensaje");
    }

    public void setMensaje(String mensaje) {
        put("mensaje", mensaje);
    }


    public ParseUser getOtroUsuario(String emisorId) {
        return getEmisor().getObjectId().equals(emisorId) ? getReceptor() : getEmisor();
    }


    public String getOtroUsuarioNombre(String emisorId) {
        ParseUser otroUsuario = getOtroUsuario(emisorId);
        return otroUsuario != null ? otroUsuario.getUsername() : "Usuario desconocido";
    }


    public String getOtroUsuarioFoto(String emisorId) {
        ParseUser otroUsuario = getOtroUsuario(emisorId);
        return otroUsuario != null && otroUsuario.getParseFile("fotoPerfil") != null
                ? otroUsuario.getParseFile("fotoPerfil").getUrl()
                : null;
    }
}

