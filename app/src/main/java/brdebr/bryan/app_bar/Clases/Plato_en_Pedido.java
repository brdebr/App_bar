package brdebr.bryan.app_bar.Clases;

/**
 * Created by Bryan on 10/03/2017.
 */

public class Plato_en_Pedido {

    private String id_plat_ped;
    private String id_plato;
    private String nombre_plato;
    private String id_pedido;
    private float cantidad;
    private int estado;
    private float precio;
    private float precio_plato;
    private int is_beb_o_prep;

    public static final int ESTADO_INICIAL=0;
    public static final int ESTADO_LISTO_RECOGER=1;
    public static final int ESTADO_RECOGIDO=2;

    public static final int GRUPOS_PLATO=0;
    public static final int GRUPOS_BEBIDA=1;
    public static final int GRUPOS_PREPARADO=2;




    public Plato_en_Pedido(String idplat_ped, String id_plato, String nombre_plato, String id_pedido, float cantidad, int estado, float precio, float precio_plato, int is_beb_o_prep) {
        this.id_plat_ped = idplat_ped;
        this.id_plato = id_plato;
        this.nombre_plato = nombre_plato;
        this.id_pedido = id_pedido;
        this.cantidad = cantidad;
        this.estado = estado;
        this.precio = precio;
        this.precio_plato = precio_plato;
        this.is_beb_o_prep = is_beb_o_prep;
    }

    public float getPrecio_plato() {
        return precio_plato;
    }

    public void setPrecio_plato(float precio_plato) {
        this.precio_plato = precio_plato;
    }

    public String getNombre_plato() {
        return nombre_plato;
    }

    public void setNombre_plato(String nombre_plato) {
        this.nombre_plato = nombre_plato;
    }

    public String getId_plat_ped() {
        return id_plat_ped;
    }

    public void setId_plat_ped(String id_plat_ped) {
        this.id_plat_ped = id_plat_ped;
    }

    public String getId_plato() {
        return id_plato;
    }

    public void setId_plato(String id_plato) {
        this.id_plato = id_plato;
    }

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getIs_beb_o_prep() {
        return is_beb_o_prep;
    }

    public void setIs_beb_o_prep(int is_beb_o_prep) {
        this.is_beb_o_prep = is_beb_o_prep;
    }
}
