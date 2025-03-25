package com.example.appchat.model;
import android.util.Log;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

    public User() {
        // Constructor vacío necesario para Parse
    }


    public String getRedSocial() {
        return getString("redSocial");
    }

    public void setRedSocial(String redSocial) {
        if (redSocial != null) {
            put("redSocial", redSocial);
        }
    }


    public String getFotoperfil() {
        return getString("fotoperfil");
    }

    public void setFotoperfil(String fotoperfil) {
        if (fotoperfil != null) {
            put("foto_perfil", fotoperfil);
        }
    }

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    // Getter y setter para "email"
    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        if (email != null) {
            put("email", email);
        } else {
            Log.w("User", "El correo electrónico es nulo.");
        }
    }


    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        put("password", password);
    }

    // Getter para "id" (no necesitas un setter para "id" porque Parse lo genera automáticamente)
    public String getId() {
        return getObjectId();
    }
}
