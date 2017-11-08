package brdebr.bryan.app_bar.Clases;

/**
 * Created by Bryan on 03/02/2017.
 */

public class Usuario {


    String nombre;
    String password;
    String grupo;

    public Usuario(String nombre, String password, String grupo) {
        this.nombre = nombre;
        this.password = password;
        this.grupo = grupo;
    }

    public Usuario(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
        this.grupo = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
