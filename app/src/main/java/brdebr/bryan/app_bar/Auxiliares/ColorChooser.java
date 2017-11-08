package brdebr.bryan.app_bar.Auxiliares;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import brdebr.bryan.app_bar.R;

/**
 * Created by Bryan on 28/05/2017.
 */

public class ColorChooser extends Activity implements SeekBar.OnSeekBarChangeListener{

    // Los deslizables correspondietes a RedGreenBlue
    SeekBar seek_R;
    SeekBar seek_G;
    SeekBar seek_B;
    /**
     * Layout que tiene de fondo el color actual
     */
    LinearLayout layout_color;
    EditText edit_color;
    Button btn_cambiar;

    /**
     * Preferencias
     */
    //SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_colorchooser);

        // Cambiar el tamaño ventana de dialogo
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        seek_R = (SeekBar) findViewById(R.id.seek_R);
        seek_G = (SeekBar) findViewById(R.id.seek_G);
        seek_B = (SeekBar) findViewById(R.id.seek_B);

        seek_R.setOnSeekBarChangeListener(this);
        seek_G.setOnSeekBarChangeListener(this);
        seek_B.setOnSeekBarChangeListener(this);

        btn_cambiar = (Button) findViewById(R.id.btn_cambiar_color);

        layout_color = (LinearLayout) findViewById(R.id.layout_color);

        edit_color = (EditText) findViewById(R.id.edit_color);
        edit_color.setMaxWidth(edit_color.getWidth());

        // Recojer datos del intent
        Intent intent = getIntent();
        Bundle datos = intent.getExtras();
        int color = datos.getInt("color");
        //final String nombre = datos.getString("colorname");

        // Convertir el color a RGB
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = (color >> 0) & 0xFF;

        layout_color.setBackgroundColor(Color.rgb(red,green,blue));

        seek_R.setProgress(red);
        seek_G.setProgress(green);
        seek_B.setProgress(blue);


        // Pasar el color a las preferencias
        btn_cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences.Editor editor = prefs.edit();
                //editor.putInt(nombre,Color.parseColor(edit_color.getText().toString()));
                //editor.commit();
                finish();
            }
        });

    }

    //  SeekBar Listener ---{-----------------------------------------------------------------------

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        layout_color.setBackgroundColor(Color.rgb(seek_R.getProgress(), seek_G.getProgress(), seek_B.getProgress()));
        int colorInt = ((ColorDrawable) layout_color.getBackground()).getColor();
        edit_color.setText("#"+Integer.toHexString(colorInt));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //----------------------}-----------------------------------------------------------------------



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_color_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}