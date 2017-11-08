package brdebr.bryan.app_bar.Activities.Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import brdebr.bryan.app_bar.Activities.Lista_platos;
import brdebr.bryan.app_bar.Activities.Detalles_Plato;
import brdebr.bryan.app_bar.Adaptadores.Adaptador_lista_Plato;
import brdebr.bryan.app_bar.Auxiliares.CustomRequest;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.RecyclerViewClickListener;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Plato;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 29/01/2017.
 */

public class Fragment_lista_platos extends Fragment implements RecyclerViewClickListener{


    /**
     * Adaptador del recycler view del fragmento "Lista platos"
     */
    private Adaptador_lista_Plato adapter;

    /**
     * Instancia global del recycler view
     */
    public RecyclerView lista;

    /**
     * Instancia global del administrador de fragmentos
     */
    private RecyclerView.LayoutManager lManager;

    /**
     * El que se encarga de hacer parse a los strings desde JSON y al reves
     */
    private Gson gson = new Gson();


    public Fragment_lista_platos() {
    }

    /**
     * Inflar el fragmento con el layout de la lista
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Asignar el layout al fragmento
        View v = inflater.inflate(R.layout.fragment_lista_platos, container, false);

        // Coger la referencia del recicler view y poner su tamaño fijo
        lista = (RecyclerView) v.findViewById(R.id.recycler_lista);
        lista.setAdapter(new Adaptador_lista_Plato());

        // Usar un administrador para LinearLayout
        lista.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(lManager);
        
        cargarAdaptador_all();
        // Cargar datos en el adaptador


        return v;
    }

    /**
     * Carga el adaptador con los platos obtenidas
     * en la respuesta
     */
    public void cargarAdaptador_all() {
        // Petición POST
        VolleySingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                "http://"+ Datos_conexion.getDireccionIP()+"/app_bar/obtener_platos.php",
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta(response);
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
     * Carga el adaptador con los platos de la consulta de platos filtrada por categoria
     * @param categoria La categoria en formato numero (orden en la lista)
     */
    public void cargarAdaptador_categoria(String categoria) {

        // Meter el campo categoria con su valor en el requestbody
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("categoria",categoria);

        // Petición POST
        VolleySingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new CustomRequest(
                                Request.Method.POST,
                                "http://"+ Datos_conexion.getDireccionIP()+"/app_bar/obtener_platos_categoria.php",
                                requestBody,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuesta(response);
                                        ((Lista_platos)getActivity()).cerrrarDrawer();
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
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "platos" Json
                    JSONArray mensaje = response.getJSONArray("platos");
                    // Parsear con Gson
                    Plato[] platos = gson.fromJson(mensaje.toString(), Plato[].class);
                    // Inicializar adaptador
                    adapter = new Adaptador_lista_Plato(Arrays.asList(platos), getActivity(),this);
                    // Setear adaptador a la lista

                    if (lista.getAdapter() == null){
                        lista.setAdapter(adapter);
                    }else {
                        lista.swapAdapter(adapter,false);
                    }


                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Snackbar.make(lista, mensaje2, Snackbar.LENGTH_LONG)
                            .show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Interfaz para conectar la accion de lanzar actividad "Detalles plato"
     * @param v
     * @param position
     */
    @Override
    public void recyclerViewListClicked(View v, int position) {
        Detalles_Plato.launch(getActivity(),adapter.getItems().get(position));
    }
}
