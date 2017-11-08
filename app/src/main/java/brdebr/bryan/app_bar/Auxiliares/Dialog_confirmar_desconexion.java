package brdebr.bryan.app_bar.Auxiliares;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import brdebr.bryan.app_bar.Activities.Lista_platos;
import brdebr.bryan.app_bar.Activities.MenuPrincipal;
import brdebr.bryan.app_bar.R;

public class Dialog_confirmar_desconexion extends AppCompatActivity {

    Button btn, btn2;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        setContentView(R.layout.activity_dialog_confirmar_desconexion);

        bundle = getIntent().getExtras();

        btn = ((Button) findViewById(R.id.btn_confirmar_desconexion));
        btn2 = ((Button) findViewById(R.id.btn_cancelar_desconexion));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null && bundle.containsKey("accion")) {
                    if (bundle.get("accion").equals("desconectar")) {
                        NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManger.cancel(01); // TODO añadir cierre de servicio
                        Datos_conexion.logout();
                        Intent intent = new Intent(Dialog_confirmar_desconexion.this,MenuPrincipal.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finishAffinity();
                        startActivity(intent);
                    }
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Dialog_confirmar_desconexion.this.finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        setTitle(Html.fromHtml("<b><font color='#000000'>GestiBar </font></b>"));
        this.getWindow().setBackgroundDrawableResource(R.drawable.caja_texto_cuadrada);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        bundle = intent.getExtras();
    }
}
