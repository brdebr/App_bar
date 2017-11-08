package brdebr.bryan.app_bar.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import brdebr.bryan.app_bar.Adaptadores.Adaptador_lista_Pedidos;
import brdebr.bryan.app_bar.Auxiliares.ColorChooser;
import brdebr.bryan.app_bar.Auxiliares.CustomRequest;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.RecyclerViewClickListener;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Pedido;
import brdebr.bryan.app_bar.R;

public class Lista_Pedidos extends AppCompatActivity implements RecyclerViewClickListener {

    /**
     * El recyclerview que contiene la lista de pedidos
     */
    public RecyclerView recyclerView;

    //public RelativeLayout relativeLayout;

    private RecyclerView.LayoutManager lManager;

    ImageButton btn_overflow;
    ImageButton btn_refresh;

    ImageView img_grupo;

    boolean mostrar_todos;

    PopupMenu menu_overflow;

    /**
     * El adaptador que contiene los datos de los pedidos
     */
    public Adaptador_lista_Pedidos adaptador_lista_pedidos;

    /**
     * El que se encarga de hacer parse a los strings desde JSON y al reves
     */
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mostrar_todos = false;

        img_grupo = ((ImageView) findViewById(R.id.img_toolbar_lista_pedidos));
        btn_overflow = ((ImageButton) findViewById(R.id.img_btn_overflow_lista_pedidos));
        btn_refresh = ((ImageButton) findViewById(R.id.img_btn_menu_lista_ped_refresh));

        menu_overflow = new PopupMenu(Lista_Pedidos.this, btn_overflow);
        menu_overflow.getMenuInflater().inflate(R.menu.menu_lista_pedido, menu_overflow.getMenu());

        menu_overflow.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_ord_asc_mesa:
                        ordenarPedidos(1, 1);
                        break;
                    case R.id.menu_ord_asc_comens:
                        ordenarPedidos(1, 2);
                        break;
                    case R.id.menu_ord_asc_cobrad:
                        ordenarPedidos(1, 3);
                        break;
                    case R.id.menu_ord_asc_fecha:
                        ordenarPedidos(1, 4);
                        break;
                    case R.id.menu_ord_desc_mesa:
                        ordenarPedidos(2, 1);
                        break;
                    case R.id.menu_ord_desc_comens:
                        ordenarPedidos(2, 2);
                        break;
                    case R.id.menu_ord_desc_cobrad:
                        ordenarPedidos(2, 3);
                        break;
                    case R.id.menu_ord_desc_fecha:
                        ordenarPedidos(2, 4);
                        break;
                    case R.id.menu_lista_pedidos_mostrar_todos:
                        if (mostrar_todos) {
                            mostrar_todos = false;
                            item.setChecked(false);
                        } else {
                            mostrar_todos = true;
                            item.setChecked(true);
                        }
                        cargarAdaptador();
                        break;
                    case R.id.menu_aux_notas:
                        Intent intent = new Intent(Lista_Pedidos.this, ColorChooser.class);
                        intent.putExtra("color", 2020023);
                        startActivity(intent);

