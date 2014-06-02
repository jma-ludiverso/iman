package net.jma.iman;

import net.jma.clases.AdaptadorContactos;
import net.jma.clases.InfoContacto;
import net.jma.clases.Seguridad;
import net.jma.clases.infoTelefono;
import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import net.jma.ws.Constantes_WS;
import net.jma.ws.WS_Iman;
import net.jma.ws.arrayCadenas;
import net.jma.ws.reqIman_ListadoUsuarios;
import net.jma.ws.reqIman_Usuario;
import net.jma.ws.respIman_ListadoUsuarios;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

public class activityCompartirLista extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compartirlista);
		
		int idLista = getIntent().getExtras().getInt("idLista");
		
		ListView lvContactos = (ListView)findViewById(R.id.lvCompartirContactos);
		infoTelefono info = new infoTelefono(this);
		InfoContacto[] contactos = info.dameContactosIMAN();
		if(contactos!=null){
			AdaptadorContactos adContactos = new AdaptadorContactos(this, 0, contactos);
			lvContactos.setAdapter(adContactos);
		}else{
			this.Actualizar();
		}
		
		Button btnCompartir = (Button)findViewById(R.id.btnCompartir);
		Evento_Datos evDatos = new Evento_Datos();
		evDatos.setInstancia(this);
		evDatos.setTipoEvento(Evento_Acciones.COMPARTIR_LISTA);
		evDatos.setInfoEvento(new int[] {idLista});
		btnCompartir.setTag(evDatos);
		btnCompartir.setOnClickListener(new Eventos());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menucompartir, menu);
	    return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menCompartirActualizar:
	        	this.Actualizar();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void Actualizar(){
		infoTelefono info = new infoTelefono(this);
		InfoContacto[] contactos = info.dameContactos();
		if(contactos!=null){
			try {
				reqIman_ListadoUsuarios buscar = new reqIman_ListadoUsuarios();
				reqIman_Usuario solicitante;
				solicitante = info.dameInfoUsuario();
				solicitante.setProperty(0, "");
				solicitante.setProperty(1, "");
				solicitante.setProperty(2, Seguridad.Encriptar(solicitante.getProperty(2).toString()));				
				buscar.setProperty(0, solicitante);
				arrayCadenas arrBuscados = new arrayCadenas();
				for(int i=0;i<contactos.length;i++){
					arrBuscados.add(Seguridad.Encriptar(contactos[i].getTelfContacto()));
				}
				buscar.setProperty(1, arrBuscados);
				WS_Iman ws = new WS_Iman(this, Constantes_WS.WS_Funcion_Usuarios);
				respIman_ListadoUsuarios Encontrados = ws.ListadoUsuarios(buscar);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}									
		}		
	}
	
	
}
