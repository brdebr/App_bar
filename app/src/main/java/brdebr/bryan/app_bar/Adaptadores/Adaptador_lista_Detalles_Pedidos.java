package brdebr.bryan.app_bar.Adaptadores;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import brdebr.bryan.app_bar.Activities.Detalles_Conexion;
import brdebr.bryan.app_bar.Activities.Detalles_Pedido;
import brdebr.bryan.app_bar.Auxiliares.RecyclerViewClickListener;
import brdebr.bryan.app_bar.Clases.Pedido;
import brdebr.bryan.app_bar.Clases.Plato;
import brdebr.bryan.app_bar.Clases.Plato_en_Pedido;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 29/01/2017.
 */

public class Adaptador_lista_Detalles_Pedidos extends RecyclerView.Adapter<Adaptador_lista_Detalles_Pedidos.Pedido_ViewHolder> {

    /**
     * Lista que contiene los pedidos
     */
    private List<Plato_en_Pedido> items;

    /**
     * Context para usar en alguna operacion interior
     */
    private Context context;

    /**
     * La interfaz necesaria para conectar el adaptador con el fragment/activity
     * creando asi un ClickListener
     */
    private static RecyclerViewClickListener itemListener;


    /**
     * Constructor estandard
     *
     * @param items        Lista de platos
     * @param itemListener La interfaz de ClickListener
     */
    public Adaptador_lista_Detalles_Pedidos(Context context, List<Plato_en_Pedido> items, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.items = items;
        this.itemListener = itemListener;
    }

    public Adaptador_lista_Detalles_Pedidos(Context context, List<Plato_en_Pedido> items) {
        this.context = context;
        this.items = items;
    }

    public Adaptador_lista_Detalles_Pedidos() {
        this.items = new ArrayList<Plato_en_Pedido>(0);
    }

    public List<Plato_en_Pedido> getItems() {
        return items;
    }


    // CREACION CLASE INTERIOR ----------------------------------------------------------

    /**
     * Clase que hará de holder para los views
     */
    public static class Pedido_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        // TODO CAMBIAR DOCUMENTACION EN ESTE ARCHIVO Y ALGUNOS MAS
        /**
         * TextView que contiene la mesa del pedido
         */
        public TextView nombre_plato;
        /**
         * TextView que contiene la cantidad de comensales
         */
        public TextView precio;
        /**
         * TextView que contiene los "platos que faltan"/"platos totales"
         */
        public TextView cantidad;
        /**
         * TextView que contiene las "bebidas que faltan"/"bebidas totales"
         */
        public TextView total;

        public PercentRelativeLayout layout;

        // --------------

        // Constructor
        public Pedido_ViewHolder(View v) {
            super(v);

            nombre_plato = (TextView) v.findViewById(R.id.txt_nombre_plato_item_lista_detalles_pedido);
            precio = (TextView) v.findViewById(R.id.txt_precio_plato_item_lista_detalles_pedido);
            cantidad = (TextView) v.findViewById(R.id.txt_cantidad_plato_item_lista_detalles_pedido);
            total = (TextView) v.findViewById(R.id.txt_total_plato_item_lista_detalles_pedido);
            layout = (PercentRelativeLayout) v.findViewById(R.id.percent_layout_item_detalles_pedido_);

            // Meter el Click listener
            v.setOnClickListener(this);
        }

        /**
         * Manda la sañal de que se ha hecho click en el view de la lista Pedidos
         *
         * @param v El view en el que se ha clicado
         */
        @Override
        public void onClick(View v) {
            // Hace la accion de la interfaz auxiliar para pasar a la actividad que view se ha clicado
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }

        public void colorLayout(ColorDrawable color){
            layout.setBackground(color);
        }
    }
    // FIN CLASE INTERIOR ----------------------------------------------------------

    @Override
    public Pedido_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_detalles_pedido, viewGroup, false);
        return new Pedido_ViewHolder(v);
    }

    @Override
    /**
     * Método en el cual se asignan los datos de la lista a los views del holder
     */
    public void onBindViewHolder(final Pedido_ViewHolder holder, final int i) {

        // Asignar datos a los viewHolder
        holder.nombre_plato.setText(items.get(i).getNombre_plato());
        holder.precio.setText(String.format("%.2f", items.get(i).getPrecio_plato()) + " €");
        holder.cantidad.setText(String.format("%.0f", items.get(i).getCantidad()));
        holder.total.setText(String.format("%.2f", items.get(i).getPrecio()) + " €");
        switch (items.get(i).getEstado()) {
            case 0:
                holder.layout.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.gris_clarito)));
                break;
            case 1:
                holder.layout.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.verde_claro)));
                break;
            case 2:
                holder.layout.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.gris_medio)));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

