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
import brdebr.bryan.app_bar.Clases.Plato;
import brdebr.bryan.app_bar.Clases.Plato_seleccionable;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 29/01/2017.
 */

public class Adaptador_lista_Plato_seleccionables extends RecyclerView.Adapter<Adaptador_lista_Plato_seleccionables.Plato_ViewHolder> {

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
    public Adaptador_lista_Plato_seleccionables(List<Plato_seleccionable> items, Context context, RecyclerViewClickListener itemListener) {
        this.items = items;
        this.context = context;
        this.itemListener = itemListener;
    }

    public Adaptador_lista_Plato_seleccionables() {
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
        /**
         * TextView que contiene el precio del plato
         */
        public TextView precio;
        /**
         * NetworkView que contiene la imagen del plato
         */
        public NetworkImageView imagen;
        /**
         * ImageView que tiene el drawable redondo que representa la categoria
         */
        public ImageView img_categoria;

        public EditText cantidad;

        public ImageButton btn_anadir;

        // --------------

        // Constructor
        public Plato_ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombrePlato_plato_item_seleccionable);
            precio = (TextView) v.findViewById(R.id.precioPlato_plato_item_seleccionable);
            imagen = ((NetworkImageView) v.findViewById(R.id.foto_plato_item_seleccionable));
            img_categoria = ((ImageView) v.findViewById(R.id.img_categoria_redonda_seleccionable));
            cantidad = ((EditText) v.findViewById(R.id.edit_txt_cantidad_plato_seleccionable));
            btn_anadir = ((ImageButton) v.findViewById(R.id.btn_anadir_cantidad_plato_seleccionable));

            // Meter el Click listener
            btn_anadir.setOnClickListener(this);
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
                .inflate(R.layout.item_lista_platos_seleccionable, viewGroup, false);
        return new Plato_ViewHolder(v);
    }

    @Override
    /**
     * Método en el cual se asignan los datos de la lista a los views del holder
     */
    public void onBindViewHolder(Plato_ViewHolder holder, int i) {

        // Asignar datos a los viewHolder
        holder.nombre.setText(items.get(i).getNombre());
        holder.precio.setText((items.get(i).getPrecio() + " €").replace('.', ','));

        // Comprobar si hay un color en la BD antes de tintar el circulo de categoria
        if (items.get(i).getColor() != null) {
            holder.img_categoria.setColorFilter(Color.parseColor(items.get(i).getColor()));
        }
        // TODO Hacer esto bien en cada imageloader
        holder.imagen.setErrorImageResId(R.drawable.no_photo);
        holder.imagen.setDefaultImageResId(R.drawable.no_photo);

        // Comprueba si la hay imagen y es diferente a "" o null
        if (items.get(i).getImagen() != null && !items.get(i).getImagen().isEmpty()) {
            holder.imagen.setImageUrl(
                    // Parsear la URL en UTF-8 para que no haya problemas
                    Datos_conexion.parsearURLimagen(items.get(i).getImagen()),
                    VolleySingleton.getInstance(context).getImageLoader());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
