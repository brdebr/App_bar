package brdebr.bryan.app_bar.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Categoria;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 22/02/2017.
 */

public class Anadir_Plato extends AppCompatActivity {


    /**
     * El dialogo utilizado para seleccionar categoria
     */
    AlertDialog categorias_dialog;

    /**
     * El linear layout que engloba los views de la categoria
     * y tiene el OnClick que abre el dialogo de categorias
     */
    LinearLayout seleccionar_categoria;

    // Views que contienen los datos
    TextView nombre;
    TextView descripcion;
    TextView precio;
    ImageView imagen_plato;
    //-----------------------
    /**
     * El boton que abre el selector de imagen del plato
     */
    ImageView btn_editar_imagen;
    /**
     * El bitmap que se va a enviar al servidor
     */
    Bitmap bitmap;
    /**
     * El int que representa la categoria escogida
     */
    String idCategoria;
    /**
     * La lista de categorias que se llena para poder seleccionar en el dialogo,
     * y para llenar los datos del view que representa la categoria
     */
    Categoria[] categorias;
    /**
     * Los nombres de las categorias convertidos en array para pasarselos al dialogo
     */
    CharSequence[] lista_categorias;

    // Views dentro del layout seleccionar_categoria
    TextView categoria_text;
    ImageView categoria_img_redonda;
    //----------------------

    /**
     * El encargado de parsear la respuesta en JSON a objetos
     */
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout con los datos del xml
        setContentView(R.layout.activity_anadir_plato);
        // Poner el titulo en la actionbar
        if (getSupportActionBar() != null) {
            // Cambiar el titulo del action bar en caso de que haya una
            getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#000000'>Añadir Plato </font></b>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(Anadir_Plato.this,R.color.naranja_claro)));
        }

        // Referencias a los views

        seleccionar_categoria = ((LinearLayout) findViewById(R.id.layout_container_categoria_anadir_plato));

        nombre = ((TextView) findViewById(R.id.txt_nombre_plato_anadir));
        descripcion = ((TextView) findViewById(R.id.txt_descripcion_plato_anadir));
        precio = ((TextView) findViewById(R.id.txt_precio_anadir_plato));
        imagen_plato = ((ImageView) findViewById(R.id.img_plato_detalles_anadir));
        btn_editar_imagen = ((ImageView) findViewById(R.id.btn_editar_imagen_plato));
        categoria_text = ((TextView) findViewById(R.id.txt_categoria_detalles_anadir));
        categoria_img_redonda = ((ImageView) findViewById(R.id.img_categoria_detalles_anadir));

        //-FIN REFERENCIAS ----------------------------------------

        // Inicializar categoria
        idCategoria = "-1";

