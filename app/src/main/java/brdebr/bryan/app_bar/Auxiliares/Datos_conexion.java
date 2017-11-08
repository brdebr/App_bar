package brdebr.bryan.app_bar.Auxiliares;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Bryan on 09/02/2017.
 */

public class Datos_conexion {

    /**
     * La dirección ip del servidor
     */
    public static String direccionIP;
    /**
     * El puerto en caso de que haga falta
     */
    public static String puerto;
    /**
     * El numero que identifica el grupo al que pertenece el usuario
     */
    public static int grupo;
    /**
     * Nombre del usuario conectado
     */
    public static String nombre;
    // TODO revisar si esto hace falta (contraseña)
    public static String contraseña;

    /**
     * Constante que guarda la direccion de la carpeta de imagenes
     * Se lllena al conectarse
     */
    public static String URL_IMAGENES;

    public static String getDireccionIP() {
        return direccionIP;
    }

    public static void setDireccionIP(String direccionIP) {
        Datos_conexion.direccionIP = direccionIP;
        URL_IMAGENES = "http://" + Datos_conexion.getDireccionIP() + "/app_bar/imgs/";
    }

    public static final int GRUPO_CAMARERO=1;
    public static final int GRUPO_COCINA=2;
    public static final int GRUPO_CAJA=3;

    public static String getPuerto() {
        return puerto;
    }

    public static void setPuerto(String puerto) {
        Datos_conexion.puerto = puerto;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Datos_conexion.nombre = nombre;
    }

    public static String getContraseña() {
        return contraseña;
    }

    public static void setContraseña(String contraseña) {
        Datos_conexion.contraseña = contraseña;
    }

    public static int getGrupo() {
        return grupo;
    }

    public static void setGrupo(int grupo) {
        Datos_conexion.grupo = grupo;
    }

    public static void login(String nombre, String direccionIP,int grupo){
        Datos_conexion.setNombre(nombre);
        Datos_conexion.setDireccionIP(direccionIP);
        Datos_conexion.setGrupo(grupo);
    }

    public static void logout(){
        Datos_conexion.setNombre(null);
        Datos_conexion.setDireccionIP(null);
        Datos_conexion.setGrupo(-1);
    }

    /**
     * Metodo para parsear el nombre de la imagen
     * @param nombreIMG
     * @return
     */
    public static String parsearURLimagen(String nombreIMG){
        if (nombreIMG!=null && !nombreIMG.isEmpty()){
            try {
                //String baseUrl = "http://"+ Datos_conexion.getDireccionIP()+"/app_bar/imgs/";
                //String imageName = nombreIMG;
                URL imageUrl = new URL(Datos_conexion.URL_IMAGENES+ URLEncoder.encode(nombreIMG ,"UTF-8"));
                return imageUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "error.png";
    }


}
