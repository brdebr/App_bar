package brdebr.bryan.app_bar.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import brdebr.bryan.app_bar.Activities.Fragments.Fragment_lista_platos;
import brdebr.bryan.app_bar.Activities.Fragments.Fragment_lista_platos_seleccionables;
import brdebr.bryan.app_bar.Adaptadores.Adaptador_lista_Plato_seleccionado;
import brdebr.bryan.app_bar.Adaptadores.Adaptador_lista_categorias_drawer;
import brdebr.bryan.app_bar.Auxiliares.Aux_Anadir_Plato_Pedido;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.RecyclerItemClickListener;
import brdebr.bryan.app_bar.Auxiliares.RecyclerViewClickListener;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Categoria;
import brdebr.bryan.app_bar.Clases.Plato;
import brdebr.bryan.app_bar.Clases.Plato_seleccionable;
import brdebr.bryan.app_bar.R;

public class Lista_platos_seleccionables extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    /**
     * Referencia al listview que ocupara el drawer
     */
    private RecyclerView lista_drawer_seleccionados;
    /**
     * Referencia al drawer en si para poder cerrarlo despues de las acciones
     */
    private DrawerLayout drawerLayout_seleccionados;

    /**
     * El adaptador que contendra los datos del drawer categorias
     */
    private Adaptador_lista_Plato_seleccionado adaptador_drawer;

    /**
     * Encargado de parsear los JSON a objetos
     */
    private Gson gson = new Gson();

    private String idpedido;

    private RecyclerView.LayoutManager lManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos_seleccionables);

        // TODO poner un segundo drawerlaout en la derecha y pasar el acual ahi, y en el de la izquierda poner la lista de categorias como en el la carta

        Bundle bundle = getIntent().getExtras();
        idpedido = bundle.getString("idpedido");

        // Cambiar el titulo de la actiobar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#000000'>Carta Platos Seleccionables </font></b>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(Lista_platos_seleccionables.this, R.color.naranja_claro)));
        }

        // Sacar referecnias de la lista del drawer
        lista_drawer_seleccionados = ((RecyclerView) findViewById(R.id.lista_seleccionados_drawer));

        lista_drawer_seleccionados.addOnItemTouchListener(new RecyclerItemClickListener(Lista_platos_seleccionables.this,this));

        // Sacar referencia del drawer
        drawerLayout_seleccionados = ((DrawerLayout) findViewById(R.id.drawerlayout_platos_seleccionados));

        // Cargar el adaptador de lista_platos con todos los platos
        cargarAdaptadorDrawer();

        //((Fragment_lista_platos_seleccionables) getSupportFragmentManager().findFragmentById(R.id.fragment_lista_platos_seleccionables)).cargarAdaptador_seleccionables();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_platos_seleccionables, menu);
        return true;
    }

    @Override
    /**
     * Escucha de cuando se selecciona un item del ActionBar
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_anadir_platos_seleccionados) {

            enviar_platos_a_pedido();
            /*setResult(100);
            this.finish();*/

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void enviar_platos_a_pedido(){

        ArrayList<Plato_seleccionable> lista_seleccionados = new ArrayList<Plato_seleccionable>();
        for (int i = 0; i < adaptador_drawer.getItems().size(); i++) {
            lista_seleccionados.add(adaptador_drawer.getItems().get(i).convertir_a_enviable());
        }

        Aux_Anadir_Plato_Pedido aux_enviar = new Aux_Anadir_Plato_Pedido(idpedido,lista_seleccionados);

        final String json_enviar =  gson.toJson(aux_enviar);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/anadir_platos_pedido.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setResult(100);
                        Lista_platos_seleccionables.this.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json_enviar == null ? null : json_enviar.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {

                    responseString = String.valueOf(response.statusCode);

                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };


        VolleySingleton.getInstance(Lista_platos_seleccionables.this).addToRequestQueue(stringRequest);

    }

    // Al volver a la actividad re-cargar los datos para actualizar
    @Override
    protected void onResume() {
        super.onResume();
        ((Fragment_lista_platos_seleccionables) getSupportFragmentManager().findFragmentById(R.id.fragment_lista_platos_seleccionables)).cargarAdaptador_seleccionables();
    }

    public void anadir_seleccionado(Plato_seleccionable plato,int cantidad){
        Plato_seleccionable aux = new Plato_seleccionable(plato.getIdPlato(),plato.getNombre(),plato.getImagen(),plato.getPrecio(),cantidad,plato.getCategoria(),plato.getColor());
        adaptador_drawer.getItems().add(aux);

        lista_drawer_seleccionados.setAdapter(adaptador_drawer);
        lManager = new LinearLayoutManager(Lista_platos_seleccionables.this);
        lista_drawer_seleccionados.setLayoutManager(lManager);
        lista_drawer_seleccionados.setHasFixedSize(true);

    }

    public AlertDialog dialog_borrar_seleccionado(final int pos){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Lista_platos_seleccionables.this);

        builder.setTitle("Borrar plato de la selección ?");
        builder.setMessage("\n\""+adaptador_drawer.getItems().get(pos).getNombre()+"\" x"+adaptador_drawer.getItems().get(pos).getCantidad());

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adaptador_drawer.getItems().remove(pos);
                adaptador_drawer.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    /**
     * Carga el adaptador con las categorias obtenidas
     * en la respuesta
     */
    public void cargarAdaptadorDrawer() {
        ArrayList<Plato_seleccionable> array = new ArrayList<Plato_seleccionable>();
        adaptador_drawer = new Adaptador_lista_Plato_seleccionado(array,Lista_platos_seleccionables.this);
    }


    /**
     * Metodo auxiliar para cerrar el drawer hacia la izquierda
     */
    public void cerrrarDrawer(){
        drawerLayout_seleccionados.closeDrawer(Gravity.LEFT);
    }

    /*@Override
    public void recyclerViewListClicked(View v, int position) {
        dialog_borrar_seleccionado(position).show();
    }*/

    @Override
    public void onItemClick(View childView, int position) {
        dialog_borrar_seleccionado(position).show();
    }

    @Override
    public void onItemLongPress(View childView, final int position) {

        LayoutInflater inflater = LayoutInflater.from(Lista_platos_seleccionables.this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Cambiar cantidad");

        View v = inflater.inflate(R.layout.dialog_cambiar_cant_plato_seleccionado,null);
        alert.setView(v);

        final EditText edit_cant = ((EditText) v.findViewById(R.id.edit_cantidad_plato_seleccionado));

        //final EditText input = new EditText(this);

        //input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //input.setInputType(InputType.TYPE_CLASS_NUMBER);
        //input.setRawInputType(Configuration.KEYBOARD_12KEY);
        //alert.setView(input);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for OK button here
                adaptador_drawer.getItems().get(position).setCantidad(Integer.parseInt(edit_cant.getText().toString()));
                adaptador_drawer.notifyDataSetChanged();
            }
        });
        alert.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank

            }
        });
        alert.show();


    }





}
