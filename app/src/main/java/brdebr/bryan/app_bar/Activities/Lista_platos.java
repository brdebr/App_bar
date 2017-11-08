package brdebr.bryan.app_bar.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import brdebr.bryan.app_bar.Activities.Fragments.Fragment_lista_platos;
import brdebr.bryan.app_bar.Adaptadores.Adaptador_lista_categorias_drawer;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Categoria;
import brdebr.bryan.app_bar.R;

public class Lista_platos extends AppCompatActivity {

    /**
     * Referencia al listview que ocupara el drawer
     */
    private ListView lista_drawer_categorias;
    /**
     * Referencia al drawer en si para poder cerrarlo despues de las acciones
     */
    private DrawerLayout drawerLayout_categorias;

    /**
     * El adaptador que contendra los datos del drawer categorias
     */
    private Adaptador_lista_categorias_drawer adaptador_drawer;

    /**
     * Encargado de parsear los JSON a objetos
     */
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);

        // Cambiar el titulo de la actiobar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#000000'>Carta Platos </font></b>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(Lista_platos.this, R.color.naranja_claro)));
        }

        // Sacar referecnias de la lista del drawer
        lista_drawer_categorias = ((ListView) findViewById(R.id.lista_categorias_drawer));

        // Accion de mandar señal de filtrar al fragmento de lista platos
        lista_drawer_categorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                // Sacar la referencia al listview usando el fragmentManager
                Fragment_lista_platos ref = ((Fragment_lista_platos) getSupportFragmentManager().findFragmentById(R.id.fragment_lista_platos));
                // Convertir la posicion en string para pasarla como parametro
                String aux = String.valueOf(position);
                // Cargar el adaptador de la lista_platos filtrando con por "aux"
                ref.cargarAdaptador_categoria(aux);
            }});
        // Sacar referencia del drawer
        drawerLayout_categorias = ((DrawerLayout) findViewById(R.id.drawerlayout_carta_platos));

        // Cargar el adaptador de lista_platos con todos los platos
        cargarAdaptadorDrawer();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carta_platos, menu);
        if (Datos_conexion.getGrupo() != Datos_conexion.GRUPO_COCINA){
            menu.removeItem(R.id.menu_carta_añadir_plato);
        }
        return true;
    }

    // Al volver a la actividad re-cargar los datos para actualizar
    @Override
    protected void onResume() {
        super.onResume();
        ((Fragment_lista_platos) getSupportFragmentManager().findFragmentById(R.id.fragment_lista_platos)).cargarAdaptador_all();
    }

    // Acciones de cuando se selecciona algo en el menu actiobar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_carta_mostrar_todo:
                // Llamar al metodo cargar datos del fragmento lista_platos
                ((Fragment_lista_platos) getSupportFragmentManager().findFragmentById(R.id.fragment_lista_platos)).cargarAdaptador_all();
                // Cerrar el drawer
                cerrrarDrawer();
                Snackbar.make(lista_drawer_categorias, "Mostrando todo los platos", Snackbar.LENGTH_LONG)
                        .show();

                return true;
            case R.id.menu_carta_añadir_plato:
                // Crear un intent para ir a la actividad crear plato
                Intent intent = new Intent(this,Anadir_Plato.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Carga el adaptador con las categorias obtenidas
     * en la respuesta
     */
    public void cargarAdaptadorDrawer() {
        // Petición GET
        VolleySingleton.
                getInstance(Lista_platos.this).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                "http://"+ Datos_conexion.getDireccionIP()+"/app_bar/obtener_categorias_drawer.php",
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuestaDrawer(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Log.d(CAG, "Error Volley: " + error.getMessage());
                                    }
                                }

                        )
                );
    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuestaDrawer(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "categorias" Json
                    JSONArray mensaje = response.getJSONArray("categorias");
                    // Parsear con Gson
                    Categoria[] array_categorias = gson.fromJson(mensaje.toString(), Categoria[].class);
                    // Inicializar adaptador
                    adaptador_drawer = new Adaptador_lista_categorias_drawer(Arrays.asList(array_categorias),Lista_platos.this);
                    // Setear adaptador a la lista
                    lista_drawer_categorias.setAdapter(adaptador_drawer);
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            Lista_platos.this,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    /*Snackbar.make(lista_drawer_categorias, mensaje2, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo auxiliar para cerrar el drawer hacia la izquierda
     */
    public void cerrrarDrawer(){
        drawerLayout_categorias.closeDrawer(Gravity.LEFT);
    }

}
