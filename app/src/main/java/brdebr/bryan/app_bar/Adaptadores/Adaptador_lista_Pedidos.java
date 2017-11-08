package brdebr.bryan.app_bar.Adaptadores;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brdebr.bryan.app_bar.Activities.Lista_Pedidos;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.CallBackListener;
import brdebr.bryan.app_bar.Auxiliares.RecyclerViewClickListener;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Pedido;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 29/01/2017.
 */

public class Adaptador_lista_Pedidos extends RecyclerView.Adapter<Adaptador_lista_Pedidos.Pedido_ViewHolder> {

    /**
     * Lista que contiene los pedidos
     */
    private List<Pedido> items;

    /**
     * La interfaz necesaria para conectar el adaptador con el fragment/activity
     * creando asi un ClickListener
     */
    private static RecyclerViewClickListener itemListener;

    /**
     * Context para usar en alguna operacion interior
     */
    private Context context;


    /**
     * Constructor estandard
     *
     * @param items        Lista de platos
     * @param itemListener La interfaz de ClickListener
     */
    public Adaptador_lista_Pedidos(Context context, List<Pedido> items, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.items = items;
        this.items = items;
        this.itemListener = itemListener;
    }

    public Adaptador_lista_Pedidos() {
        this.items = new ArrayList<Pedido>(0);
    }

    public List<Pedido> getItems() {
        return items;
    }


    // CREACION CLASE INTERIOR ----------------------------------------------------------

    /**
     * Clase que hará de holder para los views
     */
    public static class Pedido_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * TextView que contiene la mesa del pedido
         */
        public TextView mesa;
        /**
         * TextView que contiene la cantidad de comensales
         */
        public TextView comensales;
        /**
         * TextView que contiene los "platos que faltan"/"platos totales"
         */
        public TextView platos;
        /**
         * TextView que contiene las "bebidas que faltan"/"bebidas totales"
         */
        public TextView bebidas;
        /**
         * TextView que contiene el estado de cobro del pedido
         */
        public CheckBox cobrado;
        /**
         * ImageView que contiene el icono de tenedor y cuchillo que representa si hay platos por recoger
         */
        public ImageView img_platos;
        /**
         * ImageView que contiene el icono de vaso que representa si hay bebidas por recoger
         */
        public ImageView img_bebidas;

        public PercentRelativeLayout layout;

        // --------------

        // Constructor
        public Pedido_ViewHolder(View v) {
            super(v);

            mesa = (TextView) v.findViewById(R.id.txt_mesa_lista_pedidos);
            comensales = (TextView) v.findViewById(R.id.txt_comensales_lista_pedidos);
            platos = (TextView) v.findViewById(R.id.txt_platos_lista_pedidos);
            bebidas = (TextView) v.findViewById(R.id.txt_bebidas_lista_pedidos);
            cobrado = ((CheckBox) v.findViewById(R.id.txt_cobrado_lista_pedidos));
            img_platos = ((ImageView) v.findViewById(R.id.img_platos_lista_pedidos));
            img_bebidas = ((ImageView) v.findViewById(R.id.img_bebidas_lista_pedidos));
            layout = ((PercentRelativeLayout) v.findViewById(R.id.layout_item_lista_pedidos));

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
    }
    // FIN CLASE INTERIOR ----------------------------------------------------------

    @Override
    public Pedido_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_pedidos, viewGroup, false);
        return new Pedido_ViewHolder(v);
    }

    @Override
    /**
     * Método en el cual se asignan los datos de la lista a los views del holder
     */
    public void onBindViewHolder(final Pedido_ViewHolder holder, final int i) {

        // Asignar datos a los viewHolder
        holder.mesa.setText(items.get(i).getMesa());
        holder.comensales.setText(items.get(i).getComensales());

        // Marcar o no marcar cobrado en funcion de si el valor es mayor que 0
        if (items.get(i).getCobrado() == 0) {
            holder.cobrado.setChecked(false);
        } else {
            holder.cobrado.setChecked(true);
        }

        // Asignar los valores de p1,p2 b1,b2 a los TextView
        holder.platos.setText(items.get(i).getP1() + "/" + items.get(i).getP2());
        holder.bebidas.setText(items.get(i).getB1() + "/" + items.get(i).getB2());

        // Tintar de verde el icono si hay alguno de esos listo para recoger
        if (items.get(i).getP3() > 0) {
            holder.img_platos.setColorFilter(ContextCompat.getColor(context, R.color.verde_oscuro));
        } else {
            holder.img_platos.setColorFilter(R.color.transparente);
        }
        if (items.get(i).getB3() > 0) {
            holder.img_bebidas.setColorFilter(ContextCompat.getColor(context, R.color.verde_oscuro));
        } else {
            holder.img_bebidas.setColorFilter(R.color.transparente);
        }

        if (items.get(i).getCreador().matches(Datos_conexion.getNombre())){
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.caja_pedido_cuadrada_oscura));
        }else {
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.caja_pedido_cuadrada_oscura_fondo_gris));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

