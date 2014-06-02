package net.jma.eventos;

import java.util.Enumeration;
import java.util.Hashtable;

import net.jma.clases.AccionesListas;
import net.jma.clases.CompartirListas;
import net.jma.clases.Registro;
import net.jma.clases.en_MensajeEliminacion;
import net.jma.clases.en_VistaLista;
import net.jma.iman.R;
import net.jma.iman.activityLista;
import net.jma.iman.activityNuevaCategoria;
import net.jma.iman.activityNuevaLista;
import net.jma.iman.activityPrincipal;
import net.jma.iman.controlUnidades;
import net.jma.iman.dlg_AddProducto;
import net.jma.iman.dlg_Base;
import net.jma.iman.dlg_SeleccionCategoria;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Eventos implements OnClickListener, OnItemClickListener, OnItemSelectedListener, OnCheckedChangeListener, OnCancelListener, OnItemLongClickListener, OnLongClickListener {
	
	public void EventoCodigo(Evento_Datos evDatos){
		Intent intent = new Intent(evDatos.getInstancia(), (Class<?>) evDatos.getDestino());
		evDatos.getInstancia().startActivity(intent);    						
	}
	
	private boolean compruebaObligatorios(Evento_Datos evDatos){
		boolean bContinuar = true;
		if(evDatos.getArrObligatorios()!=null){
			EditText[] arrTxt = evDatos.getArrObligatorios();					
			for(int i=0;i<=arrTxt.length-1;i++){
				EditText txt = arrTxt[i];
				if(txt.getText().toString().equals("")){
					Toast.makeText(evDatos.getInstancia(), evDatos.getInstancia().getResources().getText(net.jma.iman.R.string.msgValidacionEvento),Toast.LENGTH_SHORT).show();
					bContinuar=false;
					break;
				}
			}			
		}
		return bContinuar;
	}

	public void onClick(View v) {
		
		try{
			Evento_Datos evDatos = (Evento_Datos)v.getTag();
			boolean bContinuar=this.compruebaObligatorios(evDatos);
			
			if(bContinuar){
				try{
					int requestCode;
					Evento_Acciones tipoEvento = evDatos.getTipoEvento();
					switch(tipoEvento){
						case ADD_PRODUCTO_CATEGORIA:
							int ids[] = evDatos.getIntInfoEvento();
							dlg_AddProducto dialogo = new dlg_AddProducto(evDatos.getInstancia(), ids[0], ids[1]);
							Evento_Datos evDialogo = new Evento_Datos();
							evDialogo.setTipoEvento(Evento_Acciones.DIALOGO_ADD_PRODUCTO);
							EditText arrObligatorios[] = new EditText[1];
							arrObligatorios[0] = (EditText)dialogo.findViewById(R.id.txtAddProductoNombre);
							evDialogo.setArrObligatorios(arrObligatorios);
							evDialogo.setInstancia(evDatos.getInstancia());
							dialogo.setTag(evDialogo);
							dialogo.setOnCancelListener(new Eventos());
							dialogo.show();
							break;
						case BAJA_UNIDADES:
						case SUBE_UNIDADES:
							Hashtable<String, EditText> infoEvento = evDatos.getInfoEvento();
							EditText txtUnidades = infoEvento.get("unidades");
							int intUnidades = Integer.parseInt(txtUnidades.getText().toString());
							if(tipoEvento==Evento_Acciones.BAJA_UNIDADES){
								intUnidades -= 1;
								if(intUnidades<0){
									intUnidades=0;
								}
							}else{
								intUnidades += 1;
							}
							txtUnidades.setText(String.valueOf(intUnidades));
							controlUnidades unidades = (controlUnidades)txtUnidades.getParent().getParent().getParent();
							View vItemProducto = (View)unidades.getParent().getParent();
							String claveProducto = vItemProducto.getTag().toString();
							AccionesListas acListas = new AccionesListas(evDatos.getInstancia());
							acListas.Producto_Actualizar(claveProducto, intUnidades);
							break;
						case CARGA_PRODUCTOS_CATEGORIA:
							AccionesListas acclistas = new AccionesListas(evDatos.getInstancia());
							acclistas.MuestraListProductos(v);						
							break;
						case COMPARTIR_LISTA:
							CompartirListas compartir = new CompartirListas(evDatos.getInstancia());
							compartir.CompartirLista_AddContactos(evDatos.getIntInfoEvento()[0]);
							break;
						case ELIMINAR_PRODUCTO:
							activityLista acLista = (activityLista)evDatos.getInstancia();
							acLista.DialogoConfirmacion(en_MensajeEliminacion.ELIMINAR_PRODUCTO, evDatos.getIntInfoEvento());
							break;
						case GRABAR_NUEVA_CATEGORIA:
							AccionesListas listaNuevaCategoria = new AccionesListas((activityNuevaCategoria)evDatos.getInstancia());
							Hashtable<String, EditText> htInfo = evDatos.getInfoEvento();
							EditText txtId = htInfo.get("idLista");
							listaNuevaCategoria.AddListaCategoria(Integer.parseInt(txtId.getText().toString()));
							break;
						case GRABAR_NUEVA_LISTA:
							AccionesListas listas = new AccionesListas((activityNuevaLista)evDatos.getInstancia());
							listas.AddListaCabecera();
							break;
						case ICONO_LISTA:
						case ICONO_CATEGORIA:
							evDatos.getInstancia().showDialog(0);
							break;
						case ICONO_LISTA_GALERIA:
							Intent intGaleria = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
							requestCode=1;
							evDatos.getInstancia().startActivityForResult(intGaleria, requestCode);
							break;
						case ICONO_LISTA_CAMARA:
							requestCode=2;
							Intent intCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							evDatos.getInstancia().startActivityForResult(intCamara, requestCode);
							break;						
						case LOGIN:
							Registro login = new Registro(evDatos.getInstancia());
							boolean bLogado = login.LoginUsuario();
							if(!bLogado){
								Toast.makeText(evDatos.getInstancia(), evDatos.getInstancia().getResources().getText(net.jma.iman.R.string.msgErrorLogin),Toast.LENGTH_SHORT).show();
								bContinuar=false;							
							}
							break;						
						case REGISTRO_USUARIO:
							Registro regUsuario = new Registro(evDatos.getInstancia());
							regUsuario.RegistroUsuario();
							break;			
					}
					
				}catch (Exception ex){
					bContinuar = false;
				}
			}
			
			if(bContinuar){
				this.haciaDestino(evDatos);
			}
			
		}catch (Exception ex){
			
		}
	
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Evento_Datos evDatos = (Evento_Datos)arg0.getTag();
		Hashtable<String, EditText> htInfo = new Hashtable<String, EditText>();
		EditText txt = new EditText(evDatos.getInstancia());
		txt.setText(arg1.getTag().toString());
		htInfo.put("idLista", txt);
		evDatos.setInfoEvento(htInfo);
		this.haciaDestino(evDatos);		
	}
	
	private void haciaDestino(Evento_Datos evDatos){
		if(evDatos.getDestino()!=null){
			Intent intent = new Intent(evDatos.getInstancia(), (Class<?>) evDatos.getDestino());
			
			if(evDatos.getInfoEvento()!=null){
				Bundle bundle = new Bundle();
				Enumeration<String> e = evDatos.getInfoEvento().keys();
				while(e.hasMoreElements()){
					String clave = e.nextElement();
					EditText txt = evDatos.getInfoEvento().get(clave);
					bundle.putString(clave, txt.getText().toString());				
				}			
				intent.putExtras(bundle);			
			}

			evDatos.getInstancia().startActivity(intent);
		}		
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Evento_Datos evDatos = (Evento_Datos)arg0.getTag();
		if(evDatos!=null){
			Hashtable<String, EditText> infoEvento = evDatos.getInfoEvento();
			EditText txtControlEvento = infoEvento.get("omitir");
			if(txtControlEvento.getText().toString().equals("CARGADO")){
				if(evDatos.tipoEvento==Evento_Acciones.SELECCION_KILOS){
					Spinner drpKilos = (Spinner)evDatos.getInstancia().findViewById(R.id.drpKilos);
					controlUnidades unidades = (controlUnidades)drpKilos.getParent().getParent().getParent();
					View vItemProducto = (View)unidades.getParent().getParent();
					String claveProducto = vItemProducto.getTag().toString();					
					@SuppressWarnings("unchecked")
					ArrayAdapter<String> myAdap = (ArrayAdapter<String>)drpKilos.getAdapter(); 
					AccionesListas acListas = new AccionesListas(evDatos.getInstancia());
					acListas.Producto_Actualizar(claveProducto, myAdap.getItem(arg2));
				}
			}
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		//vacio
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Evento_Datos evDatos = (Evento_Datos)buttonView.getTag();
		View vItemProducto = (View)buttonView.getParent().getParent();
		String claveProducto = vItemProducto.getTag().toString();		
		en_VistaLista tipoVista = (en_VistaLista)vItemProducto.findViewById(R.id.ctrlUnidades).getTag();
		AccionesListas acListas = new AccionesListas(evDatos.getInstancia());
		acListas.Producto_Actualizar(claveProducto, isChecked, tipoVista);		
	}

	public void onCancel(DialogInterface dialog) {
		dlg_Base dialogoBase = (dlg_Base)dialog;
		Evento_Datos evDatos = dialogoBase.getTag();
		boolean bContinuar=this.compruebaObligatorios(evDatos);
		if (bContinuar){
			switch(evDatos.getTipoEvento()){
				case DIALOGO_ADD_PRODUCTO:
					dlg_AddProducto dlgProducto = (dlg_AddProducto)dialogoBase;
					if(dlgProducto.pulsadoBoton()){
						EditText txtProducto = (EditText)dlgProducto.findViewById(R.id.txtAddProductoNombre);
						RadioGroup rbTipoCantidad = (RadioGroup)dlgProducto.findViewById(R.id.rbTipoCantidad);
						int tipoCantidad;
						if(rbTipoCantidad.getCheckedRadioButtonId()==R.id.rbPeso){
							tipoCantidad = 1;
						}else{
							tipoCantidad = 2;
						}
						activityLista actividad = (activityLista)evDatos.getInstancia();
						AccionesListas acListas = new AccionesListas(actividad);
						acListas.Producto_Insertar(dlgProducto.idLista(), dlgProducto.idCategoria(), tipoCantidad, txtProducto.getText().toString());
						actividad.cargaCategoriasLista(acListas);															
					}				
					break;
				case DIALOGO_SELECCION_CATEGORIA:						
					dlg_SeleccionCategoria dlg = (dlg_SeleccionCategoria)dialogoBase;
					if(dlg.pulsadoBoton()){
						int idLista = dlg.idLista();
						ListView lvCategorias = (ListView)dlg.findViewById(R.id.lvSeleccionCategorias);
						int[] idsCategoria = new int[lvCategorias.getChildCount()];
						boolean bRecargar = false;
				
						for(int intI=0; intI<=lvCategorias.getChildCount()-1;intI++){
							View itemCategoria = lvCategorias.getChildAt(intI);
							TextView txtNombreCategoria = (TextView)itemCategoria.findViewById(R.id.lblNuevaListaItemCategoriaNombre);
							CheckBox chkCategoria = (CheckBox)itemCategoria.findViewById(R.id.chkNuevaListaItemNecesito);
							if(chkCategoria.isChecked()){
								int idCategoria = Integer.parseInt(txtNombreCategoria.getTag().toString());
								idsCategoria[intI] = idCategoria;
								bRecargar = true;
							}									
						} 		
						
						activityLista actividad = (activityLista)evDatos.getInstancia();
				
						AccionesListas acListas = new AccionesListas(actividad);
						acListas.AddListaCategorias(idLista, idsCategoria);
						
						if(bRecargar){
							actividad.cargaCategoriasLista(acListas);
						}
					}
					break;
			}			
		}		
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		ListView lvListas = (ListView)arg1.getParent();
		Evento_Datos evDatos = (Evento_Datos)lvListas.getTag();
		if(evDatos.getTipoEvento()==Evento_Acciones.ELIMINAR_LISTA){
			int idLista = Integer.valueOf(arg1.getTag().toString());
			activityPrincipal activListas = (activityPrincipal)evDatos.getInstancia();
			activListas.DialogoConfirmacion(idLista);			
		}
		return false;
	}

	public boolean onLongClick(View v) {
		Evento_Datos evDatos = (Evento_Datos)v.getTag();	
		activityLista activLista = (activityLista)evDatos.getInstancia();
		activLista.DialogoConfirmacion(en_MensajeEliminacion.ELIMINAR_CATEGORIA, evDatos.getIntInfoEvento());
		return true;			
	}


	
}