        /**
         * Accion de editar imagen seleccionando una del dispositivo
         */
        btn_editar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen"), 999);
            }
        });

        /**
         * Accion de crear el dialogo para seleccionar las categorias
         */
        seleccionar_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRadioListDialog();
            }
        });

        /**
         * Accion de llenar la lista de categorias al entrar en la actividad
         */
        llenarCategorias();

    }

    /**
     * Hace una peticion a la BD para sacar solo los nombres y colores de las categorias
     */
    public void llenarCategorias() {

        VolleySingleton.
                getInstance(Anadir_Plato.this).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/obtener_categorias_lista.php",
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta_categorias(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("TAG", "Error Volley: " + error.getMessage());
                                    }
                                }

                        )
                );
    }

    /**
     * La accion a realizar al volver de una actividad a la que se fue esperando un resultado
     * En este caso lo que hace es coger el bitmap seleccionado y meterlo en la variable global
     * y ponerlo en el view "imagen_plato"
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK && data != null) {
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imagen_plato.setImageBitmap(bitmap);
            } catch (IOException e) {

            }

        }
    }

    /**
     * Llenar el actionbar con el menu seleccionado
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_anadir_plato, menu);
        return true;
    }

    /**
     * Manejar la accion de cuando se selecciona un item del actionbar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /**
             * Accion al seleccionar añadir del actionbar
             */
            case R.id.menu_anadir_plato_check:
                Snackbar.make(categoria_text, "Añadir este plato ?", Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(ContextCompat.getColor(Anadir_Plato.this,R.color.verde_claro))
                        .setAction("AÑADIR", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                añadir_plato_Volley();
                            }
                        })
                        .show();



                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Procesa los datos recibidos de la consulta "añadir_plato"
     * @param response
     */
    private void procesar_response(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    this.finish();
                    break;
                case "2": // FALLIDO
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            Anadir_Plato.this,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo para crear el dialog de seleccion de categorias
     */
    public void createRadioListDialog() {

        /**
         * Crear el builder
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(Anadir_Plato.this);

        /**
         * Meter la lista de categorias en items
         */
        final CharSequence[] items = lista_categorias;

        /**
         * Crear el dialogo con los datos pasados y hacer la accion correspondiente
         */
        builder.setTitle("Categoría")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        idCategoria = String.valueOf(which + 1);
                        categoria_text.setText(categorias[which].getNombre());
                        categoria_img_redonda.setColorFilter(Color.parseColor(categorias[which].getColor()));
                        categorias_dialog.dismiss();
                    }
                });
        /**
         * Mostrar el dialogp
         */
        categorias_dialog = builder.show();
    }

    /**
     * Metodo auxiliar para convertir un bitmap a string codificandolo en Base64
     * @param bm El bitmap de la imagen que se desea convertir
     * @return La imagen en formato string base64
     */
    public String getStringImage(Bitmap bm) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, ba);
        byte[] imageByte = ba.toByteArray();
        String encode = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encode;
    }

    /**
     * Procesar la respues de la peticion de categorias
     * @param response
     */
    private void procesarRespuesta_categorias(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "categorias" Json
                    JSONArray mensaje = response.getJSONArray("categorias");
                    // Parsear con Gson
                    categorias = gson.fromJson(mensaje.toString(), Categoria[].class);
                    // Crear un array del tamaño que toca para meter los texto de categoria
                    lista_categorias = new CharSequence[categorias.length];
                    // Un bucle para meter las categorias en el array
                    for (int i = 0; i < categorias.length; i++) {
                        lista_categorias[i] = categorias[i].getNombre();
                    }
                    break;

                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            Anadir_Plato.this,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Hace toda el proceso de crear el requestBody y lanzar la request
     */
    public void añadir_plato_Volley(){

        // Meter datos en el requestBody
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("nombre", nombre.getText().toString());
        requestBody.put("descripcion", descripcion.getText().toString());
        requestBody.put("precio", precio.getText().toString());
        requestBody.put("nombre_img", nombre.getText().toString().trim().replace(' ', '_'));
        requestBody.put("id_categoria", idCategoria);
        if (bitmap!=null){
            requestBody.put("imagen_data", getStringImage(bitmap));
        }
        //-----------------------------------------------


        /**
         * Hacer una peticion JSON, esta esta hecha de forma que ademas
         * añade la cabecera que concreta que se enviara un JSON en UTF-8
         */
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/insertar_plato.php",
                new JSONObject(requestBody),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        procesar_response(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            // La cabecera del requestBody
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };
        // Añadir el request creado anteriormente a la lista de requests
        VolleySingleton.getInstance(Anadir_Plato.this).addToRequestQueue(jsonObjReq);

    }


    /*$comando->bindValue(':nombre',$nombre,PDO::PARAM_STR);
    $comando->bindValue(':descripcion',$descripcion,PDO::PARAM_STR);
    $comando->bindValue(':precio',$precio,PDO::PARAM_STR);
    $comando->bindValue(':imagen',$imagen,PDO::PARAM_STR);
    $comando->bindValue(':categoria',(int) trim($categoria),PDO::PARAM_INT);*/

}