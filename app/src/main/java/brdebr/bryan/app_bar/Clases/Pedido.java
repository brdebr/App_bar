package brdebr.bryan.app_bar.Clases;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Bryan on 26/02/2017.
 */




public class Pedido {

    /**
     * Identificador de pedido "idpedido"
     */
    private String idpedido;
    /**
     * TIMESTAMP(2), la fecha y hora de creacion, se autocalcula en MySQL
     */
    private Date fecha_hora;
    /**
     * VARCHAR(5), UNICO, identificador de la mesa
     */
    private String mesa;
    /**
     * INT(3) La cantidad de comensales
     */
    private String comensales;
    /**
     * VARCHAR(300), Observaciones respecto al pedido
     */
    private String notas;


    private int cobrado;
    /**
     * El nombre de usuario del que ha creado el pedido
     */
    private String creador;

    /**
     * Los numeros que van en la lista de pedidos que representan cuantos platos/bebidas se han recogido del total
     */
    private int p1,p2,p3,b1,b2,b3;

    public static final int COBRADO_INICIAL=0;
    public static final int COBRADO_PEDIDA_CUENTA=1;
    public static final int COBRADO_CUENTA_ENTREGADA=2;
    public static final int COBRADO_CUENTA_PAGADA=3;


    public Pedido(String idpedido, Date fecha_hora, String mesa, String comensales, int cobrado, String creador) {
        this.idpedido = idpedido;
        this.fecha_hora = fecha_hora;
        this.mesa = mesa;
        this.comensales = comensales;
        this.cobrado = cobrado;
        this.creador = creador;
    }

    public Pedido(String idpedido, Date fecha_hora, String mesa, String comensales, int cobrado, String creador, int p1, int p2, int p3, int b1, int b2, int b3) {
        this.idpedido = idpedido;
        this.fecha_hora = fecha_hora;
        this.mesa = mesa;
        this.comensales = comensales;
        this.cobrado = cobrado;
        this.creador = creador;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    public Pedido(String idpedido, Date fecha_hora, String mesa, String comensales, String notas, int cobrado, String creador, int p1, int p2, int p3, int b1, int b2, int b3) {
        this.idpedido = idpedido;
        this.fecha_hora = fecha_hora;
        this.mesa = mesa;
        this.comensales = comensales;
        this.notas = notas;
        this.cobrado = cobrado;
        this.creador = creador;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }

    public Date getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Date fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getComensales() {
        return comensales;
    }

    public void setComensales(String comensales) {
        this.comensales = comensales;
    }

    public int getCobrado() {
        return cobrado;
    }

    public void setCobrado(int cobrado) {
        this.cobrado = cobrado;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public int getP3() {
        return p3;
    }

    public void setP3(int p3) {
        this.p3 = p3;
    }

    public int getB1() {
        return b1;
    }

    public void setB1(int b1) {
        this.b1 = b1;
    }

    public int getB2() {
        return b2;
    }

    public void setB2(int b2) {
        this.b2 = b2;
    }

    public int getB3() {
        return b3;
    }

    public void setB3(int b3) {
        this.b3 = b3;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
