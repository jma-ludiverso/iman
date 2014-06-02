package net.jma.iman;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.jma.clases.AccionesListas;
import net.jma.clases.AdaptadorCategorias;
import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class activityNuevaLista extends Activity {

	ImageButton btnIcono=null;
	boolean bDesdeDialogo = false;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevalista);
		
		btnIcono = (ImageButton)findViewById(R.id.imgIcono);      
		Evento_Datos evDatos = new Evento_Datos();
		evDatos.setInstancia(this);
		evDatos.setTipoEvento(Evento_Acciones.ICONO_LISTA);
		btnIcono.setTag(evDatos);
		btnIcono.setOnClickListener(new Eventos());  		
		
		Button btnNueva = (Button)findViewById(R.id.btnNueva);
		Evento_Datos evBoton = new Evento_Datos();
		evBoton.setInstancia(this);
		evBoton.setTipoEvento(Evento_Acciones.GRABAR_NUEVA_LISTA);
		EditText txtNombreLista = (EditText)findViewById(R.id.txtNombreLista);
		EditText[] arrObligatorios = {txtNombreLista}; 
		evBoton.setArrObligatorios(arrObligatorios);
		evBoton.setDestino(activityPrincipal.class);
		btnNueva.setTag(evBoton);
		btnNueva.setOnClickListener(new Eventos());
		
		AccionesListas accListas = new AccionesListas(this);
		Cursor cCategorias = accListas.DameCategorias(-1);
		ListView lvCategorias = (ListView)findViewById(R.id.lvNuevaListaCategorias);
		AdaptadorCategorias ad = new AdaptadorCategorias(this, cCategorias, false);
		ad.setDatosLista(-1, null);
		lvCategorias.setAdapter(ad);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setTitle(R.string.dlgTitulo);
		dlg.setIcon(R.drawable.icon);
		
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dlgiconolista,(ViewGroup) findViewById(R.id.dlgIconoLista_contenedor));
		
		Button btnGaleria = (Button)layout.findViewById(R.id.btnGaleria);
		Button btnCamara = (Button)layout.findViewById(R.id.btnCamara);
		
		Evento_Datos evGaleria = new Evento_Datos();
		evGaleria.setInstancia(this);
		evGaleria.setTipoEvento(Evento_Acciones.ICONO_LISTA_GALERIA);
		btnGaleria.setTag(evGaleria);
		btnGaleria.setOnClickListener(new Eventos());
		
		Evento_Datos evCamara = new Evento_Datos();
		evCamara.setInstancia(this);
		evCamara.setTipoEvento(Evento_Acciones.ICONO_LISTA_CAMARA);
		btnCamara.setTag(evCamara);
		btnCamara.setOnClickListener(new Eventos());
		
		dlg.setView(layout);
		return dlg.create();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bDesdeDialogo = true;
		if(requestCode==1){
			if(data != null){
				Uri imgGaleria = data.getData();
				InputStream is;
				try {
				    is = getContentResolver().openInputStream(imgGaleria);
				    BufferedInputStream bis = new BufferedInputStream(is);
				    Bitmap bitmap = BitmapFactory.decodeStream(bis);	
				    btnIcono.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 72, 72, false));
				} catch (FileNotFoundException e) {
					
				}			
			}
		    dismissDialog(0);
		}
		if(requestCode==2){
			if (data != null) {
				if (data.hasExtra("data")) {
					btnIcono.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
				}				
			}
			dismissDialog(0);
		}		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		if(!bDesdeDialogo){
			finish();
		}else{
			bDesdeDialogo = false;
		}
	}
	
}
