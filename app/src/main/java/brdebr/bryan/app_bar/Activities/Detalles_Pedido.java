package brdebr.bryan.app_bar.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import brdebr.bryan.app_bar.Adaptadores.Adaptador_lista_Detalles_Pedidos;
import brdebr.bryan.app_bar.Adaptadores.Adaptador_lista_categorias_drawer;
import brdebr.bryan.app_bar.Auxiliares.CustomRequest;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.RecyclerItemClickListener;
import brdebr.bryan.app_bar.Auxiliares.RecyclerViewClickListener;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Categoria;
import brdebr.bryan.app_bar.Clases.Pedido;
import brdebr.bryan.app_bar.Clases.Plato_en_Pedido;
import brdebr.bryan.app_bar.R;

public class Detalles_Pedido extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    /**
     * El pedido que va a llevar todos los datos de los detalles
     */
    Pedido pedido;

    RecyclerView lista_platos_en_pedido;
    /**
     * El adaptador que lleva los platos, cantidades, precios
     */
    Adaptador_lista_Detalles_Pedidos adaptador_lista_detalles_pedidos;

    private RecyclerView.LayoutManager lManager;

    TextView mesa;
    TextView comensales;
    TextView platos;
    TextView bebidas;
    ImageButton overflow;
    PopupMenu menu_overflow;
    TextView bi;
    TextView iva;
    TextView total;
    AppBarLayout toolbar_abajo;
    TextView nombre_plato;
    TextView cantidad_plato;
    TextView precio_plato;

    int ultimo_lista;
    int ultimo_orden;

    /**
     * Encargado de parsear los JSON a objetos
     */
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private boolean ocultos_entregados = false;

    private String aux_id_ped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        ultimo_lista = 0;
        ultimo_orden = 0;

        mesa = ((TextView) findViewById(R.id.txt_id_mesa_detalles_pedido));
        comensales = ((TextView) findViewById(R.id.txt_comensales_detalles_pedido));
        platos = ((TextView) findViewById(R.id.txt_platos_estados_detalles_pedido));
        bebidas = ((TextView) findViewById(R.id.txt_bebidas_estados_detalles_pedido));
        overflow = ((ImageButton) findViewById(R.id.img_btn_overflow_detalles_pedidos));

        bi = ((TextView) findViewById(R.id.txt_vbi_bottom_detalles_pedido));
        iva = ((TextView) findViewById(R.id.txt_viva_bottom_detalles_pedido));
        total = ((TextView) findViewById(R.id.txt_vtotal_bottom_detalles_pedido));

        toolbar_abajo = ((AppBarLayout) findViewById(R.id.toolbar_abajo_detalles));
        lista_platos_en_pedido = ((RecyclerView) findViewById(R.id.recycler_detalles_pedido));

        nombre_plato = ((TextView) findViewById(R.id.txt_cabecera_nombre_plato_item_lista_detalles_pedido));
        cantidad_plato = ((TextView) findViewById(R.id.txt_cabecera_cantidad_plato_item_lista_detalles_pedido));
        precio_plato = ((TextView) findViewById(R.id.txt_cabecera_total_plato_item_lista_detalles_pedido));

        lista_platos_en_pedido.addOnItemTouchListener(new RecyclerItemClickListener(Detalles_Pedido.this, this));
        lista_platos_en_pedido.setAdapter(new Adaptador_lista_Detalles_Pedidos());

        lManager = new LinearLayoutManager(Detalles_Pedido.this);
        lista_platos_en_pedido.setLayoutManager(lManager);
        lista_platos_en_pedido.setHasFixedSize(true);


        nombre_plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenar_platos(1);
            }
        });
        cantidad_plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenar_platos(2);
            }
        });
        precio_plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenar_platos(3);
            }
        });


        Bundle bundle = getIntent().getExtras();
        aux_id_ped = bundle.getString("id_pedido");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (Datos_conexion.getGrupo() == Datos_conexion.GRUPO_CAMARERO) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Detalles_Pedido.this, Lista_platos_seleccionables.class);
                    intent.putExtra("idpedido", pedido.getIdpedido());

                    startActivityForResult(intent, 0);

                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        menu_overflow = new PopupMenu(Detalles_Pedido.this, overflow);
        menu_overflow.getMenuInflater().inflate(R.menu.menu_detalles_pedido, menu_overflow.getMenu());

        menu_overflow.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                // TODO Cambiar este metodo para que lea las ids de los itemmenu, para ello añadir ids a cada uno
                if (item.getTitle().equals("Ocultar entreg.")) {
                    if (ocultos_entregados) {
                        cargarDetallesPedido(aux_id_ped);
                        ocultos_entregados = false;
                        menu_overflow.getMenu().getItem(0).setChecked(false);
                    } else {
                        ocultar_platos_ped_entregados();
                    }
                }
                if (item.getTitle().equals("Comensales")) {
                    abrir_dialog_cambiar_comensales();
                }
                if (item.getTitle().equals("Notas")) {
                    abrir_dialog_cambiar_notas();
                }
                if (item.getTitle().equals("Archivar")) {
                    if (todoPagado()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Pedido.this);
                        builder.setTitle("Archivar pedido");
                        builder.setIcon(R.drawable.notepad);
                        builder.setMessage("El pedido se archivara en el servidor para futuros usos de gestión");
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }

                return true;
            }
        });

        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_overflow.show();
            }
        });

        cargarDetallesPedido(aux_id_ped);
    }

    /**
     * Ordena la lista de platos segun el orden y parametro introducidos
     *
     * @param parametro El parametro por el cual se va a realizar el orden
     */
    public void ordenar_platos(int parametro) {
        final int aux;
        if (ultimo_lista != parametro) {
            ultimo_orden = 1;
            aux = 1;
        } else if (ultimo_lista == parametro && ultimo_orden == 1) {
            ultimo_orden = -1;
            aux = -1;
        } else if (ultimo_lista == parametro && ultimo_orden == -1) {
            ultimo_orden = 1;
            aux = 1;
        } else {
            aux = 0;
        }
        ultimo_lista = parametro;
        switch (parametro) {
            case 1:
                Collections.sort(adaptador_lista_detalles_pedidos.getItems(), new Comparator<Plato_en_Pedido>() {
                    @Override
                    public int compare(Plato_en_Pedido o1, Plato_en_Pedido o2) {
                        return (o1.getNombre_plato().compareToIgnoreCase(o2.getNombre_plato()) * aux);
                    }
                });
                break;
            case 2:
                Collections.sort(adaptador_lista_detalles_pedidos.getItems(), new Comparator<Plato_en_Pedido>() {
                    @Override
                    public int compare(Plato_en_Pedido o1, Plato_en_Pedido o2) {
                        if (o1.getCantidad() > o2.getCantidad()) {
                            return 1 * aux;
                        } else if (o1.getCantidad() < o2.getCantidad()) {
                            return -1 * aux;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
            case 3:
                Collections.sort(adaptador_lista_detalles_pedidos.getItems(), new Comparator<Plato_en_Pedido>() {
                    @Override
                    public int compare(Plato_en_Pedido o1, Plato_en_Pedido o2) {
                        if (o1.getPrecio() > o2.getPrecio()) {
                            return 1 * aux;
                        } else if (o1.getPrecio() < o2.getPrecio()) {
                            return -1 * aux;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
        }

        switch(parametro) {
            case 1:
                cantidad_plato.setText(cantidad_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                precio_plato.setText(precio_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                if (aux == 1) {
                    nombre_plato.setText("↥  " + nombre_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                } else if (aux == -1) {
                    nombre_plato.setText("↧  " + nombre_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                }
                break;
            case 2:
                nombre_plato.setText(nombre_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                precio_plato.setText(precio_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                if (aux == 1) {
                    cantidad_plato.setText("↥  " + cantidad_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                } else if (aux == -1) {
                    cantidad_plato.setText("↧  " + cantidad_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                }
                break;
            case 3:
                nombre_plato.setText(nombre_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                cantidad_plato.setText(cantidad_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                if (aux == 1) {
                    precio_plato.setText("↥  " + precio_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                } else if (aux == -1) {
                    precio_plato.setText("↧  " + precio_plato.getText().toString().replace("↥  ","").replace("↧  ",""));
                }
                break;
        }

        adaptador_lista_detalles_pedidos.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 100) {
            cargarDetallesPedido(aux_id_ped);
            if (adaptador_lista_detalles_pedidos != null) {
                adaptador_lista_detalles_pedidos.notifyDataSetChanged();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pedido != null) {
            cargarDetallesPedido(aux_id_ped);
        }

    }

    /**
     * Oculta los platos entregados
     */
    public void ocultar_platos_ped_entregados() {

        ListIterator<Plato_en_Pedido> iterator = adaptador_lista_detalles_pedidos.getItems().listIterator();

        while (iterator.hasNext()) {
            Plato_en_Pedido auxi = iterator.next();
            if (auxi.getEstado() == 2) {
                iterator.remove();
            }
        }

        adaptador_lista_detalles_pedidos.notifyDataSetChanged();

        ocultos_entregados = true;

        menu_overflow.getMenu().getItem(0).setChecked(true);
    }

    public void cargarDetallesPedido(String id_pedido) {

        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id_pedido", id_pedido);

        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/obtener_detalles_pedido_x_id.php",
                requestBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        procesar_detalles_pedido(response);
                        if (ocultos_entregados) {
                            ocultar_platos_ped_entregados();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleySingleton.
                getInstance(getApplicationContext()).
                addToRequestQueue(customRequest);


    }

    public boolean todoPagado() {

        if (adaptador_lista_detalles_pedidos != null) {
            int size = adaptador_lista_detalles_pedidos.getItems().size();
            int sum = 0;
            if (pedido.getCobrado() == Pedido.COBRADO_CUENTA_PAGADA) {
                for (int i = 0; i < size; i++) {
                    if (adaptador_lista_detalles_pedidos.getItems().get(i).getEstado() == Plato_en_Pedido.ESTADO_RECOGIDO) {
                        sum++;
                    }
                }
            }
            if (size == sum) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void procesar_detalles_pedido(JSONObject response) {

        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "platos" Json
                    JSONArray mensaje = response.getJSONArray("platos");
                    // Parsear con Gson
                    Plato_en_Pedido[] array_platos_en_ped = gson.fromJson(mensaje.toString(), Plato_en_Pedido[].class);

                    // Inicializar adaptador
                    adaptador_lista_detalles_pedidos = new Adaptador_lista_Detalles_Pedidos(
                            Detalles_Pedido.this,
                            new LinkedList<Plato_en_Pedido>(Arrays.asList(array_platos_en_ped))
                    );

                    lista_platos_en_pedido.swapAdapter(adaptador_lista_detalles_pedidos, false);
                    //adaptador_lista_detalles_pedidos.notifyDataSetChanged();

                    JSONArray mensaje_cabecera = response.getJSONArray("pedido");
                    // Parsear con Gson
                    pedido = gson.fromJson(mensaje_cabecera.toString(), Pedido[].class)[0];

                    datosCabecera_a_interfaz();
                    datosInferior_a_interfaz();

                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            Detalles_Pedido.this,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
                case "3":
                    JSONArray mensaje_cabecera2 = response.getJSONArray("pedido");
                    // Parsear con Gson
                    pedido = gson.fromJson(mensaje_cabecera2.toString(), Pedido[].class)[0];

                    datosCabecera_a_interfaz();
                    datosInferior_a_interfaz();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void datosCabecera_a_interfaz() {
        mesa.setText(pedido.getMesa());
        comensales.setText(pedido.getComensales());
        int p1 = 0;
        int p2 = 0;
        int b1 = 0;
        int b2 = 0;
        if (adaptador_lista_detalles_pedidos != null) {
            for (Plato_en_Pedido auxi : adaptador_lista_detalles_pedidos.getItems()
                    ) {
                if (auxi.getIs_beb_o_prep() == 0 && auxi.getEstado() == 2) {
                    p1++;
                }
                if (auxi.getIs_beb_o_prep() == 0) {
                    p2++;
                }
                if (auxi.getIs_beb_o_prep() == 1 && auxi.getEstado() == 2) {
                    b1++;
                }
                if (auxi.getIs_beb_o_prep() == 1) {
                    b2++;
                }
            }
        }
        if (pedido.getCobrado() == 1) {
            menu_overflow.getMenu().getItem(5).setChecked(true);
        }

        platos.setText(p1 + "/" + p2);
        bebidas.setText(b1 + "/" + b2);
    }

    private void datosInferior_a_interfaz() {

        double nbi = 0;
        double niva = 0;
        double ntotal = 0;

        if (adaptador_lista_detalles_pedidos != null) {
            for (int i = 0; i < adaptador_lista_detalles_pedidos.getItems().size(); i++) {
                ntotal += adaptador_lista_detalles_pedidos.getItems().get(i).getPrecio();
            }
        }
        niva = ntotal * 0.21;
        nbi = ntotal * 0.79;

        bi.setText(String.format("%.2f", nbi) + " €");
        iva.setText(String.format("%.2f", niva) + " €");
        total.setText(String.format("%.2f", ntotal) + " €");

        if (pedido != null) {
            switch (pedido.getCobrado()) {
                case Pedido.COBRADO_INICIAL:
                    toolbar_abajo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case Pedido.COBRADO_PEDIDA_CUENTA:
                    toolbar_abajo.setBackgroundColor(getResources().getColor(R.color.naranja_claro));
                    break;
                case Pedido.COBRADO_CUENTA_ENTREGADA:
                    toolbar_abajo.setBackgroundColor(getResources().getColor(R.color.cyan));
                    break;
                case Pedido.COBRADO_CUENTA_PAGADA:
                    toolbar_abajo.setBackgroundColor(getResources().getColor(R.color.gris_medio));
                    break;
            }
        }

    }

    /**
     * Metodo auxiliar para abrir esta activity
     *
     * @param context
     * @param idpedido
     * @return
     */
    public static Intent getLaunchIntent(Context context, String idpedido) {
        Intent intent = new Intent(context, Detalles_Pedido.class);
        intent.putExtra("id_pedido", idpedido);
        return intent;
    }

    /**
     * Un metodo que se usara desde fuera de la clase para que se abra esta activity
     *
     * @param activity La actividad en la que estas
     * @param idpedido El id del plato que se abriran los detalles
     */
    public static void launch(Activity activity, String idpedido) {
        Intent intent = getLaunchIntent(activity, idpedido);
        activity.startActivity(intent);
    }

    /*@Override
    public void recyclerViewListClicked(View v, int position) {
            int aux = adaptador_lista_detalles_pedidos.getItems().get(position).getEstado();
            switch (aux){
                case 0:
                    //Toast.makeText(Detalles_Pedido.this,"Gris clarito",Toast.LENGTH_SHORT);
                    lista_platos_en_pedido.getChildAt(position).setBackground(new ColorDrawable(ContextCompat.getColor(Detalles_Pedido.this, R.color.verde_claro)));
                    break;
                case 1:
                    Toast.makeText(Detalles_Pedido.this,"Verde clarito",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Detalles_Pedido.this,"Gris medio",Toast.LENGTH_SHORT).show();
                    break;
            }
    }*/

    public AlertDialog dialog_cambiar_comensales() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Pedido.this);
        LayoutInflater inflater = LayoutInflater.from(Detalles_Pedido.this);

        View v = inflater.inflate(R.layout.dialog_cambiar_comensales, null);
        builder.setView(v);
        builder.setTitle("Cambiar comensales");

        final EditText edit_comensales = ((EditText) v.findViewById(R.id.edit_comensales_dialog_cambiar));

        edit_comensales.setText(pedido.getComensales());
        edit_comensales.selectAll();

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String aux = edit_comensales.getText().toString();
                modificar_comensales(aux_id_ped, aux);
                //cargarDetallesPedido(pedido.getIdpedido());
                comensales.setText(aux);
                pedido.setComensales(aux);
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


    public AlertDialog dialog_cambiar_notas() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Pedido.this);
        LayoutInflater inflater = LayoutInflater.from(Detalles_Pedido.this);

        View v = inflater.inflate(R.layout.dialog_cambiar_notas, null);
        builder.setView(v);
        builder.setTitle("Editar notas");

        final EditText edit_notas = ((EditText) v.findViewById(R.id.edit_notas_cambiar_detalles_pedido));

        edit_notas.setText(pedido.getNotas());

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String aux = edit_notas.getText().toString();
                modificar_notas(aux_id_ped, aux);
                //cargarDetallesPedido(pedido.getIdpedido());
                pedido.setNotas(aux);
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

    public AlertDialog dialog_confirmar_cambio_estado(final String id_plat_ped, final int estado, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Pedido.this);
        //LayoutInflater inflater = LayoutInflater.from(Detalles_Pedido.this);

        builder.setTitle("Confirmación");

        switch (estado) {
            case 0:
                builder.setMessage("\nMarcar plato como \"Listo para recoger\"");
                break;
            case 1:
                builder.setMessage("\nMarcar plato como \"Entregado\"");
                break;
        }


        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                modificar_estado_a_listo(id_plat_ped, String.valueOf(estado));
                lista_platos_en_pedido.getChildAt(position).setBackground(new ColorDrawable(ContextCompat.getColor(Detalles_Pedido.this, R.color.verde_claro)));

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

    private void modificar_notas(String idpedido, String notas) {

        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id_pedido", idpedido);
        requestBody.put("notas", notas);

        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/modificar_notas.php",
                requestBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        //procesar_detalles_pedido(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleySingleton.
                getInstance(getApplicationContext()).
                addToRequestQueue(customRequest);

    }

    public void abrir_dialog_cambiar_notas() {
        dialog_cambiar_notas().show();
    }

    private void modificar_comensales(String id_pedido, String comensales) {

        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id_pedido", id_pedido);
        requestBody.put("comensales", comensales);

        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/modificar_comensales.php",
                requestBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        //procesar_detalles_pedido(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleySingleton.
                getInstance(getApplicationContext()).
                addToRequestQueue(customRequest);
    }

    public void abrir_dialog_cambiar_comensales() {
        dialog_cambiar_comensales().show();
    }

    public void modificar_estado_a_listo(String id_plato_ped, String estado) {

        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id_plato_ped", id_plato_ped);
        requestBody.put("estado", estado);

        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                "http://" + Datos_conexion.getDireccionIP() + "/app_bar/modificar_estado_plato_ped_a_listo.php",
                requestBody,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        //procesar_detalles_pedido(response);
                        cargarDetallesPedido(aux_id_ped);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleySingleton.
                getInstance(getApplicationContext()).
                addToRequestQueue(customRequest);

    }

    @Override
    public void onItemClick(View childView, int position) {

        int aux = adaptador_lista_detalles_pedidos.getItems().get(position).getEstado();
        String aux2 = adaptador_lista_detalles_pedidos.getItems().get(position).getId_plat_ped();

        switch (aux) {
            case 0:
                if (Datos_conexion.getGrupo() == Datos_conexion.GRUPO_COCINA) {
                    dialog_confirmar_cambio_estado(aux2, aux, position).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Pedido.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setMessage("Espera a que cocina cambie el estado a \"Listo para recoger\"");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                //modificar_estado_a_listo(aux2,String.valueOf(aux));
                //lista_platos_en_pedido.getChildAt(position).setBackground(new ColorDrawable(ContextCompat.getColor(Detalles_Pedido.this, R.color.verde_claro)));

                break;
            case 1:

                if (Datos_conexion.getGrupo() == Datos_conexion.GRUPO_CAMARERO) {
                    dialog_confirmar_cambio_estado(aux2, aux, position).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Pedido.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setMessage("Solo los camareros pueden recoger pedidos");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                //modificar_estado_a_listo(aux2,String.valueOf(aux));
                //lista_platos_en_pedido.getChildAt(position).setBackground(new ColorDrawable(ContextCompat.getColor(Detalles_Pedido.this, R.color.gris_medio)));

                break;
            case 2:

                ocultar_platos_ped_entregados();

                break;
        }

    }

    @Override
    public void onItemLongPress(View childView, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Pedido.this);

        if (adaptador_lista_detalles_pedidos.getItems().get(position).getEstado() < 2) {

            builder.setTitle("Eliminar plato seleccionado ?");
            builder.setMessage("\n" + adaptador_lista_detalles_pedidos.getItems().get(position).getNombre_plato() + "      x" + String.format("%.0f", adaptador_lista_detalles_pedidos.getItems().get(position).getCantidad()));


            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String aux_idplat = adaptador_lista_detalles_pedidos.getItems().get(position).getId_plat_ped();

                    if (adaptador_lista_detalles_pedidos.getItems().size() == 1 && adaptador_lista_detalles_pedidos != null) {
                        adaptador_lista_detalles_pedidos.getItems().remove(0);
                        adaptador_lista_detalles_pedidos.notifyDataSetChanged();
                    }

                    Map<String, String> requestBody = new HashMap<String, String>();
                    requestBody.put("id_plato_ped", aux_idplat);

                    CustomRequest customRequest = new CustomRequest(
                            Request.Method.POST,
                            "http://" + Datos_conexion.getDireccionIP() + "/app_bar/borrar_plato_pedido.php",
                            requestBody,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    // Procesar la respuesta Json
                                    //procesar_detalles_pedido(response);
                                    cargarDetallesPedido(aux_id_ped);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    );

                    VolleySingleton.
                            getInstance(getApplicationContext()).
                            addToRequestQueue(customRequest);

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        } else {
            builder.setTitle("Error");
            builder.setIcon(R.drawable.ic_warning);
            builder.setMessage("\n No se pueden borrar platos que ya estan entregados");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

    }
}
