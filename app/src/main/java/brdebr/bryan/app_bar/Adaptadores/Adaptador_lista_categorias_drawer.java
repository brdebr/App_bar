package brdebr.bryan.app_bar.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Categoria;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 19/02/2017.
 */

public class Adaptador_lista_categorias_drawer extends ArrayAdapter<Categoria>{

    public Adaptador_lista_categorias_drawer(List<Categoria> objetos,Context context) {
        super(context, 0 ,objetos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_lista_categoria_drawer,parent,false);
        }

        // Referencias a los UI
        NetworkImageView imagen = ((NetworkImageView) convertView.findViewById(R.id.img_categoria_itemDrawer));
        TextView nombre = ((TextView) convertView.findViewById(R.id.text_nombre_categoria_itemDrawer));
        RelativeLayout layout = ((RelativeLayout) convertView.findViewById(R.id.relative_layout_item_drawer_categoria));

        // Convertir el item de la lista de catorias en un objeto
        Categoria categoria = getItem(position);

        // Llenar los views con los datos correspondientes del objeto categoria
        nombre.setText(categoria.getNombre());
        layout.setBackgroundColor(Color.parseColor(categoria.getColor()));
        nombre.setTextColor(Color.parseColor(categoria.getColor_letra()));
        imagen.setImageUrl(
                "http://"+ Datos_conexion.getDireccionIP()+"/app_bar/imgs/"+categoria.getImagen()
                , VolleySingleton.getInstance(parent.getContext()).getImageLoader());

        return convertView;

    }
}
