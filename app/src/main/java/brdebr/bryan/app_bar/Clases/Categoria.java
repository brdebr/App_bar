package brdebr.bryan.app_bar.Clases;

/**
 * Created by Bryan on 19/02/2017.
 */

public class Categoria {


    String nombre;
    String imagen;
    //String abreviatura;
    String color;
    String color_letra;

    public Categoria(String nombre, String imagen, String abreviatura, String color, String color_letra) {
        this.nombre = nombre;
        this.imagen = imagen;
        //this.abreviatura = abreviatura;
        this.color = color;
        this.color_letra = color_letra;
    }

    public Categoria() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /*public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }*/

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor_letra() {
        return color_letra;
    }

    public void setColor_letra(String color_letra) {
        this.color_letra = color_letra;
    }
}
