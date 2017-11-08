package brdebr.bryan.app_bar.Clases;

import java.io.Serializable;

/**
 * Created by Bryan on 29/01/2017.
 */

public class Plato_seleccionable implements Serializable {

    /**
     * INT, Es la clave principal que se autoincrementa
     */
    private String idPlato;
    /**
     * VARCHAR(50), nombre del plato, unico
     */
    private String nombre;
    /**
     * VARCHAR(100), el nombre del archivo EJ: "cosa.png"
     */
    private String imagen;
    /**
     * DECIMAL(5,2), el precio del plato
     */
    private String precio;

    private int cantidad;
    /**
     * INT, la id de categoria
     */
    private String categoria;

    /**
     * VARCHAR(11), el color codificado de la categoria EJ: "#00c853"
     */
    private String color;

    public Plato_seleccionable(String idPlato, String nombre, String imagen, String precio, int cantidad, String categoria, String color) {
        this.idPlato = idPlato;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.color = color;
    }

    public Plato_seleccionable(String idPlato, int cantidad) {
        this.idPlato = idPlato;
        this.cantidad = cantidad;
    }

    /*public boolean compararPlatos(Plato otro) {
        return this.idPlato.compareTo(otro.idPlato) == 0 &&
                this.nombre.compareTo(otro.nombre) == 0 &&
                this.descripcion.compareTo(otro.descripcion) == 0 &&
                this.precio.compareTo(otro.precio) == 0 &&
                this.categoria.compareTo(otro.categoria) == 0;
    }*/

    public Plato_seleccionable getPlato() {
        return this;
    }

    public String getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(String idPlato) {
        this.idPlato = idPlato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Plato_seleccionable convertir_a_enviable() {
        Plato_seleccionable aux = new Plato_seleccionable(this.getIdPlato(), this.getCantidad());
        return aux;
    }
}
