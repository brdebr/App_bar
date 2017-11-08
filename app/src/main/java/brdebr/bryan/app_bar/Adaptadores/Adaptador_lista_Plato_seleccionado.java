package brdebr.bryan.app_bar.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.RecyclerViewClickListener;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Plato_seleccionable;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 29/01/2017.
 */

public class Adaptador_lista_Plato_seleccionado extends RecyclerView.Adapter<Adaptador_lista_Plato_seleccionado.Plato_ViewHolder> {

    /**
     * Lista que contiene los platos que se van a listar
     */
    private List<Plato_seleccionable> items;

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
     * @param context
     * @param itemListener La interfaz de ClickListener
     */
    public Adaptador_lista_Plato_seleccionado(List<Plato_seleccionable> items, Context context, RecyclerViewClickListener itemListener) {
        this.items = items;
        this.context = context;
        this.itemListener = itemListener;
    }

    public Adaptador_lista_Plato_seleccionado(List<Plato_seleccionable> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public Adaptador_lista_Plato_seleccionado() {
        this.items = new ArrayList<Plato_seleccionable>(0);
    }

    public List<Plato_seleccionable> getItems() {
        return items;
    }


    // CREACION CLASE INTERIOR ----------------------------------------------------------

    /**
     * Clase que hará de holder para los views
     */
    public static class Plato_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * TextView que contiene el nombre del plato
         */
        public TextView nombre;

        public TextView cantidad;

        // --------------

        // Constructor
        public Plato_ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.text_nombre_plato_seleccionado_itemDrawer);
            cantidad = ((TextView) v.findViewById(R.id.text_cantidad_seleccionado_itemDrawer));

            // Meter el Click listener
            v.setOnClickListener(this);
        }

        /**
         * Manda la sañal de que se ha hecho click en el view de la lista Platos
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
    public Plato_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_seleccionado_drawer, viewGroup, false);
        return new Plato_ViewHolder(v);
    }

    @Override
    /**
     * Método en el cual se asignan los datos de la lista a los views del holder
     */
    public void onBindViewHolder(Plato_ViewHolder holder, int i) {

        // Asignar datos a los viewHolder
        holder.nombre.setText(items.get(i).getNombre());
        holder.cantidad.setText(String.valueOf(items.get(i).getCantidad()));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
