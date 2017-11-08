package brdebr.bryan.app_bar.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import brdebr.bryan.app_bar.Auxiliares.Datos_conexion;
import brdebr.bryan.app_bar.R;

/*
    Clase de actividad Menu Principal
 */
public class MenuPrincipal extends AppCompatActivity {


    Button btn_entrar;
    MenuItem wifiItem;

    @Override
    /**
     * Asignar el layout a la actividad
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        btn_entrar = ((Button) findViewById(R.id.btn_conectar_main));

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Datos_conexion.getNombre()!=null){
                    Intent intent = new Intent(MenuPrincipal.this, Lista_Pedidos.class);
                    startActivity(intent);
                }else {
                    dialogo_necesaria_conexion();
                }

            }
        });

    }


    @Override
    /**
     * Crear el Action Bar con el inflate
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        wifiItem = menu.findItem(R.id.menu_conexion);
        cambiar_icono_wifi();
        return true;
    }

    public void dialogo_necesaria_conexion(){
        AlertDialog.Builder dialogo_fallo = new AlertDialog.Builder(MenuPrincipal.this);

        dialogo_fallo.setTitle("No conectado");
        dialogo_fallo.setMessage("Es necesario conectarse al servidor para acceder a los datos");
        dialogo_fallo.setIcon(R.drawable.ic_warning);
        dialogo_fallo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogo_fallo.show();
    }

    public void cambiar_icono_wifi(){

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiItem != null){

            if (Datos_conexion.getDireccionIP()== null || Datos_conexion.getNombre() == null){
                wifiItem.setIcon(getResources().getDrawable(R.drawable.wifi_rojo));
            }
            if (Datos_conexion.getDireccionIP()!= null && Datos_conexion.getNombre() != null){
                if (mWifi.isConnected()){
                    wifiItem.setIcon(getResources().getDrawable(R.drawable.wifi_verde));
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        cambiar_icono_wifi();

    }

    @Override
    /**
     * Escucha de cuando se selecciona un item del ActionBar
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_conexion) {
            Intent intent = new Intent(MenuPrincipal.this, Detalles_Conexion.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_cartaPlatos) {
            if (Datos_conexion.getNombre()!=null){
                Intent intent = new Intent(MenuPrincipal.this, Lista_platos.class);
                startActivity(intent);
            }else {
                dialogo_necesaria_conexion();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
