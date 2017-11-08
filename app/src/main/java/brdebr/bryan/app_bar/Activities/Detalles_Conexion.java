package brdebr.bryan.app_bar.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import brdebr.bryan.app_bar.Auxiliares.CustomRequest;
import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.Auxiliares.Dialog_confirmar_desconexion;
import brdebr.bryan.app_bar.Auxiliares.VolleySingleton;
import brdebr.bryan.app_bar.Clases.Usuario;
import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 07/02/2017.
 */

public class Detalles_Conexion extends AppCompatActivity {

    // Referencias a los views del layout
    Button conectar;
    Button desconectar;

    EditText estado;
    EditText direccion;
    EditText puerto;
    EditText log;

    EditText usuario;
    EditText contraseña;
    //---------------------------------------

    /**
     * El sharedPreferences que almacenara la direccion IP y el nombre de usuario
     */
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_conexion);

        // Llenar los objetos de view con las referencias
        conectar = ((Button) findViewById(R.id.btn_conectar_conexion));
        desconectar = ((Button) findViewById(R.id.btn_desconectar_conexion));

        usuario = ((EditText) findViewById(R.id.edit_usuario_conexion));
        contraseña = ((EditText) findViewById(R.id.edit_contraseña_conexion));
        estado = ((EditText) findViewById(R.id.edit_estado_conexion));
        direccion = ((EditText) findViewById(R.id.edit_direccion_conexion));
        puerto = ((EditText) findViewById(R.id.edit_puerto_conexion));
        log = ((EditText) findViewById(R.id.edit_log_conexion));
        //--------------------------------------------------

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#000000'>Configurar conexión </font></b>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(Detalles_Conexion.this, R.color.azul_claro)));
        }
        /**
         * Action listener del boton conectar
         */
        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectarse_OLD();
            }
        });
        desconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desconectarse();
            }
        });

        // Crear el shared prefences en modo privado y usando de nombre "Prefs_conexion"
        prefs = getSharedPreferences("Prefs_conexion", Context.MODE_PRIVATE);

        // Poner los datos de las preferencias en los campos
        // TODO NO GUARDA LA IP BIEN Y NO DEBE GUARDAR LA CONTRASEÑA DEL USUARIO
        direccion.setText(prefs.getString("direccion", "192.168.1.101"));
        usuario.setText(prefs.getString("usuario", "Bryan"));

        /**
         * El keyListener que se encarga de hacer el metodo conectarse al pulsar enter en el campo contraseña
         */
        contraseña.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Accion de conectarse
                    //conectarse(); // TODO Revisar : poner aqui la accion de conectarse al pulsar enter en contraseña
                    return true;
                }
                return false;
            }
        });

    }

    private void desconectarse() {
        NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.cancel(01);
    }

    /**
     * Metodo usado para crear la peticion de conectarse y en caso de que lo consiga
     * ir a la actividad "Carta Platos"
     */
    public void conectarse_OLD() {

        // Meter los parametros
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("nombre", usuario.getText().toString());
        requestBody.put("password", contraseña.getText().toString());
        // Guardar la direccion en aux hasta que no se consiga conectar
        String aux = direccion.getText().toString();

        VolleySingleton.
                getInstance(getApplicationContext()).
                addToRequestQueue(
                        new CustomRequest(
                                Request.Method.POST,
                                "http://" + aux + "/app_bar/hacer_login.php",
                                requestBody,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesar_login_response(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error != null) {

                                            AlertDialog.Builder dialogo_fallo = new AlertDialog.Builder(Detalles_Conexion.this);
                                            dialogo_fallo.setTitle("Error de conexión");
                                            dialogo_fallo.setMessage("Se ha producido un error en el intento de conexión \n" + error.getMessage());
                                            dialogo_fallo.setIcon(R.drawable.ic_warning);
                                            dialogo_fallo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            dialogo_fallo.show();
                                        } else {
                                            Log.d("TAG", "Error Volley: " + error.getMessage());
                                            log.append("\n" + error.getMessage());
                                        }

                                    }
                                }
                        )
                );

    }



    public void conectar_notificacion() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        // Poner nombre
        builder.setTicker("GestiBar");

        // Cargar layout
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.layout_notificacion);
        // Setear textos
        contentView.setTextViewText(R.id.txt_fila1_notificacion, "Usuario : "+Datos_conexion.getNombre());
        contentView.setTextViewText(R.id.txt_fila2_notificacion, "- Pedidos activos : "+ new Random().nextInt(20)+" \n- Platos listos : 13 ");

        // Accion en boton desconectar
        Intent accionIntent = new Intent(Detalles_Conexion.this, Dialog_confirmar_desconexion.class);
        accionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        accionIntent.putExtra("accion", "desconectar");
        PendingIntent pendingIntentAction = PendingIntent.getActivity(Detalles_Conexion.this, 010, accionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btn_notificacion,pendingIntentAction);

        // Meter layout como contenido de la notificacion
        builder.setContent(contentView);

        // Vibracion y color
        builder.setVibrate(new long[]{0, 700, 70, 350});
        //builder.setLights(Color.GREEN, 3000, 3000);

        // Iconos grande y pequeño
        builder.setSmallIcon(R.drawable.logo24);
        builder.setLargeIcon(BitmapFactory.decodeResource(Detalles_Conexion.this.getResources(), R.drawable.logo));

        // Accion de ir a lista pedidos
        Intent notificationIntent = new Intent(this, Lista_Pedidos.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 23, notificationIntent, 0);
        builder.setContentIntent(contentIntent);

        // Hacer notificacion permanente
        builder.setOngoing(true);
        Notification notification = builder.build();

        // Mandar la notificacion
        NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(01, notification);

    }

    /**
     * Procesar la respuesta del intento de login
     *
     * @param response
     */
    private void procesar_login_response(JSONObject response) {

        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            switch (estado) {
                case "1": // EXITO
                    // Obtener array "usuario" Json
                    JSONObject mensaje = response.getJSONObject("usuario");
                    // Parsear con Gson
                    Gson gson = new Gson();
                    Usuario user = gson.fromJson(mensaje.toString(), Usuario.class);

                    // Comprobar si el nombre de usuario es correcto y la contraseña tambien
                    if (user.getNombre().equals(usuario.getText().toString()) && user.getPassword().equals(contraseña.getText().toString())) {
                        // En caso de serlo crear un editor de preferencias
                        SharedPreferences.Editor editor = prefs.edit();
                        // Al editor le meto la preferencia "direccion" y nombre
                        editor.putString("direccion", direccion.getText().toString());
                        editor.putString("usuario", usuario.getText().toString());
                        editor.commit();
                        // Tambien meto esos datos en una clase global para acceder a ellos en un futuro
                        Datos_conexion.setDireccionIP(direccion.getText().toString());
                        Datos_conexion.setNombre(usuario.getText().toString());
                        Datos_conexion.setGrupo(Integer.parseInt(user.getGrupo()));

                        conectar_notificacion();

                        finish();
                    }
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    AlertDialog.Builder dialogo_fallo = new AlertDialog.Builder(Detalles_Conexion.this);

                    dialogo_fallo.setTitle("Error de autentificación");
                    dialogo_fallo.setMessage(mensaje2);
                    dialogo_fallo.setIcon(R.drawable.ic_warning);
                    dialogo_fallo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialogo_fallo.show();
                    log.append(mensaje2 + "\n");

                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");

                    AlertDialog.Builder dialogo_fallo2 = new AlertDialog.Builder(Detalles_Conexion.this);

                    dialogo_fallo2.setTitle("Error de autentificación");
                    dialogo_fallo2.setMessage(mensaje3);
                    dialogo_fallo2.setIcon(R.drawable.ic_warning);

                    dialogo_fallo2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialogo_fallo2.show();
                    log.append(mensaje3 + "\n");
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
