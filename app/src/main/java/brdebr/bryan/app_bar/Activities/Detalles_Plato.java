package brdebr.bryan.app_bar.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import brdebr.bryan.app_bar.Auxiliares.CustomRequest;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Plato;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 20/02/2017.
 */

public class Detalles_Plato extends AppCompatActivity {


    // Referencias a los views de la actividad
    TextView nombre;
    TextView precio;
    TextView descripcion;
    TextView categoria;
    NetworkImageView imagen;
    ImageView categoria_color;

    // El objeto plato que se recibira del intent
    Plato plato_aux;




    /**
     * Un metodo que se usara desde fuera de la clase para que se abra esta activity
     *
     * @param activity La actividad en la que estas
     * @param idPlato  El id del plato que se abriran los detalles
     */
    public static void launch(Activity activity, Plato idPlato) {
        Intent intent = getLaunchIntent(activity, idPlato);
        activity.startActivity(intent);
    }

    /**
     * Metodo auxiliar para abrir esta activity
     *
     * @param context
     * @param idPlato
     * @return
     */
    public static Intent getLaunchIntent(Context context, Plato idPlato) {
        Intent intent = new Intent(context, Detalles_Plato.class);
        intent.putExtra("Plato", idPlato);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_plato);

        if (getSupportActionBar() != null) {
            // Cambiar el titulo del action bar en caso de que haya una
            getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#000000'>Detalles Plato </font></b>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(Detalles_Plato.this,R.color.naranja_claro)));
        }

        // El bundle con el plato que se pasa desde la otra actividad
        Bundle bundle = getIntent().getExtras();

        // Llenar los views con las referencias
        nombre = ((TextView) findViewById(R.id.txt_nombre_plato_detalles));
        precio = ((TextView) findViewById(R.id.txt_precio_detalles_plato));
        descripcion = ((TextView) findViewById(R.id.txt_descripcion_plato_detalles));
        categoria = ((TextView) findViewById(R.id.txt_categoria_detalles));
        imagen = ((NetworkImageView) findViewById(R.id.img_plato_detalles));
        categoria_color = ((ImageView) findViewById(R.id.img_categoria_detalles));
        //------------------------------------------

        // Convertir el plato del bundle a un objeto Plato  auxiliar
        plato_aux = ((Plato) bundle.getSerializable("Plato"));

        // Introducir los datos del plato auxiliar en los views correspondientes
        nombre.setText(plato_aux.getNombre());
        precio.setText(plato_aux.getPrecio() + " €");
        descripcion.setText(plato_aux.getDescripcion());
        categoria.setText(plato_aux.getCategoria());
        // Poner una imagen en caso de error en imageview
        imagen.setErrorImageResId(R.drawable.no_photo2);
        imagen.setDefaultImageResId(R.drawable.no_photo2);
        // Abrir la imagen de la url en el imageview
        imagen.setImageUrl(
                Datos_conexion.parsearURLimagen(plato_aux.getImagen()),
                VolleySingleton.getInstance(Detalles_Plato.this).getImageLoader());

        // Colorear la bolita de categoria con el color correspondiente
        categoria_color.setColorFilter(Color.parseColor(plato_aux.getColor()));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles_plato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /**
             * Accion al pulsar "Borrar plato"
             */
            case R.id.menu_detalles_plato_borrar:

                Snackbar.make(categoria, "Seguro que quiere eliminar este plato ?",
                        Snackbar.LENGTH_INDEFINITE).
                        setActionTextColor
                                (ContextCompat.getColor(Detalles_Plato.this, R.color.naranja_oscuro)).setAction("ELIMINAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        borrar_plato();
                    }
                })
                        .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Procesar la respuesta de la accion borrar plato
     *
     * @param response
     */
    public void procesar_response(JSONObject response) {

        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // En caso de exito cerrar la actividad
                    this.finish();
                    break;
                case "2": // FALLIDO
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            Detalles_Plato.this,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Se encarga de llenar el requestBody y mandar la peticion
     */
    public void borrar_plato() {
        // Poner el idplato y el usuario actual en el requestBody
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("idplato", plato_aux.getIdPlato());
        requestBody.put("usuario", Datos_conexion.getNombre());

        // Comprobacion de si hay imagen y si es asi meter la url en el requestbody
        if (!plato_aux.getImagen().isEmpty()) {
            requestBody.put("nombre_img", plato_aux.getImagen());
        }


        // Peticion a "borrar_plato.php"
        VolleySingleton.
                getInstance(getApplicationContext()).
                addToRequestQueue(
                        new CustomRequest(
                                Request.Method.POST,
                                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/borrar_plato.php",
                                requestBody,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesar_response(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error != null) {

                                        } else {

                                        }
                                    }
                                }
                        )
                );
    }

}


