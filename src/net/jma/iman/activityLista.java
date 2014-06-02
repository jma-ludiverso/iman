package net.jma.iman;

import net.jma.clases.AccionesListas;
import net.jma.clases.AdaptadorCategorias;
import net.jma.clases.en_MensajeEliminacion;
import net.jma.clases.en_VistaLista;
import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class activityLista extends Activity {

	int idLista;
	Cursor cLista;
	TextView txtVista;
	en_VistaLista tipoVista;
	boolean bCategorias;
	en_MensajeEliminacion dialogo_TipoMensaje;
	int[] dialogo_Ids;
	boolean bDesdeDialogo = false;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		
		idLista = Integer.parseInt(getIntent().getExtras().getString("idLista"));
		
		txtVista = (TextView)findViewById(R.id.txtListaTextoVista);
		if(savedInstanceState!=null){
			if(savedInstanceState.getSerializable("tipoVista")!=null){
				this.tipoVista = (en_VistaLista)savedInstanceState.getSerializable("tipoVista");
				if (this.tipoVista==en_VistaLista.VISTA_LISTA){
					txtVista.setText(R.string.listaVistaListaLista);			
				}else{
					txtVista.setText(R.string.listaVistaListaTienda);				
				}
			}else{
				txtVista.setText(R.string.listaVistaListaLista);
				tipoVista = en_VistaLista.VISTA_LISTA;				
			}
		}else{
			txtVista.setText(R.string.listaVistaListaLista);
			tipoVista = en_VistaLista.VISTA_LISTA;
		}
				
		AccionesListas listas = new AccionesListas(this);
		this.cargaCategoriasLista(listas);
		
		boolean bDatos = cLista.moveToFirst();
		if(!bDatos){
			bCategorias=false;
			cLista.close();
			cLista = listas.DameLista(idLista);
			cLista.moveToFirst();
		}else{
			bCategorias=true;
		}
		
		ImageView img = (ImageView)findViewById(R.id.imgListaIcono);
		TextView txtNombre = (TextView)findViewById(R.id.lblListaNombre);
		TextView txtDescripcion = (TextView)findViewById(R.id.lblListaDescripcion);
		
		byte[] byIcono = cLista.getBlob(2);
		Bitmap bmIcono = BitmapFactory.decodeByteArray(byIcono, 0, byIcono.length);
		img.setImageBitmap(bmIcono);
		
		txtNombre.setText(cLista.getString(0));
		txtDescripcion.setText(cLista.getString(1));
				
	}	
	
	public void cargaCategoriasLista(AccionesListas listas){
		try{
			if(tipoVista==en_VistaLista.VISTA_LISTA){
				cLista = listas.DameListaCategorias(idLista, false);
			}else{
				cLista = listas.DameListaCategorias(idLista, true);
			}
			
			ListView lvCategorias = (ListView)findViewById(R.id.lvMisCategorias);
			AdaptadorCategorias ad = new AdaptadorCategorias(this, cLista, false);
			ad.setDatosLista(idLista, tipoVista);
			lvCategorias.setAdapter(ad);
			lvCategorias.setOnScrollListener(new OnScrollListener() {
				
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					
				}
				
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					for(int intI=0;intI<=totalItemCount-1;intI++){
						View vItemCategoria = view.getChildAt(intI);
						if(vItemCategoria!=null){
							LinearLayout ll = (LinearLayout)vItemCategoria.findViewById(R.id.llProductos);
							if(ll!=null){
								if(ll.getChildCount()>0){
									Evento_Datos evDatos = (Evento_Datos)vItemCategoria.getTag();
									int[] arrInfo = evDatos.getIntInfoEvento();
									View vItemProducto = ll.getChildAt(0);
									String claveProd = (String)vItemProducto.getTag();
									String[] arrClaves = claveProd.split(";");
									int idLista = Integer.parseInt(arrClaves[0]);
									int idCategoria = Integer.parseInt(arrClaves[1]);
									if(arrInfo[0]!=idLista || arrInfo[1]!=idCategoria){
										AccionesListas accListas = new AccionesListas((Activity)view.getContext());
										accListas.MuestraListProductos(vItemCategoria);
									}
								}													
							}							
						}
					}
				}
			});
			
		}catch(Exception ex){
			
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menulista, menu);
	    return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try{
			AccionesListas listas = new AccionesListas(this);
		    switch (item.getItemId()) {
		        case R.id.menListaLista:
		        	txtVista.setText(R.string.listaVistaListaLista);
		        	tipoVista = en_VistaLista.VISTA_LISTA;
		        	if(bCategorias){
		        		cLista.close();
		        		this.cargaCategoriasLista(listas);
		        	}
		            return true;
		        case R.id.menListaTienda:
		        	txtVista.setText(R.string.listaVistaListaTienda);
		        	tipoVista = en_VistaLista.VISTA_TIENDA;
		        	if(bCategorias){
		        		cLista.close();
		        		this.cargaCategoriasLista(listas);
		        	}	        	
		        	return true;
		        case R.id.menListaNuevaCategoria:
					Intent intent = new Intent(this, (Class<?>) activityNuevaCategoria.class);
					Bundle bundle = new Bundle();
					bundle.putInt("idLista", this.idLista);
					intent.putExtras(bundle);		
					this.startActivity(intent);  		        	
		        	return true;
		        case R.id.menListaSeleccionarCategoria:
		        	dlg_SeleccionCategoria dialogo = new dlg_SeleccionCategoria(this, this.idLista);
		        	Evento_Datos evDialogo = new Evento_Datos();
		        	evDialogo.setInstancia(this);
		        	evDialogo.setTipoEvento(Evento_Acciones.DIALOGO_SELECCION_CATEGORIA);
		        	dialogo.setTag(evDialogo);
		        	dialogo.setOnCancelListener(new Eventos());
		        	dialogo.show();
		        	return true;
		        case R.id.menListaCompartir:
		        	bDesdeDialogo = true;
					Intent intCompartir = new Intent(this, (Class<?>) activityCompartirLista.class);
					Bundle bundCompartir = new Bundle();
					bundCompartir.putInt("idLista", this.idLista);
					intCompartir.putExtras(bundCompartir);		
		        	this.startActivity(intCompartir);
		        	return true;
		        default:
		            return true; 
		    }
		}catch (Exception ex){
						
			return true;
		}
	}	
	
	public void DialogoConfirmacion(en_MensajeEliminacion tipoMensaje, int[] arrId){
		try{
			this.dialogo_TipoMensaje = tipoMensaje;
			this.dialogo_Ids = arrId;
			if(tipoMensaje==en_MensajeEliminacion.ELIMINAR_CATEGORIA){				
				this.showDialog(1);
			}else{
				if(tipoMensaje==en_MensajeEliminacion.ELIMINAR_PRODUCTO){
					this.showDialog(2);
				}
			}			
		}catch (Exception ex){
			
		}
	}
	
	public void DialogoConfirmado(){
		AccionesListas acListas = new AccionesListas(this);
		if(dialogo_TipoMensaje==en_MensajeEliminacion.ELIMINAR_CATEGORIA){
			acListas.EliminarCategoria(dialogo_Ids[0], dialogo_Ids[1]);
		}else{
			if(dialogo_TipoMensaje==en_MensajeEliminacion.ELIMINAR_PRODUCTO){
				acListas.Producto_Eliminar(dialogo_Ids[0], dialogo_Ids[1], dialogo_Ids[2]);
			}
		}
		this.cargaCategoriasLista(acListas);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);	    
		try{
		    builder.setTitle(R.string.confirmacionEliminar);
		    if(id==1){
		    	builder.setMessage(R.string.confirmacionEliminar_Categoria);
		    }else{
		    	if(id==2){
		    		builder.setMessage(R.string.confirmacionEliminar_Producto);
		    	}
		    }
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
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("tipoVista", tipoVista);
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cLista.close();
	}


	
	
}
