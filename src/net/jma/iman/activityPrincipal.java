package net.jma.iman;

import net.jma.clases.AccionesListas;
import net.jma.clases.AdaptadorListas;
import net.jma.clases.Registro;
import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;

public class activityPrincipal extends Activity {

	Cursor cListas;
	int idListaEliminar;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
		
		ImageButton btnNueva = (ImageButton)findViewById(R.id.btnNueva);      
		Evento_Datos evDatos = new Evento_Datos();
		evDatos.setInstancia(this);
		evDatos.setTipoEvento(Evento_Acciones.NUEVA_LISTA);
		evDatos.setDestino(activityNuevaLista.class);
		btnNueva.setTag(evDatos);
		btnNueva.setOnClickListener(new Eventos());  	
		
		AccionesListas accListas = new AccionesListas(this);
		this.cargaLista(accListas);
		
		ListView lvListas = (ListView)findViewById(R.id.lvMisListas);
		Evento_Datos evLista = new Evento_Datos();
		evLista.setInstancia(this);
		evLista.setDestino(activityLista.class);
		evLista.setTipoEvento(Evento_Acciones.ELIMINAR_LISTA);
		lvListas.setTag(evLista);
		lvListas.setOnItemClickListener(new Eventos());
		lvListas.setOnItemLongClickListener(new Eventos());
	}
	
	public void cargaLista(AccionesListas accListas){
		ListView lvListas = (ListView)findViewById(R.id.lvMisListas);
		cListas = accListas.DameListas();
		AdaptadorListas ad = new AdaptadorListas(this, cListas, false);
		lvListas.setAdapter(ad);			
	}
	
	public void DialogoConfirmacion(int idLista){
		try{			
			this.idListaEliminar = idLista;
			this.showDialog(1);
		}catch (Exception ex){
			
		}
	}	
	
	private void DialogoConfirmado(){
		AccionesListas acListas = new AccionesListas(this);
		acListas.EliminarLista(this.idListaEliminar);
		this.cargaLista(acListas);		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
		try{
		    builder.setTitle(R.string.confirmacionEliminar);
	    	builder.setMessage(R.string.confirmacionEliminar_Lista);
		    builder.setPositiveButton(R.string.confirmacionEliminar_Aceptar, new OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		        DialogoConfirmado();
		    }
		    });
		    builder.setNegativeButton(R.string.confirmacionEliminar_Cancelar, new OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		    });	 
		}catch (Exception ex){
						
		}
	    return builder.create();	
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menuprincipal, menu);
	    return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menPrincipalCerrarSesion:
	            Registro reg = new Registro(this);
	            reg.CerrarSesion();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		AccionesListas accListas = new AccionesListas(this);
		this.cargaLista(accListas);		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cListas.close();
	}

	
	
}
