package net.jma.clases;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import net.jma.iman.R;
import net.jma.iman.activityNuevaCategoria;
import net.jma.iman.activityNuevaLista;
import net.jma.iman.controlUnidades;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AccionesListas {

	Activity gActividad;
	int version;
		
	public AccionesListas(Activity pActivity){
		super();
		this.gActividad = pActivity;
		PackageManager manager = pActivity.getPackageManager();
		try{
			PackageInfo info = manager.getPackageInfo(pActivity.getPackageName(), 0);
			version = info.versionCode;					
		}catch (Exception ex){
			version = 1;
		}
	}
	
	public void AddListaCabecera() throws Exception {
		activityNuevaLista v_Activity = (activityNuevaLista)gActividad;
		try{			
			ImageButton imgIcono = (ImageButton)v_Activity.findViewById(net.jma.iman.R.id.imgIcono);
			EditText txtNombre = (EditText)v_Activity.findViewById(net.jma.iman.R.id.txtNombreLista);
			EditText txtDescripcion = (EditText)v_Activity.findViewById(net.jma.iman.R.id.txtDescripcionLista);
			ListView lvCategorias = (ListView)v_Activity.findViewById(net.jma.iman.R.id.lvNuevaListaCategorias);

			AccesoBD accBD = new AccesoBD(v_Activity,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			
			db.beginTransaction();
			try {
				//grabar la lista
				ContentValues valRegistro = new ContentValues();
				Integer id = accBD.obtenMaxId(db, ConstantesBD.BD_TABLA_LISTAS, ConstantesBD.TB_LISTAS_idLista, "", null);
				valRegistro.put(ConstantesBD.TB_LISTAS_idLista, id);
				valRegistro.put(ConstantesBD.TB_LISTAS_NombreLista, txtNombre.getText().toString());
				valRegistro.put(ConstantesBD.TB_LISTAS_Descripcion, txtDescripcion.getText().toString());
				Drawable d = imgIcono.getDrawable();  
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();			
				valRegistro.put(ConstantesBD.TB_LISTAS_IconoLista, byteArray);
				db.insert(ConstantesBD.BD_TABLA_LISTAS, null, valRegistro);
				//grabar las categorias de la lista
				for(int intI=0; intI<=lvCategorias.getChildCount()-1;intI++){
					View itemCategoria = lvCategorias.getChildAt(intI);
					TextView txtNombreCategoria = (TextView)itemCategoria.findViewById(R.id.lblNuevaListaItemCategoriaNombre);
					CheckBox chkCategoria = (CheckBox)itemCategoria.findViewById(R.id.chkNuevaListaItemNecesito);
					if(chkCategoria.isChecked()){
						int idCategoria = Integer.parseInt(txtNombreCategoria.getTag().toString());
						this.AddListaCategoria(db, id, idCategoria);
					}									
				}     				
				db.setTransactionSuccessful();
			} catch (Exception ex){
				throw ex;
			} finally {
				db.endTransaction();
				db.close();					
			}			 			
			
		}catch (Exception ex){
			Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorLista),Toast.LENGTH_SHORT).show();			
			throw ex;
		}
	}
	
	public void AddListaCategoria(int idLista) throws Exception{
		activityNuevaCategoria v_Activity = (activityNuevaCategoria)gActividad;
		Boolean bExiste = false;
		try{
			ImageButton imgIcono = (ImageButton)v_Activity.findViewById(net.jma.iman.R.id.imgIcono);
			EditText txtNombre = (EditText)v_Activity.findViewById(net.jma.iman.R.id.txtNuevaCategoriaNombre);
			
			AccesoBD accBD = new AccesoBD(v_Activity,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getWritableDatabase();			
			//comprobar que no existe una categoría con el mismo nombre
			bExiste = accBD.existeEnBD(db, ConstantesBD.BD_TABLA_CATEGORIAS, ConstantesBD.TB_CATEGORIAS_Categoria, ConstantesBD.TB_CATEGORIAS_Categoria + "= ?", new String[]{txtNombre.getText().toString()});
			if(!bExiste){						
				db.beginTransaction();
				try {
					//grabar la nueva categoría
					Integer idCategoria = accBD.obtenMaxId(db, ConstantesBD.BD_TABLA_CATEGORIAS, ConstantesBD.TB_CATEGORIAS_idCategoria, "", null);
					ContentValues valCategoria = new ContentValues();
					valCategoria.put(ConstantesBD.TB_CATEGORIAS_idCategoria, idCategoria);
					valCategoria.put(ConstantesBD.TB_CATEGORIAS_Categoria, txtNombre.getText().toString());
					Drawable d = imgIcono.getDrawable();  
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray();			
					valCategoria.put(ConstantesBD.TB_CATEGORIAS_IconoCategoria, byteArray);
					db.insert(ConstantesBD.BD_TABLA_CATEGORIAS, null, valCategoria);								
					//grabar la categoría asociada a la lista
					this.AddListaCategoria(db, idLista, idCategoria);				
					db.setTransactionSuccessful();
				} catch (Exception ex){
					throw ex;
				} finally {
					db.endTransaction();
					db.close();					
				}
			}else{
				Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorNuevaCategoriaExiste),Toast.LENGTH_SHORT).show();			
				throw new Exception();
			}
						
		}catch (Exception ex){
			if(!bExiste){
				Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorNuevaCategoria),Toast.LENGTH_SHORT).show();
			}
			throw ex;
		}
	}
	
	private void AddListaCategoria(SQLiteDatabase db, int idLista, int idCategoria) throws Exception{
		try{
			ContentValues valores = new ContentValues();
			valores.put(ConstantesBD.TB_LISTAS_CATEGORIAS_idLista, idLista);
			valores.put(ConstantesBD.TB_LISTAS_CATEGORIAS_idCategoria, idCategoria);
			db.insert(ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS, null, valores);
		}catch (Exception ex){
			throw ex;
		}
	}
	
	public void AddListaCategorias(int idLista, int[] idsCategorias){
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getWritableDatabase();	
			db.beginTransaction();
			try {
				//grabar las categorías asociadas a la lista
				for(int intI = 0; intI <= idsCategorias.length -1; intI++){
					if(idsCategorias[intI]!=0){
						this.AddListaCategoria(db, idLista, idsCategorias[intI]);
					}
				}
				db.setTransactionSuccessful();
			} catch (Exception ex){
				throw ex;
			} finally {
				db.endTransaction();
				db.close();					
			}						
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorNuevaCategoria),Toast.LENGTH_SHORT).show();			
		}
	}
	
	public Cursor DameCategorias(int idCategoria){
		Cursor ret = null;
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			if (idCategoria==-1){
				ret = db.query(ConstantesBD.BD_TABLA_CATEGORIAS, new String[]{ConstantesBD.TB_CATEGORIAS_idCategoria + " AS _id", ConstantesBD.TB_CATEGORIAS_Categoria}, null, null, null, null, ConstantesBD.TB_CATEGORIAS_Categoria);
			}else{
				String where = ConstantesBD.TB_CATEGORIAS_idCategoria + "=?";
				String whereArgs[] = new String[1];
				whereArgs[0] = String.valueOf(idCategoria);
				ret = db.query(ConstantesBD.BD_TABLA_CATEGORIAS, new String[]{ConstantesBD.TB_CATEGORIAS_idCategoria + " AS _id", ConstantesBD.TB_CATEGORIAS_Categoria}, where, whereArgs, null, null, ConstantesBD.TB_CATEGORIAS_Categoria);				
			}
		}catch (Exception ex){
			
		}
		return ret;
	}

	public Cursor DameLista(int idLista){
		Cursor ret = null;
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			ret = db.query(ConstantesBD.BD_TABLA_LISTAS, new String[]{ConstantesBD.TB_LISTAS_NombreLista, ConstantesBD.TB_LISTAS_Descripcion, ConstantesBD.TB_LISTAS_IconoLista}, ConstantesBD.TB_LISTAS_idLista + "=?", new String[]{String.valueOf(idLista)}, null, null, null);
		}catch (Exception ex){
			
		}
		return ret;
	}
			
	public Cursor DameListaCategorias(int idLista, boolean bFiltroTienda){
		Cursor ret = null;
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			String tabla = ConstantesBD.BD_TABLA_LISTAS + "," + ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS + "," + ConstantesBD.BD_TABLA_CATEGORIAS;
			String[] columnas = new String[]{ConstantesBD.BD_TABLA_LISTAS + "." + ConstantesBD.TB_LISTAS_NombreLista, 
					ConstantesBD.BD_TABLA_LISTAS + "." + ConstantesBD.TB_LISTAS_Descripcion, 
					ConstantesBD.BD_TABLA_LISTAS + "." + ConstantesBD.TB_LISTAS_IconoLista, 
					ConstantesBD.BD_TABLA_CATEGORIAS + "." + ConstantesBD.TB_CATEGORIAS_idCategoria + " AS _id", 
					ConstantesBD.BD_TABLA_CATEGORIAS + "." + ConstantesBD.TB_CATEGORIAS_Categoria, 
					ConstantesBD.BD_TABLA_CATEGORIAS + "." + ConstantesBD.TB_CATEGORIAS_IconoCategoria};
			String join = "AND " + ConstantesBD.BD_TABLA_LISTAS + "." + ConstantesBD.TB_LISTAS_idLista + "=" +
					ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS + "." + ConstantesBD.TB_LISTAS_CATEGORIAS_idLista + " AND " +
					ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS + "." + ConstantesBD.TB_LISTAS_CATEGORIAS_idCategoria + "=" + 
					ConstantesBD.BD_TABLA_CATEGORIAS + "." + ConstantesBD.TB_CATEGORIAS_idCategoria;
			if(bFiltroTienda){
				join += " AND " + ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS + "." + ConstantesBD.TB_LISTAS_CATEGORIAS_idCategoria + 
						" IN (SELECT " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + " FROM " + ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS + " WHERE " + ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=" + idLista + 
						" AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoLista + "=1)";
			}
			String where = ConstantesBD.BD_TABLA_LISTAS + "." + ConstantesBD.TB_LISTAS_idLista + "=? " + join;
			String[] whereArgs = new String[]{String.valueOf(idLista)};
			String orden = ConstantesBD.BD_TABLA_CATEGORIAS + "." + ConstantesBD.TB_CATEGORIAS_Categoria;
			ret = db.query(tabla, columnas, where, whereArgs, null, null, orden);	
			//ret.moveToFirst();
		}catch (Exception ex){
			
		}
		return ret;
	}
	
	public Cursor DameListaNoCategorias(int idLista){
		Cursor ret = null;
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			String where = ConstantesBD.TB_CATEGORIAS_idCategoria + " NOT IN (SELECT " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + " FROM " + ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS + 
					" WHERE " + ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=" + idLista + ")" + 
					" AND " + ConstantesBD.TB_CATEGORIAS_idCategoria + " NOT IN (SELECT " + ConstantesBD.TB_LISTAS_CATEGORIAS_idCategoria + " FROM " + ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS + 
					" WHERE " + ConstantesBD.TB_LISTAS_CATEGORIAS_idLista + "=" + idLista + ")";
			ret = db.query(ConstantesBD.BD_TABLA_CATEGORIAS, new String[]{ConstantesBD.TB_CATEGORIAS_idCategoria + " AS _id", ConstantesBD.TB_CATEGORIAS_Categoria}, where, null, null, null, ConstantesBD.TB_CATEGORIAS_Categoria);
		}catch (Exception ex){
			
		}
		return ret;			
	}
	
	public Cursor DameListaProductos(int idLista, int idCategoria, boolean bFiltroTienda){
		Cursor ret = null;
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			String[] columnas = new String[]{ConstantesBD.TB_LISTAS_PRODUCTOS_idProducto + " AS _id", 
					ConstantesBD.TB_LISTAS_PRODUCTOS_Producto, ConstantesBD.TB_LISTAS_PRODUCTOS_TipoCantidad, 
					ConstantesBD.TB_LISTAS_PRODUCTOS_Cantidad, ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoLista,
					ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoTienda};
			String where;
			String[] whereArgs;
			if(bFiltroTienda){
				where = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=? AND " +
						ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoLista + "=?";
				whereArgs = new String[]{String.valueOf(idLista),String.valueOf(idCategoria),"1"};				
			}else{
				where = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=?";
				whereArgs = new String[]{String.valueOf(idLista),String.valueOf(idCategoria)};
			}
			String orden = ConstantesBD.TB_LISTAS_PRODUCTOS_Producto;			
			ret = db.query(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, columnas, where, whereArgs, null, null, orden);	
		}catch (Exception ex){
			
		}
		return ret;
	}
	
	public Cursor DameListas(){
		Cursor ret = null;
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			ret = db.query(ConstantesBD.BD_TABLA_LISTAS, new String[]{ConstantesBD.TB_LISTAS_idLista + " AS _id", ConstantesBD.TB_LISTAS_NombreLista, ConstantesBD.TB_LISTAS_Descripcion, ConstantesBD.TB_LISTAS_IconoLista}, null, null, null, null, ConstantesBD.TB_LISTAS_NombreLista);
		}catch (Exception ex){
			
		}
		return ret;
	}
	
	public void EliminarCategoria(int idLista, int idCategoria){
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getWritableDatabase();	
			db.beginTransaction();
			try {
				//eliminar los registros asociados a la relación de lista y categoría
				String[] arrWhere = new String[2];
				arrWhere[0] = String.valueOf(idLista);
				arrWhere[1] = String.valueOf(idCategoria);
				String strWhere = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " + 
				ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=?";
				db.delete(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, strWhere, arrWhere);
				
				strWhere = ConstantesBD.TB_LISTAS_CATEGORIAS_idLista + "=? AND " + 
				ConstantesBD.TB_LISTAS_CATEGORIAS_idCategoria + "=?";
				db.delete(ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS, strWhere, arrWhere);									
				
				db.setTransactionSuccessful();
			} catch (Exception ex){
				throw ex;
			} finally {
				db.endTransaction();
				db.close();					
			}								
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorEliminarCategoria),Toast.LENGTH_SHORT).show();															
		}
	}
	
	public void EliminarLista(int idLista){
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			db.beginTransaction();
			try {
				String[] arrWhere = new String[1];
				arrWhere[0] = String.valueOf(idLista);
				String strWhere = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=?";
				db.delete(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, strWhere, arrWhere);					
				
				strWhere = ConstantesBD.TB_LISTAS_CATEGORIAS_idLista + "=?";
				db.delete(ConstantesBD.BD_TABLA_LISTAS_CATEGORIAS, strWhere, arrWhere);									
			
				strWhere = ConstantesBD.TB_LISTAS_idLista + "=?";
				db.delete(ConstantesBD.BD_TABLA_LISTAS, strWhere, arrWhere);					

				db.setTransactionSuccessful();
			} catch (Exception ex){
				throw ex;
			} finally {
				db.endTransaction();
				db.close();					
			}								
			
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorEliminarLista),Toast.LENGTH_SHORT).show();												
		}
	}
	
	public void MuestraListProductos(View vItemCategoria){
		float altoBase = 45.0f;
		float densidad = gActividad.getResources().getDisplayMetrics().density;
		int altoControl = (int) (altoBase * densidad + 0.5f);
		LayoutParams lp = vItemCategoria.getLayoutParams();
		if(lp.height==altoControl){
			
			LinearLayout ll = (LinearLayout)vItemCategoria.findViewById(R.id.llProductos);
			AccionesListas accListas = new AccionesListas((Activity) gActividad);
			Evento_Datos evDatos = (Evento_Datos)vItemCategoria.getTag();
			int idLista = evDatos.getIntInfoEvento()[0];
			int idCategoria = evDatos.getIntInfoEvento()[1];
			en_VistaLista tipoVista;
			if(evDatos.getIntInfoEvento()[2]==1){
				tipoVista = en_VistaLista.VISTA_LISTA;
			}else{
				tipoVista = en_VistaLista.VISTA_TIENDA;
			}
			Cursor productos;
			if(tipoVista==en_VistaLista.VISTA_LISTA){
				productos = accListas.DameListaProductos(idLista, idCategoria, false);						
			}else{
				productos = accListas.DameListaProductos(idLista, idCategoria, true);			
			}
			LayoutInflater mInflater = gActividad.getLayoutInflater();
			if(productos.moveToFirst()){			
				do{
					View vItemProducto = mInflater.inflate(R.layout.listitemproducto, (ViewGroup) vItemCategoria, false);	
					String claveProducto = String.valueOf(idLista) + ";" + String.valueOf(idCategoria) + ";" + String.valueOf(productos.getInt(0));
					vItemProducto.setTag(claveProducto);
					ImageButton imgEliminar = (ImageButton)vItemProducto.findViewById(R.id.imgQuitaProducto);
					Evento_Datos evEliminar = new Evento_Datos();
					evEliminar.setTipoEvento(Evento_Acciones.ELIMINAR_PRODUCTO);
					evEliminar.setInstancia((Activity)gActividad);
					int[] arrIds = new int[3];
					arrIds[0] = idLista;
					arrIds[1] = idCategoria;
					arrIds[2] = productos.getInt(0);
					evEliminar.setInfoEvento(arrIds);
					imgEliminar.setTag(evEliminar);
					imgEliminar.setOnClickListener(new Eventos());
					TextView txtProducto  = (TextView)vItemProducto.findViewById(R.id.lblItemProductoNombre);
					txtProducto.setText(productos.getString(productos.getColumnIndex(ConstantesBD.TB_LISTAS_PRODUCTOS_Producto)));
					Boolean bSelLista = productos.getInt(productos.getColumnIndex(ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoLista)) > 0; // Boolean.parseBoolean(productos.getString(productos.getColumnIndex(ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoLista)));
					Boolean bSelTienda = Boolean.parseBoolean(productos.getString(productos.getColumnIndex(ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoTienda)));
					CheckBox chkNecesitoTengo = (CheckBox)vItemProducto.findViewById(R.id.chkNecesito);
					if(tipoVista==en_VistaLista.VISTA_LISTA){
						chkNecesitoTengo.setChecked(bSelLista);
					}else{
						chkNecesitoTengo.setChecked(bSelTienda);
					}				
					Evento_Datos ev = new Evento_Datos();
					ev.setInstancia((Activity)vItemProducto.getContext());
					ev.setTipoEvento(Evento_Acciones.NECESITO_TENGO);
					chkNecesitoTengo.setTag(ev);
					chkNecesitoTengo.setOnCheckedChangeListener(new Eventos());
					int tipoCantidad = productos.getInt(productos.getColumnIndex(ConstantesBD.TB_LISTAS_PRODUCTOS_TipoCantidad));
					float cantidad = productos.getFloat(productos.getColumnIndex(ConstantesBD.TB_LISTAS_PRODUCTOS_Cantidad));				
					controlUnidades ctrlUnidades = (controlUnidades)vItemProducto.findViewById(R.id.ctrlUnidades);
					if(tipoCantidad==1){
						ctrlUnidades.ConfiguraVista(false);
					}else{
						ctrlUnidades.ConfiguraVista(true);
					}
					ctrlUnidades.setUnidades(cantidad);
					ctrlUnidades.setTag(tipoVista);
					ll.addView(vItemProducto);				
				}while (productos.moveToNext());
			}
			productos.close();		
			
			lp.height = LayoutParams.WRAP_CONTENT;
			vItemCategoria.setLayoutParams(lp);	
		}else{
			LinearLayout ll = (LinearLayout)vItemCategoria.findViewById(R.id.llProductos);			
			ll.removeAllViews();			
			lp.height = altoControl;
			vItemCategoria.setLayoutParams(lp);
		}
				
	}
	
	public void Producto_Actualizar(String claveProducto, int intUnidades){
		try{
			int idLista = Integer.parseInt(claveProducto.split(";")[0]);
			int idCategoria = Integer.parseInt(claveProducto.split(";")[1]);
			int idProducto = Integer.parseInt(claveProducto.split(";")[2]);
			
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			ContentValues valProducto = new ContentValues();
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_Cantidad, intUnidades);
			String where = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=? AND " +
					ConstantesBD.TB_LISTAS_PRODUCTOS_idProducto + "=?";
			String whereArgs[] = new String[]{String.valueOf(idLista),String.valueOf(idCategoria),String.valueOf(idProducto)};
			db.update(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, valProducto, where, whereArgs);			
			db.close();			
			
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorLista),Toast.LENGTH_SHORT).show();									
		}
	}

	public void Producto_Actualizar(String claveProducto, String strKilos){
		try{
			int idLista = Integer.parseInt(claveProducto.split(";")[0]);
			int idCategoria = Integer.parseInt(claveProducto.split(";")[1]);
			int idProducto = Integer.parseInt(claveProducto.split(";")[2]);
			
			float fKilos = 0;
			if(strKilos.contains(">")){
				fKilos = 3;
			}else{
				if(strKilos.contains("kg")){
					strKilos = strKilos.substring(0, strKilos.indexOf(" "));
					strKilos = strKilos.replace(",", ".");
					fKilos = Float.parseFloat(strKilos);
				}else{
					fKilos = Float.parseFloat(strKilos.substring(0, strKilos.indexOf(" ")));
					fKilos = fKilos / 1000;
				}
			}
			
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			ContentValues valProducto = new ContentValues();
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_Cantidad, fKilos);
			String where = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=? AND " +
					ConstantesBD.TB_LISTAS_PRODUCTOS_idProducto + "=?";
			String whereArgs[] = new String[]{String.valueOf(idLista),String.valueOf(idCategoria),String.valueOf(idProducto)};
			db.update(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, valProducto, where, whereArgs);			
			db.close();			
			
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorLista),Toast.LENGTH_SHORT).show();									
		}		
	}
	
	public void Producto_Actualizar(String claveProducto, boolean NecesitoTengo, en_VistaLista tipoVista){
		try{
			int idLista = Integer.parseInt(claveProducto.split(";")[0]);
			int idCategoria = Integer.parseInt(claveProducto.split(";")[1]);
			int idProducto = Integer.parseInt(claveProducto.split(";")[2]);
			
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			ContentValues valProducto = new ContentValues();
			if(tipoVista==en_VistaLista.VISTA_LISTA){
				valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoLista, NecesitoTengo);
			}else{
				valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoTienda, NecesitoTengo);
			}
			String where = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=? AND " +
					ConstantesBD.TB_LISTAS_PRODUCTOS_idProducto + "=?";
			String whereArgs[] = new String[]{String.valueOf(idLista),String.valueOf(idCategoria),String.valueOf(idProducto)};
			db.update(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, valProducto, where, whereArgs);			
			db.close();
						
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorLista),Toast.LENGTH_SHORT).show();									
		}		
	}
	
	public void Producto_Eliminar(int idLista, int idCategoria, int idProducto){
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			String[] arrWhere = new String[3];
			arrWhere[0] = String.valueOf(idLista);
			arrWhere[1] = String.valueOf(idCategoria);
			arrWhere[2] = String.valueOf(idProducto);
			String strWhere = ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " +
			ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=? AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_idProducto + "=?";
			db.delete(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, strWhere, arrWhere);						
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorEliminarProducto),Toast.LENGTH_SHORT).show();															
		}
	}
	
	public void Producto_Insertar(int idLista, int idCategoria, int tipoCantidad, String nombreProducto){
		try{
			AccesoBD accBD = new AccesoBD(gActividad,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			ContentValues valProducto = new ContentValues();

			String[] arrWhere = new String[2];
			arrWhere[0] = String.valueOf(idLista);
			arrWhere[1] = String.valueOf(idCategoria);
			Integer idProducto = accBD.obtenMaxId(db, ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, ConstantesBD.TB_LISTAS_PRODUCTOS_idProducto, ConstantesBD.TB_LISTAS_PRODUCTOS_idLista + "=? AND " + ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria + "=?", arrWhere);
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_idLista, idLista);
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_idCategoria, idCategoria);
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_idProducto, idProducto);
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_TipoCantidad, tipoCantidad);
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_Producto, nombreProducto);
			if(tipoCantidad==1){
				valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_Cantidad, 0.25);
			}else{
				valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_Cantidad, 0);
			}
			Calendar c = Calendar.getInstance();
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_Actualizado, c.getTime().toLocaleString());
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoLista, false);
			valProducto.put(ConstantesBD.TB_LISTAS_PRODUCTOS_seleccionadoTienda, false);
			db.insert(ConstantesBD.BD_TABLA_LISTAS_PRODUCTOS, null, valProducto);			
			
		}catch (Exception ex){
			Toast.makeText(gActividad, gActividad.getResources().getText(net.jma.iman.R.string.msgErrorNuevoProducto),Toast.LENGTH_SHORT).show();												
		}
	}

}