                        break;
                }
                return false;
            }
        });

        btn_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_overflow.show();
            }
        });

        switch (Datos_conexion.getGrupo()) {
            case 1:
                img_grupo.setImageResource(R.drawable.waiter);
                break;
            case 2:
                img_grupo.setImageResource(R.drawable.chef);
                break;
            case 3:
                img_grupo.setImageResource(R.drawable.cash_register);
                break;
        }

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Datos_conexion.getGrupo() == Datos_conexion.GRUPO_CAMARERO) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirDialogo_crearPedido();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        //relativeLayout = ((RelativeLayout) findViewById(R.id.content_lista_pedidos));;
        recyclerView = ((RecyclerView) findViewById(R.id.recycler_lista_pedidos_content));
        recyclerView.setAdapter(new Adaptador_lista_Pedidos());

        recyclerView.setHasFixedSize(true);
        lManager = new LinearLayoutManager(Lista_Pedidos.this);
        recyclerView.setLayoutManager(lManager);

        cargarAdaptador();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarAdaptador();
    }

    public void cargarAdaptador() {
        if (mostrar_todos) {
            cargarAdaptador_all();
        } else {
            cargarAdaptador_bycreador();
        }
    }

    public void refresh() {
        Animation animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setRepeatCount(0);
        animation.setDuration(1000);
        btn_refresh.startAnimation(animation);
        cargarAdaptador();
    }

    public void ordenarPedidos(int orden, int dato) {
        final int aux;
        if (orden == 1) {
            aux = 1;
        } else if (orden == 2) {
            aux = -1;
        } else {
            aux = 0;
        }
        switch (dato) {
            case 1:
                Collections.sort(adaptador_lista_pedidos.getItems(), new Comparator<Pedido>() {
                    @Override
                    public int compare(Pedido o1, Pedido o2) {
                        if (Integer.parseInt(o1.getMesa()) > Integer.parseInt(o2.getMesa())) {
                            return 1 * aux;
                        } else if (Integer.parseInt(o1.getMesa()) < Integer.parseInt(o2.getMesa())) {
                            return -1 * aux;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
            case 2:
                Collections.sort(adaptador_lista_pedidos.getItems(), new Comparator<Pedido>() {
                    @Override
                    public int compare(Pedido o1, Pedido o2) {
                        if (Integer.parseInt(o1.getComensales()) > Integer.parseInt(o2.getComensales())) {
                            return 1 * aux;
                        } else if (Integer.parseInt(o1.getComensales()) < Integer.parseInt(o2.getComensales())) {
                            return -1 * aux;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
            case 3:
                Collections.sort(adaptador_lista_pedidos.getItems(), new Comparator<Pedido>() {
                    @Override
                    public int compare(Pedido o1, Pedido o2) {
                        if (o1.getCobrado() > o2.getCobrado()) {
                            return 1 * aux;
                        } else if (o1.getCobrado() < o2.getCobrado()) {
                            return -1 * aux;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
            case 4:
                Collections.sort(adaptador_lista_pedidos.getItems(), new Comparator<Pedido>() {
                    @Override
                    public int compare(Pedido o1, Pedido o2) {
                        if (o1.getFecha_hora().after(o2.getFecha_hora())) {
                            return 1 * aux;
                        } else if (o1.getFecha_hora().before(o2.getFecha_hora())) {
                            return -1 * aux;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
        }

        /*if (dato == 1) {

        } else if (dato == 2) {

        } else if (dato == 3) {


        } else if (dato == 4) {

        }*/

        adaptador_lista_pedidos.notifyDataSetChanged();
    }

    private void abrirDialogo_crearPedido() {
        // TODO sustituir el alerdialog con un popupwindow, y al pinchar en el boton fab, girar este 45º
        dialog_crear_pedido().show();
    }

    public AlertDialog dialog_crear_pedido() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Lista_Pedidos.this);
        LayoutInflater inflater = LayoutInflater.from(Lista_Pedidos.this);

        View v = inflater.inflate(R.layout.dialog_crear_pedido, null);
        builder.setView(v);
        builder.setTitle("Crear Pedido");

        final EditText edit_comensales = ((EditText) v.findViewById(R.id.edit_comensales_dialog_crear_pedido));
        final EditText edit_mesa = ((EditText) v.findViewById(R.id.edit_mesa_dialog_crear_pedido));

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edit_comensales.getText().toString().matches("") || edit_mesa.getText().toString().matches("")) {
                    // TODO advertencia de que hay que rellenar los dos campos
                } else {
                    crearPedido(edit_comensales.getText().toString(), edit_mesa.getText().toString());
                }
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

    private void crearPedido(String comensales, String mesa) {

        // Meter el campo categoria con su valor en el requestbody
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("creador", Datos_conexion.getNombre());
        requestBody.put("comensales", comensales);
        requestBody.put("mesa", mesa);

        // Petición POST
        VolleySingleton.
                getInstance(Lista_Pedidos.this).
                addToRequestQueue(
                        new CustomRequest(
                                Request.Method.POST,
                                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/crear_pedido.php",
                                requestBody,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        comprobarCreacion(response);
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

    private void comprobarCreacion(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String id_pedido = response.getString("id_pedido");
            String estado = response.getString("estado");

            if (Integer.parseInt(estado) == 1) {
                if (!id_pedido.isEmpty()) {
                    String idpedido = response.getString("id_pedido");
                    Detalles_Pedido.launch(Lista_Pedidos.this, idpedido);

                } else {
                    String mensaje = "Ha ocurrido un error en la creación";
                    Snackbar.make(recyclerView, mensaje, Snackbar.LENGTH_LONG)
                            .show();
                }
            } else if (Integer.parseInt(estado) == 2) {
                String mensaje = "Ha ocurrido un error en la creación";
                Snackbar.make(recyclerView, mensaje, Snackbar.LENGTH_LONG)
                        .show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cargarAdaptador_all() {

        VolleySingleton.
                getInstance(Lista_Pedidos.this).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/obtener_pedidos.php",
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

    public void cargarAdaptador_bycreador() {

        // Meter el campo categoria con su valor en el requestbody
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("creador", Datos_conexion.getNombre());

        // Petición POST
        VolleySingleton.
                getInstance(Lista_Pedidos.this).
                addToRequestQueue(
                        new CustomRequest(
                                Request.Method.POST,
                                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/obtener_pedidos_bycreador.php",
                                requestBody,
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

    public void procesarRespuesta(JSONObject response) {

        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "platos" Json
                    JSONArray mensaje = response.getJSONArray("pedidos");
                    // Parsear con Gson
                    Pedido[] pedidos = gson.fromJson(mensaje.toString(), Pedido[].class);
                    // Inicializar adaptador
                    adaptador_lista_pedidos = new Adaptador_lista_Pedidos(Lista_Pedidos.this, Arrays.asList(pedidos), Lista_Pedidos.this);
                    // Setear adaptador a la lista
                    if (recyclerView.getAdapter() == null) {
                        recyclerView.setAdapter(adaptador_lista_pedidos);
                        recyclerView.swapAdapter(adaptador_lista_pedidos, false);
                    } else {
                        recyclerView.swapAdapter(adaptador_lista_pedidos, false);
                    }
                    adaptador_lista_pedidos.notifyDataSetChanged();

                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Snackbar.make(recyclerView, mensaje2, Snackbar.LENGTH_LONG)
                            .show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void recyclerViewListClicked(View v, int position) {
        Detalles_Pedido.launch(Lista_Pedidos.this, adaptador_lista_pedidos.getItems().get(position).getIdpedido());
    }


}
