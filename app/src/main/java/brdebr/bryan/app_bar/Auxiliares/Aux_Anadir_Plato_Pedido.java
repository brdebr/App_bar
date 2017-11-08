package brdebr.bryan.app_bar.Auxiliares;

import java.util.List;

import brdebr.bryan.app_bar.Clases.Plato_seleccionable;

/**
 * Created by Bryan on 12/05/2017.
 */

public class Aux_Anadir_Plato_Pedido {

    private String idpedido;
    private List<Plato_seleccionable> platos;

    public Aux_Anadir_Plato_Pedido(String idpedido, List<Plato_seleccionable> platos) {
        this.idpedido = idpedido;
        this.platos = platos;
    }

    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }

    public List<Plato_seleccionable> getPlatos() {
        return platos;
    }

    public void setPlatos(List<Plato_seleccionable> platos) {
        this.platos = platos;
    }
}
